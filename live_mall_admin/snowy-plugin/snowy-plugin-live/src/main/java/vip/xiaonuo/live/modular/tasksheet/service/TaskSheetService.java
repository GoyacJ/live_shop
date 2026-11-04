/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.live.modular.tasksheet.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.live.modular.tasksheet.entity.TaskSheet;
import vip.xiaonuo.live.modular.tasksheet.param.TaskSheetAddParam;
import vip.xiaonuo.live.modular.tasksheet.param.TaskSheetEditParam;
import vip.xiaonuo.live.modular.tasksheet.param.TaskSheetIdParam;
import vip.xiaonuo.live.modular.tasksheet.param.TaskSheetPageParam;

import java.util.List;

/**
 * 任务表Service接口
 *
 * @author lgq
 * @date  2025/03/04 14:20
 **/
public interface TaskSheetService extends IService<TaskSheet> {

    /**
     * 获取任务表分页
     *
     * @author lgq
     * @date  2025/03/04 14:20
     */
    Page<TaskSheet> page(TaskSheetPageParam taskSheetPageParam);

    /**
     * 添加任务表
     *
     * @author lgq
     * @date  2025/03/04 14:20
     */
    void add(TaskSheetAddParam taskSheetAddParam);

    /**
     * 编辑任务表
     *
     * @author lgq
     * @date  2025/03/04 14:20
     */
    void edit(TaskSheetEditParam taskSheetEditParam);

    /**
     * 删除任务表
     *
     * @author lgq
     * @date  2025/03/04 14:20
     */
    void delete(List<TaskSheetIdParam> taskSheetIdParamList);

    /**
     * 获取任务表详情
     *
     * @author lgq
     * @date  2025/03/04 14:20
     */
    TaskSheet detail(TaskSheetIdParam taskSheetIdParam);

    /**
     * 获取任务表详情
     *
     * @author lgq
     * @date  2025/03/04 14:20
     **/
    TaskSheet queryEntity(String id);
}
