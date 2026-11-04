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
package vip.xiaonuo.mobile.modular.manage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.Context;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.manage.modular.messagelog.entity.LiveMessageLog;
import vip.xiaonuo.manage.modular.messagelog.mapper.MessageLogMapper;
import vip.xiaonuo.manage.modular.messagelog.param.MessageLogAddParam;
import vip.xiaonuo.manage.modular.messagelog.param.MessageLogPageParam;
import vip.xiaonuo.mobile.modular.manage.result.MessageLogResult;
import vip.xiaonuo.mobile.modular.manage.service.MessageLogService;

import java.util.ArrayList;
import java.util.List;

import static vip.xiaonuo.common.util.CommonServletUtil.getRequest;

/**
 * 消息记录Service接口实现类
 *
 */
@Component
public class MessageLogServiceImpl extends ServiceImpl<MessageLogMapper, LiveMessageLog> implements MessageLogService {
    @Inject
    private CommonCacheOperator commonCacheOperator;

    @Override
    public Page<MessageLogResult> page(MessageLogPageParam messageLogPageParam) {

        Context context = getRequest();
        String uid = context.attrOrDefault("uid", "-");

        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(uid)) {
            queryWrapper.eq("create_user", uid).orderBy(LiveMessageLog::getCreateTime,false);
        }
        Page<LiveMessageLog> pageList =  this.page(CommonPageRequest.defaultPage(), queryWrapper);
        List<LiveMessageLog> list = this.list(queryWrapper);
        List<MessageLogResult> resultList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (LiveMessageLog liveMessageLog : list) {
                MessageLogResult messageLogResult = new MessageLogResult();
                BeanUtil.copyProperties(liveMessageLog, messageLogResult);
                resultList.add(messageLogResult);
            }
        }
        Page<MessageLogResult> pageListResult = new Page<>(pageList.getCurrent(),pageList.getPageSize(), pageList.getTotal());
        pageListResult.setRecords(resultList);
        return pageListResult;
        /*Page<MessageLogResult> pageListResult = new Page<>(resultList, pageList.getCurrent(), pageList.getPageSize(), pageList.getTotal());
        return pageListResult;*/
    }

    @Override
    public void add(MessageLogAddParam messageLogAddParam) {
        LiveMessageLog liveMessageLog = BeanUtil.toBean(messageLogAddParam, LiveMessageLog.class);
        this.save(liveMessageLog);
    }
}
