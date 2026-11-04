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
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.solon.annotation.Component;
import vip.xiaonuo.manage.modular.feedback.entity.BizFeedback;
import vip.xiaonuo.manage.modular.feedback.mapper.BizFeedbackMapper;
import vip.xiaonuo.manage.modular.feedback.param.BizFeedbackMobileParam;
import vip.xiaonuo.mobile.modular.manage.result.FeedbackResult;
import vip.xiaonuo.mobile.modular.manage.service.FeedBackService;

import java.util.ArrayList;
import java.util.List;

import static vip.xiaonuo.common.util.CommonServletUtil.getRequest;

/**
 * 反馈意见Service接口实现类
 *
 */
@Component
public class FeedbackServiceImpl extends ServiceImpl<BizFeedbackMapper, BizFeedback> implements FeedBackService {

    @Override
    public void add(String content) {
        // BizFeedback bizFeedback = BeanUtil.toBean(bizFeedbackAddParam, BizFeedback.class);
        Object uid = getRequest().attr("uid");
        if (ObjectUtil.isEmpty(uid)) {
            throw new RuntimeException("用户id不能为空");
        }
        BizFeedback bizFeedback = new BizFeedback();
        bizFeedback.setFeedbackMessage(content);
        bizFeedback.setCreateUser(Convert.toStr(uid));
        this.save(bizFeedback);
    }

    @Override
    public List<FeedbackResult> feedbackList() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("create_user", Convert.toStr(getRequest().attrOrDefault("uid", '-')));
        queryWrapper.orderBy(BizFeedback::getCreateTime,false);
        List<BizFeedback> feedbackList = this.list(queryWrapper);
        List<FeedbackResult> list = new ArrayList<FeedbackResult>();
        if (feedbackList != null && !feedbackList.isEmpty()) {
            for (BizFeedback feedback : feedbackList) {
                FeedbackResult feedbackResult = new FeedbackResult();
                feedbackResult.setFeedbackType(feedback.getFeedbackType());
                feedbackResult.setFeedbackMessage(feedback.getFeedbackMessage());
                feedbackResult.setReplyMessage(feedback.getReplyMessage());
                feedbackResult.setCreateTime(feedback.getCreateTime());
                feedbackResult.setUpdateTime(feedback.getUpdateTime());
                list.add(feedbackResult);
            }
        }
        return list;
    }

    @Override
    public List<FeedbackResult> detailByMobile(BizFeedbackMobileParam bizFeedbackMobileParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(bizFeedbackMobileParam.getMobile())) {
            queryWrapper.eq("mobile", bizFeedbackMobileParam.getMobile()).orderBy(BizFeedback::getCreateTime,false);
        }
        List<BizFeedback> feedbackList = this.list(queryWrapper);
        List<FeedbackResult> list = new ArrayList<FeedbackResult>();
        if (feedbackList != null && feedbackList.size() > 0) {
            for (BizFeedback feedback : feedbackList) {
                FeedbackResult feedbackResult = new FeedbackResult();
                // feedbackResult.setId(feedback.getId());
                feedbackResult.setFeedbackType(feedback.getFeedbackType());
                feedbackResult.setFeedbackMessage(feedback.getFeedbackMessage());
                // feedbackResult.setStatus(feedback.getStatus());
                feedbackResult.setCreateTime(feedback.getCreateTime());
                list.add(feedbackResult);
            }
        }
        return list;
    }
}
