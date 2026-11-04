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
package vip.xiaonuo.manage.modular.feedback.service;


import com.mybatisflex.core.paginate.Page;
import vip.xiaonuo.manage.modular.feedback.entity.BizFeedback;
import vip.xiaonuo.manage.modular.feedback.param.*;
import vip.xiaonuo.manage.modular.userinfo.entity.UserInfo;
import vip.xiaonuo.manage.modular.userinfo.param.UserInfoAddParam;
import vip.xiaonuo.manage.modular.userinfo.param.UserInfoEditParam;
import vip.xiaonuo.manage.modular.userinfo.param.UserInfoIdParam;
import vip.xiaonuo.manage.modular.userinfo.param.UserInfoPageParam;

import java.util.List;

/**
 * 反馈意见Service接口
 *
 */
public interface BizFeedBackService {

    /**
     * 获取反馈意见列表分页
     *
     */
    Page<BizFeedback> page(BizFeedbackPageParam bizFeedbackPageParam);

    /**
     * 编辑反馈意见
     *
     */
    void edit(BizFeedbackEditParam bizFeedbackEditParam);

    /**
     * 删除用户列表
     *
     */
    void delete(List<BizFeedbackIdParam> bizFeedbackIdParamList);

    /**
     * 获取用户列表详情
     *
     */
    BizFeedback detail(BizFeedbackIdParam BizFeedbackIdParam);

    /**
     * 添加反馈意见
     *
     */
    void add(BizFeedbackAddParam bizFeedbackAddParam);

    /**
     * 获取用户组详情
     *
     */
    List<BizFeedback> detailByMobile(BizFeedbackMobileParam bizFeedbackMobileParam);

}
