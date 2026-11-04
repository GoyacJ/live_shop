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
package vip.xiaonuo.live.modular.tasksheet.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.solon.annotation.Component;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.live.modular.tasksheet.entity.TaskSheet;
import vip.xiaonuo.live.modular.tasksheet.mapper.TaskSheetMapper;
import vip.xiaonuo.live.modular.tasksheet.param.TaskSheetAddParam;
import vip.xiaonuo.live.modular.tasksheet.param.TaskSheetEditParam;
import vip.xiaonuo.live.modular.tasksheet.param.TaskSheetIdParam;
import vip.xiaonuo.live.modular.tasksheet.param.TaskSheetPageParam;
import vip.xiaonuo.live.modular.tasksheet.service.TaskSheetService;

import java.util.List;

/**
 * 任务表Service接口实现类
 *
 * @author lgq
 * @date  2025/03/04 14:20
 **/
@Component
public class TaskSheetServiceImpl extends ServiceImpl<TaskSheetMapper, TaskSheet> implements TaskSheetService {

    @Override
    public Page<TaskSheet> page(TaskSheetPageParam taskSheetPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(taskSheetPageParam.getTaskId())) {
            queryWrapper.like(TaskSheet::getTaskId, taskSheetPageParam.getTaskId());
        }
        if(ObjectUtil.isAllNotEmpty(taskSheetPageParam.getSortField(), taskSheetPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(taskSheetPageParam.getSortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(taskSheetPageParam.getSortField()),taskSheetPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(TaskSheet::getId);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Tran
    @Override
    public void add(TaskSheetAddParam taskSheetAddParam) {
        TaskSheet taskSheet = BeanUtil.toBean(taskSheetAddParam, TaskSheet.class);
        this.save(taskSheet);
    }

    @Tran
    @Override
    public void edit(TaskSheetEditParam taskSheetEditParam) {
        TaskSheet taskSheet = this.queryEntity(taskSheetEditParam.getId());
        BeanUtil.copyProperties(taskSheetEditParam, taskSheet);
        this.updateById(taskSheet);
    }

    @Tran
    @Override
    public void delete(List<TaskSheetIdParam> taskSheetIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(taskSheetIdParamList, TaskSheetIdParam::getId));
    }

    @Override
    public TaskSheet detail(TaskSheetIdParam taskSheetIdParam) {
        return this.queryEntity(taskSheetIdParam.getId());
    }

    @Override
    public TaskSheet queryEntity(String id) {
        TaskSheet taskSheet = this.getById(id);
        if(ObjectUtil.isEmpty(taskSheet)) {
            throw new CommonException("任务表不存在，id值为：{}", id);
        }
        return taskSheet;
    }
}
