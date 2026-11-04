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
package vip.xiaonuo.manage.modular.messagelog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.solon.annotation.Component;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.manage.modular.feedback.entity.BizFeedback;
import vip.xiaonuo.manage.modular.feedback.mapper.BizFeedbackMapper;
import vip.xiaonuo.manage.modular.feedback.param.*;
import vip.xiaonuo.manage.modular.feedback.service.BizFeedBackService;
import vip.xiaonuo.manage.modular.messagelog.entity.LiveMessageLog;
import vip.xiaonuo.manage.modular.messagelog.mapper.MessageLogMapper;
import vip.xiaonuo.manage.modular.messagelog.param.*;
import vip.xiaonuo.manage.modular.messagelog.service.MessageLogService;

import java.util.List;

/**
 * 消息记录Service接口实现类
 *
 */
@Component
public class MessageLogServiceImpl extends ServiceImpl<MessageLogMapper, LiveMessageLog> implements MessageLogService {


    @Override
    public Page<LiveMessageLog> page(MessageLogPageParam messageLogPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderBy(LiveMessageLog::getCreateTime,false);
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public void edit(MessageLogEditParam messageLogEditParam) {
        LiveMessageLog liveMessageLog = this.getById(messageLogEditParam.getId());
        if(ObjectUtil.isEmpty(liveMessageLog)) {
            throw new CommonException("消息记录不存在，id值为：{}", messageLogEditParam.getId());
        }
        BeanUtil.copyProperties(messageLogEditParam, liveMessageLog);
        this.updateById(liveMessageLog);
    }

    @Override
    public void delete(List<MessageLogIdParam> messageLogIdParamList) {
        this.removeByIds(CollStreamUtil.toList(messageLogIdParamList, MessageLogIdParam::getId));
    }

    @Override
    public LiveMessageLog detail(MessageLogIdParam MessageLogIdParam) {
        LiveMessageLog liveMessageLog = this.getById(MessageLogIdParam.getId());
        if(ObjectUtil.isEmpty(liveMessageLog)) {
            throw new CommonException("消息记录不存在，id值为：{}", MessageLogIdParam.getId());
        }
        return liveMessageLog;
    }

    @Override
    public void add(MessageLogAddParam messageLogAddParam) {
        LiveMessageLog liveMessageLog = BeanUtil.toBean(messageLogAddParam, LiveMessageLog.class);
        this.save(liveMessageLog);
    }

    @Override
    public List<LiveMessageLog> getDetailByUser(MessageLogUserParam messageLogUserParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(messageLogUserParam.getCreateUser())) {
            queryWrapper.eq("create_user", messageLogUserParam.getCreateUser());
        }
        List<LiveMessageLog> list = this.list(queryWrapper);
        return list;
    }
}
