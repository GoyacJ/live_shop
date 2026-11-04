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
package vip.xiaonuo.dev.modular.job.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.pattern.CronPattern;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.java_cron.CronExpression;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.common.timer.CommonTimerTaskRunner;
import vip.xiaonuo.dev.modular.job.entity.DevJob;
import vip.xiaonuo.dev.modular.job.enums.DevJobCategoryEnum;
import vip.xiaonuo.dev.modular.job.enums.DevJobStatusEnum;
import vip.xiaonuo.dev.modular.job.mapper.DevJobMapper;
import vip.xiaonuo.dev.modular.job.param.*;
import vip.xiaonuo.dev.modular.job.service.DevJobService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 定时任务Service接口实现类
 *
 * @author xuyuxiang
 * @date 2022/8/5 10:47
 **/
@Component
public class DevJobServiceImpl extends ServiceImpl<DevJobMapper, DevJob> implements DevJobService {

    @Override
    public Page<DevJob> page(DevJobPageParam devJobPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        // 查询部分字段
        queryWrapper.select(DevJob::getId, DevJob::getName, DevJob::getCategory,
                DevJob::getActionClass, DevJob::getCronExpression, DevJob::getJobStatus, DevJob::getSortCode);
        if (ObjectUtil.isNotEmpty(devJobPageParam.getCategory())) {
            queryWrapper.eq(DevJob::getCategory, devJobPageParam.getCategory());
        }
        if (ObjectUtil.isNotEmpty(devJobPageParam.getSearchKey())) {
            queryWrapper.like(DevJob::getName, devJobPageParam.getSearchKey());
        }
        if (ObjectUtil.isNotEmpty(devJobPageParam.getJobStatus())) {
            queryWrapper.like(DevJob::getJobStatus, devJobPageParam.getJobStatus());
        }
        if (ObjectUtil.isAllNotEmpty(devJobPageParam.getSortField(), devJobPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(devJobPageParam.getSortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(devJobPageParam.getSortField()), devJobPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(DevJob::getSortCode);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public List<DevJob> list(DevJobListParam devJobListParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        // 查询部分字段
        queryWrapper.select(DevJob::getId, DevJob::getName, DevJob::getCategory,
                DevJob::getActionClass, DevJob::getCronExpression, DevJob::getJobStatus, DevJob::getSortCode);
        if (ObjectUtil.isNotEmpty(devJobListParam.getCategory())) {
            queryWrapper.eq(DevJob::getCategory, devJobListParam.getCategory());
        }
        if (ObjectUtil.isNotEmpty(devJobListParam.getSearchKey())) {
            queryWrapper.like(DevJob::getName, devJobListParam.getSearchKey());
        }
        if (ObjectUtil.isNotEmpty(devJobListParam.getJobStatus())) {
            queryWrapper.like(DevJob::getJobStatus, devJobListParam.getJobStatus());
        }
        return this.list(queryWrapper);
    }

    @Override
    public void add(DevJobAddParam devJobAddParam) {
        checkParam(devJobAddParam);
        DevJob devJob = BeanUtil.toBean(devJobAddParam, DevJob.class);
        devJob.setCode(RandomUtil.randomString(10));
        devJob.setJobStatus(DevJobStatusEnum.STOPPED.getValue());
        this.save(devJob);
    }

    private void checkParam(DevJobAddParam devJobAddParam) {
        DevJobCategoryEnum.validate(devJobAddParam.getCategory());
        try {
            CronPattern.of(devJobAddParam.getCronExpression());
        } catch (Exception e) {
            throw new CommonException("cron表达式：{}格式不正确", devJobAddParam.getCronExpression());
        }
        try {
            Class<?> actionClass = Class.forName(devJobAddParam.getActionClass());
            if (!CommonTimerTaskRunner.class.isAssignableFrom(actionClass)) {
                List<String> actionClassArr = StrUtil.split(devJobAddParam.getActionClass(), StrUtil.DOT);
                throw new CommonException("定时任务对应的类：{}不符合要求", actionClassArr.get(actionClassArr.size() - 1));
            }
        } catch (ClassNotFoundException e) {
            throw new CommonException("定时任务找不到对应的类，名称为：{}", devJobAddParam.getActionClass());
        }
        boolean hasSameJob = this.count(new QueryWrapper()
                .eq(DevJob::getActionClass, devJobAddParam.getActionClass())
                .eq(DevJob::getCronExpression, devJobAddParam.getCronExpression())) > 0;
        if (hasSameJob) {
            throw new CommonException("存在重复执行的定时任务，名称为：{}", devJobAddParam.getName());
        }
    }

    @Override
    public void edit(DevJobEditParam devJobEditParam) {
        DevJob devJob = this.queryEntity(devJobEditParam.getId());
        if (devJob.getJobStatus().equals(DevJobStatusEnum.RUNNING.getValue())) {
            throw new CommonException("运行中的定时任务不可编辑，id值为：{}", devJob.getId());
        }
        checkParam(devJobEditParam);
        BeanUtil.copyProperties(devJobEditParam, devJob);
        this.updateById(devJob);
    }

    private void checkParam(DevJobEditParam devJobEditParam) {
        DevJobCategoryEnum.validate(devJobEditParam.getCategory());
        if (!CronExpression.isValidExpression(devJobEditParam.getCronExpression())) {
            throw new CommonException("cron表达式：{}格式不正确", devJobEditParam.getCronExpression());
        }
        try {
            Class<?> actionClass = Class.forName(devJobEditParam.getActionClass());
            if (!CommonTimerTaskRunner.class.isAssignableFrom(actionClass)) {
                List<String> actionClassArr = StrUtil.split(devJobEditParam.getActionClass(), StrUtil.DOT);
                throw new CommonException("定时任务对应的类：{}不符合要求", actionClassArr.get(actionClassArr.size() - 1));
            }
        } catch (ClassNotFoundException e) {
            throw new CommonException("定时任务找不到对应的类，名称为：{}", devJobEditParam.getActionClass());
        }
        boolean hasSameJob = this.count(new QueryWrapper()
                .eq(DevJob::getActionClass, devJobEditParam.getActionClass())
                .eq(DevJob::getCronExpression, devJobEditParam.getCronExpression())
                .ne(DevJob::getId, devJobEditParam.getId())) > 0;
        if (hasSameJob) {
            throw new CommonException("存在重复的定时任务，名称为：{}", devJobEditParam.getName());
        }
    }

    @Tran
    @Override
    public void delete(List<DevJobIdParam> devJobIdParamList) {
        List<String> devJobIdList = CollStreamUtil.toList(devJobIdParamList, DevJobIdParam::getId);
        if (ObjectUtil.isNotEmpty(devJobIdList)) {
            // 将运行中的停止
            devJobIdList.forEach(CronUtil::remove);
            // 执行删除
            this.removeByIds(devJobIdList);
        }
    }

    @Override
    public DevJob detail(DevJobIdParam devJobIdParam) {
        return this.queryEntity(devJobIdParam.getId());
    }

    @Override
    public DevJob queryEntity(String id) {
        DevJob devJob = this.getById(id);
        if (ObjectUtil.isEmpty(devJob)) {
            throw new CommonException("定时任务不存在，id值为：{}", id);
        }
        return devJob;
    }

    @Tran
    @Override
    public void stopJob(DevJobIdParam devJobIdParam) {
        DevJob devJob = this.detail(devJobIdParam);
        if (devJob.getJobStatus().equals(DevJobStatusEnum.STOPPED.getValue())) {
            throw new CommonException("定时任务已经处于停止状态，id值为：{}", devJob.getId());
        }
        // 将运行中的定时任务停止
        CronUtil.remove(devJob.getId());
        this.updateChain().eq(DevJob::getId, devJobIdParam.getId())
                .set(DevJob::getJobStatus, DevJobStatusEnum.STOPPED.getValue()).update();
    }

    @Tran
    @Override
    public void runJob(DevJobIdParam devJobIdParam) {
        DevJob devJob = this.detail(devJobIdParam);
        if (devJob.getJobStatus().equals(DevJobStatusEnum.RUNNING.getValue())) {
            throw new CommonException("定时任务已经处于运行状态，id值为：{}", devJob.getId());
        }
        CronUtil.schedule(devJob.getId(), devJob.getCronExpression(), () -> {
            try {
                // 运行定时任务
                ((CommonTimerTaskRunner) Solon.context().getBean(Class.forName(devJob.getActionClass()))).action(devJob.getExtJson());
            } catch (ClassNotFoundException e) {
                throw new CommonException("定时任务找不到对应的类，名称为：{}", devJob.getActionClass());
            }
        });
        this.updateChain().eq(DevJob::getId, devJobIdParam.getId())
                .set(DevJob::getJobStatus, DevJobStatusEnum.RUNNING.getValue()).update();
    }

    @Override
    public void runJobNow(DevJobIdParam devJobIdParam) {
        DevJob devJob = this.detail(devJobIdParam);
        if (devJob.getJobStatus().equals(DevJobStatusEnum.STOPPED.getValue())) {
            // 如果是停止的，则先开启运行
            this.runJob(devJobIdParam);
        }
        try {
            // 直接运行一次
            ((CommonTimerTaskRunner) Solon.context().getBean(Class.forName(devJob.getActionClass()))).action(devJob.getExtJson());
        } catch (ClassNotFoundException e) {
            throw new CommonException("定时任务找不到对应的类，名称为：{}", devJob.getActionClass());
        }
    }

    @Override
    public List<String> getActionClass() {
        List<CommonTimerTaskRunner> commonTimerTaskRunnerMap = Solon.context().getBeansOfType(CommonTimerTaskRunner.class);
        if (ObjectUtil.isNotEmpty(commonTimerTaskRunnerMap)) {
            return commonTimerTaskRunnerMap.stream().map(commonTimerTaskRunner -> commonTimerTaskRunner.getClass().getName()).collect(Collectors.toList());
        } else {
            return CollectionUtil.newArrayList();
        }
    }
}
