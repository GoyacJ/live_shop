package vip.xiaonuo.integral.modular.record.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.scheduling.annotation.Scheduled;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.integral.api.dto.ChangeIntegralDTO;
import vip.xiaonuo.integral.api.dto.UserIntegralChangeDTO;
import vip.xiaonuo.integral.api.dto.UserIntegralChangePageDTO;
import vip.xiaonuo.integral.api.enums.IntegralOperationSource;
import vip.xiaonuo.integral.api.enums.IntegralSource;
import vip.xiaonuo.integral.modular.integral.service.IntegralService;
import vip.xiaonuo.integral.modular.record.entity.IntegralRecord;
import vip.xiaonuo.integral.modular.record.mapper.IntegralRecordMapper;
import vip.xiaonuo.integral.modular.record.params.IntegralRecordPageParam;
import vip.xiaonuo.integral.modular.record.service.IntegralRecordService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/28 14:04
 */
@Slf4j
@Component
public class IntegralRecordServiceImpl extends ServiceImpl<IntegralRecordMapper, IntegralRecord> implements IntegralRecordService {

    @Inject
    private IntegralService integralService;

    @Override
    public Page<IntegralRecord> page(IntegralRecordPageParam param) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (ObjectUtil.isNotEmpty(param.startCreateTime()) && ObjectUtil.isNotEmpty(param.endCreateTime())) {
            queryWrapper.between(IntegralRecord::getCreateTime, param.startCreateTime(), param.endCreateTime());
        }

        if (ObjectUtil.isAllNotEmpty(param.sortField(), param.sortOrder())) {
            CommonSortOrderEnum.validate(param.sortOrder());
            queryWrapper.orderBy(CharSequenceUtil.toUnderlineCase(param.sortField()), param.sortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(IntegralRecord::getCreateTime, false);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public boolean addIntegralRecord(IntegralRecord integralLog) {
        return this.save(integralLog);
    }

    @Scheduled(cron = "0 0 2 * * ?")
    @Override
    public void expireIntegral() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime startOfDay = yesterday.atStartOfDay();
        LocalDateTime endOfDay = yesterday.atTime(LocalTime.MAX);

        List<IntegralRecord> integralRecords = this.list(new QueryWrapper()
                .eq(IntegralRecord::getClearExpireStatus, SimpleStatus.DISABLE.getCode())
                .isNotNull(IntegralRecord::getValidityPeriod)
                .between(IntegralRecord::getValidityPeriod, startOfDay, endOfDay));

        // 积分过期
        integralRecords.forEach(record -> integralService.changeIntegral(ChangeIntegralDTO.expireIntegral(
                record.getUserId(),
                record.getIntegral(),
                record.getUserId(),
                IntegralSource.SYSTEM,
                null,
                IntegralOperationSource.SYSTEM,
                "过期积分清除"
        )));
    }

    @Override
    public boolean deleteIntegralRecordByUserId(String userId) {
        return this.remove(new QueryWrapper().eq(IntegralRecord::getUserId, userId));
    }

    @Override
    public IntegralRecord detail(String id) {
        return this.getById(id);
    }

    @Override
    public Page<UserIntegralChangeDTO> userIntegralChangePage(UserIntegralChangePageDTO param) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(IntegralRecord::getUserId, param.userId());
        if (Objects.nonNull(param.integralSource())) {
            queryWrapper.eq(IntegralRecord::getIntegralSource, param.integralSource());
        }
        if (Objects.nonNull(param.integralCategory())) {
            queryWrapper.eq(IntegralRecord::getIntegralCategory, param.integralCategory());
        }
        if (Objects.nonNull(param.integralChangeType())) {
            queryWrapper.eq(IntegralRecord::getChangeType, param.integralChangeType());
        }

        if (Objects.nonNull(param.sort())) {
            if (CommonSortOrderEnum.ASC.equals(param.sort())) {
                queryWrapper.orderBy(IntegralRecord::getOperationTime, true);
            } else {
                queryWrapper.orderBy(IntegralRecord::getOperationTime, false);
            }
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper)
                .map(record -> {
                    UserIntegralChangeDTO dto = BeanUtil.copyProperties(record, UserIntegralChangeDTO.class);
                    dto.setIntegralRecordId(record.getId());
                    return dto;
                });
    }
}
