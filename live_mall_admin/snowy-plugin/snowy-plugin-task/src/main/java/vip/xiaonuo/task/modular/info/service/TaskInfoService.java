package vip.xiaonuo.task.modular.info.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.task.api.enums.TaskBusinessCategory;
import vip.xiaonuo.task.api.enums.TaskCategory;
import vip.xiaonuo.task.modular.info.entity.TaskInfo;
import vip.xiaonuo.task.modular.info.params.*;

import java.util.List;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/28 13:56
 */
public interface TaskInfoService extends IService<TaskInfo> {

    /**
     * 积分任务管理分页
     *
     * @param param 分页参数
     * @return 结果
     */
    Page<TaskInfo> page(TaskInfoPageParam param);

    /**
     * 获取详情
     *
     * @param id id
     * @return 结果
     */
    TaskInfo detail(String id);

    /**
     * 添加
     *
     * @param param 参数
     */
    void add(TaskInfoAddParam param);

    /**
     * 编辑
     *
     * @param param 参数
     */
    void edit(TaskInfoEditParam param);

    /**
     * 删除
     *
     * @param param 参数
     */
    void delete(CommonValidList<TaskInfoIdParam> param);

    /**
     * 修改状态
     *
     * @param param 参数
     */
    void changeStatus(TaskInfoChangeStatusParam param);

    /**
     * 根据积分任务分类获取积分任务配置
     *
     * @param category 积分任务分类
     * @return 结果
     */
    TaskInfo getIntegralTaskByCategory(TaskCategory category);

    /**
     * 根据业务分类获取任务
     *
     * @param taskBusinessCategory 业务分类
     * @return 结果
     */
    default List<TaskInfo> getTaskInfoByBusinessCategory(TaskBusinessCategory taskBusinessCategory) {
        return getTaskInfoByBusinessCategory(taskBusinessCategory, null);
    }

    /**
     * 根据业务分类获取任务
     *
     * @param taskBusinessCategory 业务分类
     * @param businessId           业务Id
     * @return 结果
     */
    List<TaskInfo> getTaskInfoByBusinessCategory(TaskBusinessCategory taskBusinessCategory, String businessId);

    /**
     * 获取任务列表
     *
     * @return 结果
     */
    List<TaskInfo> getTaskInfoList();
}
