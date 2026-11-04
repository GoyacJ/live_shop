package vip.xiaonuo.task.modular.record.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.common.pojo.dto.CommonPageParam;
import vip.xiaonuo.task.modular.info.entity.TaskInfo;
import vip.xiaonuo.task.modular.record.entity.TaskRecord;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/31 18:33
 */
public interface TaskRecordService extends IService<TaskRecord> {

    /**
     * 分页查询
     *
     * @param param 参数
     * @return 结果
     */
    Page<TaskRecord> page(CommonPageParam param);

    TaskRecord createRecord(TaskRecord record);

    TaskRecord createRecord(String userId, String taskInfoId);

    TaskRecord createRecord(String userId, TaskInfo taskInfo);

    TaskRecord createRecord(String userId, TaskInfo taskInfo, SimpleStatus status, String errReason);

    /**
     * 详情
     *
     * @param id id
     * @return 记录
     */
    TaskRecord detail(String id);

    /**
     * 根据用户id和任务id查询任务记录
     *
     * @param userId     用户id
     * @param taskInfoId 任务id
     * @return 记录
     */
    default List<TaskRecord> getSuccessTaskRecordByUser(String userId, String taskInfoId) {
        return getSuccessTaskRecordByUser(userId, taskInfoId, null, null);
    }

    /**
     * 根据用户id、任务id、开始时间和结束时间查询任务记录
     *
     * @param userId     用户id
     * @param taskInfoId 任务id
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return 记录
     */
    List<TaskRecord> getSuccessTaskRecordByUser(String userId, String taskInfoId, LocalDateTime startTime, LocalDateTime endTime);
}
