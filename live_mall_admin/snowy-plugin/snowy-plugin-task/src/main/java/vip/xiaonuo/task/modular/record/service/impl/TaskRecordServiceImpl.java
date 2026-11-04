package vip.xiaonuo.task.modular.record.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.common.pojo.dto.CommonPageParam;
import vip.xiaonuo.task.modular.info.entity.TaskInfo;
import vip.xiaonuo.task.modular.info.service.TaskInfoService;
import vip.xiaonuo.task.modular.record.entity.TaskRecord;
import vip.xiaonuo.task.modular.record.mapper.TaskRecordMapper;
import vip.xiaonuo.task.modular.record.service.TaskRecordService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/31 18:33
 */
@Slf4j
@Component
public class TaskRecordServiceImpl extends ServiceImpl<TaskRecordMapper, TaskRecord> implements TaskRecordService {

    @Inject
    private TaskInfoService taskInfoService;

    @Override
    public Page<TaskRecord> page(CommonPageParam param) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (ObjectUtil.isNotEmpty(param.startCreateTime()) && ObjectUtil.isNotEmpty(param.endCreateTime())) {
            queryWrapper.between(TaskRecord::getCreateTime, param.startCreateTime(), param.endCreateTime());
        }
        // 关键词查询
        if (ObjectUtil.isNotEmpty(param.searchKey())) {
            // sql注入处理
            queryWrapper.and(wq -> {
                wq.where(wq1 -> {
                    wq1.or(TaskRecord::getUserId).like(param.searchKey())
                            .or(TaskRecord::getTaskName).like(param.searchKey());

                });
            });
        }
        if (ObjectUtil.isAllNotEmpty(param.sortField(), param.sortOrder())) {
            CommonSortOrderEnum.validate(param.sortOrder());
            queryWrapper.orderBy(CharSequenceUtil.toUnderlineCase(param.sortField()), param.sortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(TaskRecord::getCreateTime, false);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public TaskRecord createRecord(TaskRecord record) {
        record.setId(IdUtil.getSnowflakeNextIdStr());
        this.save(record);
        return record;
    }

    @Override
    public TaskRecord createRecord(String userId, String taskInfoId) {
        TaskInfo detail = taskInfoService.detail(taskInfoId);
        return createRecord(userId, detail);
    }

    @Override
    public TaskRecord createRecord(String userId, TaskInfo taskInfo) {
        return createRecord(userId, taskInfo, SimpleStatus.ENABLE, null);
    }

    @Override
    public TaskRecord createRecord(String userId, TaskInfo taskInfo, SimpleStatus status, String errReason) {
        TaskRecord bean = BeanUtil.toBean(taskInfo, TaskRecord.class);
        bean.setUserId(userId);
        bean.setTaskId(taskInfo.getId());
        bean.setOperateTime(LocalDateTime.now());
        bean.setExecuteStatus(status);
        bean.setErrReason(errReason);
        return createRecord(bean);
    }

    @Override
    public TaskRecord detail(String id) {
        return this.getById(id);
    }

    @Override
    public List<TaskRecord> getSuccessTaskRecordByUser(String userId, String taskInfoId, LocalDateTime startTime, LocalDateTime endTime) {
        QueryWrapper queryWrapper = new QueryWrapper()
                .eq(TaskRecord::getExecuteStatus, SimpleStatus.ENABLE)
                .eq(TaskRecord::getUserId, userId)
                .eq(TaskRecord::getTaskId, taskInfoId);
        if (ObjectUtil.isNotEmpty(startTime) && ObjectUtil.isNotEmpty(endTime)) {
            queryWrapper.between(TaskRecord::getOperateTime, startTime, endTime);
        }
        return this.list(queryWrapper);
    }
}
