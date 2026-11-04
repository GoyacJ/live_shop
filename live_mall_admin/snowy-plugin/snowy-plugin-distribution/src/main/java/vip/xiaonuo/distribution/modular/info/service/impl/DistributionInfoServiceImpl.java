package vip.xiaonuo.distribution.modular.info.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.common.pojo.dto.CommonIdParam;
import vip.xiaonuo.common.pojo.dto.CommonPageParam;
import vip.xiaonuo.distribution.api.enums.DistributionType;
import vip.xiaonuo.distribution.modular.info.entity.DistributionInfo;
import vip.xiaonuo.distribution.modular.info.mapper.DistributionInfoMapper;
import vip.xiaonuo.distribution.modular.info.params.DistributionInfoAddParam;
import vip.xiaonuo.distribution.modular.info.params.DistributionInfoEditParam;
import vip.xiaonuo.distribution.modular.info.service.DistributionInfoService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/30 10:56
 */
@Slf4j
@Component
public class DistributionInfoServiceImpl extends ServiceImpl<DistributionInfoMapper, DistributionInfo> implements DistributionInfoService {


    @Override
    public Page<DistributionInfo> page(CommonPageParam param) {
        QueryWrapper queryWrapper = new QueryWrapper();

        if (ObjectUtil.isNotEmpty(param.startCreateTime()) && ObjectUtil.isNotEmpty(param.endCreateTime())) {
            queryWrapper.between(DistributionInfo::getCreateTime, param.startCreateTime(), param.endCreateTime());
        }

        // 关键词查询
        if (ObjectUtil.isNotEmpty(param.searchKey())) {
            // sql注入处理
            queryWrapper.and(wq -> {
                wq.where(wq1 -> {
                    wq1.or(DistributionInfo::getName).like(param.searchKey())
                            .or(DistributionInfo::getType).like(param.searchKey());

                });
            });
        }

        if (ObjectUtil.isAllNotEmpty(param.sortField(), param.sortOrder())) {
            CommonSortOrderEnum.validate(param.sortOrder());
            queryWrapper.orderBy(CharSequenceUtil.toUnderlineCase(param.sortField()), param.sortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(DistributionInfo::getCreateTime, false);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public DistributionInfo detail(String id) {
        return this.getById(id);
    }

    @Override
    public void add(DistributionInfoAddParam param) {
        this.save(param.toBean());
    }

    @Override
    public void edit(DistributionInfoEditParam param) {
        this.updateById(param.toBean());
    }

    @Override
    public void delete(CommonValidList<CommonIdParam> param) {
        this.removeByIds(param.getList().stream().map(CommonIdParam::id).toList());
    }

    @Override
    public void changeStatus(String id, SimpleStatus status) {
        UpdateChain.of(DistributionInfo.class)
                .eq(DistributionInfo::getId, id)
                .set(DistributionInfo::getStatus, status.getCode())
                .update();
    }

    @Override
    public DistributionInfo getDistributionInfoByType(DistributionType type) {
        return this.getOne(new QueryWrapper().eq(DistributionInfo::getType, type.getCode())
                .eq(DistributionInfo::getStatus, SimpleStatus.ENABLE.getCode()));
    }
}
