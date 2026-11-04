package vip.xiaonuo.task.modular.info.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Component;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.task.api.enums.TaskBusinessCategory;
import vip.xiaonuo.task.api.enums.TaskCategory;
import vip.xiaonuo.task.modular.info.entity.TaskInfo;
import vip.xiaonuo.task.modular.info.mapper.TaskInfoMapper;
import vip.xiaonuo.task.modular.info.params.*;
import vip.xiaonuo.task.modular.info.service.TaskInfoService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/28 13:56
 */
@Slf4j
@Component
public class TaskInfoServiceImpl extends ServiceImpl<TaskInfoMapper, TaskInfo> implements TaskInfoService {

    @Override
    public Page<TaskInfo> page(TaskInfoPageParam param) {
        QueryWrapper queryWrapper = new QueryWrapper();

        if (StringUtils.isNotEmpty(param.taskName())) {
            queryWrapper.like(TaskInfo::getTaskName, param.taskName());
        }

        if (ObjectUtil.isNotEmpty(param.startCreateTime()) && ObjectUtil.isNotEmpty(param.endCreateTime())) {
            queryWrapper.between(TaskInfo::getCreateTime, param.startCreateTime(), param.endCreateTime());
        }

        // 关键词查询
        if (ObjectUtil.isNotEmpty(param.searchKey())) {
            // sql注入处理
            queryWrapper.and(wq -> {
                wq.where(wq1 -> {
                    wq1.or(TaskInfo::getTaskName).like(param.searchKey());
                });
            });
        }

        if (ObjectUtil.isAllNotEmpty(param.sortField(), param.sortOrder())) {
            CommonSortOrderEnum.validate(param.sortOrder());
            queryWrapper.orderBy(CharSequenceUtil.toUnderlineCase(param.sortField()), param.sortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(TaskInfo::getCreateTime, false);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);

    }

    @Override
    public TaskInfo detail(String id) {
        return this.getById(id);
    }

    @Override
    public void add(TaskInfoAddParam param) {
        this.save(param.toBean());
    }

    @Override
    public void edit(TaskInfoEditParam param) {
        TaskInfo bean = param.toBean();
        this.updateById(bean);

        // TODO: 暂时解决update null字段不更新的问题
        if (Objects.isNull(bean.getNumberParms())) {
            UpdateChain.of(TaskInfo.class)
                    .eq(TaskInfo::getId, bean.getId())
                    .set(TaskInfo::getNumberParms, null)
                    .update();
        }
    }

    @Override
    public void delete(CommonValidList<TaskInfoIdParam> param) {
        this.removeByIds(param.getList().stream().map(TaskInfoIdParam::id).toList());
    }

    @Override
    public void changeStatus(TaskInfoChangeStatusParam param) {
        UpdateChain.of(TaskInfo.class)
                .eq(TaskInfo::getId, param.id())
                .set(TaskInfo::getStatus, param.status())
                .update();
    }

    @Override
    public TaskInfo getIntegralTaskByCategory(TaskCategory category) {
        return this.getOne(new QueryWrapper().eq(TaskInfo::getTaskCategory, category.getCode())
                .eq(TaskInfo::getStatus, SimpleStatus.ENABLE.getCode()));
    }

    @Override
    public List<TaskInfo> getTaskInfoByBusinessCategory(TaskBusinessCategory taskBusinessCategory, String businessId) {
        QueryWrapper queryWrapper = new QueryWrapper().eq(TaskInfo::getTaskBusinessCategory, taskBusinessCategory.getCode())
                .eq(TaskInfo::getStatus, SimpleStatus.ENABLE.getCode());

        List<TaskCategory> taskCategories = Arrays.asList(TaskCategory.SHOP, TaskCategory.LIVE, TaskCategory.VIDEO);

        if (StringUtils.isNotEmpty(businessId)
                && taskCategories.contains(taskBusinessCategory.getTaskCategory())
        ) {
            queryWrapper.eq(TaskInfo::getJumpRouting, businessId);
        }

        if (TaskBusinessCategory.ADVERTISEMENT_VISIT.equals(taskBusinessCategory) &&
                StringUtils.isNotEmpty(businessId)
        ) {
            queryWrapper.eq(TaskInfo::getId, businessId);
        }
        return this.list(queryWrapper);
    }

    @Override
    public List<TaskInfo> getTaskInfoList() {
        return this.list(new QueryWrapper().eq(TaskInfo::getStatus, SimpleStatus.ENABLE.getCode())
                .orderBy(TaskInfo::getSort, true));
    }
}
