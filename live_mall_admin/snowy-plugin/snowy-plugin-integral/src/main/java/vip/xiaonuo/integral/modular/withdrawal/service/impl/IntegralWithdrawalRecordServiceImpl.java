package vip.xiaonuo.integral.modular.withdrawal.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.integral.modular.record.entity.IntegralRecord;
import vip.xiaonuo.integral.modular.withdrawal.entity.IntegralWithdrawalRecord;
import vip.xiaonuo.integral.modular.withdrawal.mapper.IntegralWithdrawalRecordMapper;
import vip.xiaonuo.integral.modular.withdrawal.params.IntegralWithdrawalRecordPageParam;
import vip.xiaonuo.integral.modular.withdrawal.service.IntegralWithdrawalRecordService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/28 14:04
 */
@Slf4j
@Component
public class IntegralWithdrawalRecordServiceImpl extends ServiceImpl<IntegralWithdrawalRecordMapper, IntegralWithdrawalRecord> implements IntegralWithdrawalRecordService {

    @Override
    public Page<IntegralWithdrawalRecord> page(IntegralWithdrawalRecordPageParam param) {
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
    public boolean addIntegralWithdrawalRecord(IntegralWithdrawalRecord record) {
        return this.save(record);
    }
}
