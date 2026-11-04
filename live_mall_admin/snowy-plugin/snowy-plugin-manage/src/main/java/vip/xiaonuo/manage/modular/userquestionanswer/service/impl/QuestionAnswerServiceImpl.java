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
package vip.xiaonuo.manage.modular.userquestionanswer.service.impl;

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
import vip.xiaonuo.manage.modular.userquestionanswer.entity.UserQuestionAnswer;
import vip.xiaonuo.manage.modular.userquestionanswer.mapper.QuestionAnswerMapper;
import vip.xiaonuo.manage.modular.userquestionanswer.param.QuestionAnswerAddParam;
import vip.xiaonuo.manage.modular.userquestionanswer.param.QuestionAnswerEditParam;
import vip.xiaonuo.manage.modular.userquestionanswer.param.QuestionAnswerIdParam;
import vip.xiaonuo.manage.modular.userquestionanswer.param.QuestionAnswerPageParam;
import vip.xiaonuo.manage.modular.userquestionanswer.service.QuestionAnswerService;

import java.util.List;

/**
 * 问题答案Service接口实现类
 *
 */
@Component
public class QuestionAnswerServiceImpl extends ServiceImpl<QuestionAnswerMapper, UserQuestionAnswer> implements QuestionAnswerService {

    @Override
    public Page<UserQuestionAnswer> page(QuestionAnswerPageParam questionAnswerPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(questionAnswerPageParam.getQuestionContent())) {
            queryWrapper.like(UserQuestionAnswer::getQuestionContent, questionAnswerPageParam.getQuestionContent());
        }
        if(ObjectUtil.isNotEmpty(questionAnswerPageParam.getStartCreateTime()) && ObjectUtil.isNotEmpty(questionAnswerPageParam.getEndCreateTime())) {
            queryWrapper.between(UserQuestionAnswer::getCreateTime, questionAnswerPageParam.getStartCreateTime(), questionAnswerPageParam.getEndCreateTime());
        }else {
            queryWrapper.orderBy(UserQuestionAnswer::getCreateTime, false);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public void edit(QuestionAnswerEditParam questionAnswerEditParam) {
        UserQuestionAnswer userQuestionAnswer = this.getById(questionAnswerEditParam.getId());
        if(ObjectUtil.isEmpty(userQuestionAnswer)) {
            throw new CommonException("问题答案不存在，id值为：{}", questionAnswerEditParam.getId());
        }
        BeanUtil.copyProperties(questionAnswerEditParam, userQuestionAnswer);
        this.updateById(userQuestionAnswer);
    }

    @Override
    public void delete(List<QuestionAnswerIdParam> QuestionAnswerIdParamList) {
        this.removeByIds(CollStreamUtil.toList(QuestionAnswerIdParamList, QuestionAnswerIdParam::getId));
    }

    @Override
    public UserQuestionAnswer detail(QuestionAnswerIdParam questionAnswerIdParam) {
        UserQuestionAnswer userQuestionAnswer = this.getById(questionAnswerIdParam.getId());
        if(ObjectUtil.isEmpty(userQuestionAnswer)) {
            throw new CommonException("问题答案不存在，id值为：{}", questionAnswerIdParam.getId());
        }
        return userQuestionAnswer;
    }

    @Override
    public void add(QuestionAnswerAddParam questionAnswerAddParam) {
        UserQuestionAnswer userQuestionAnswer = BeanUtil.toBean(questionAnswerAddParam, UserQuestionAnswer.class);
        this.save(userQuestionAnswer);
    }
}
