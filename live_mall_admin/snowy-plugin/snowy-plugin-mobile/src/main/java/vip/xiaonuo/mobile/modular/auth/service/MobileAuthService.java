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
package vip.xiaonuo.mobile.modular.auth.service;

import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.dto.CommonIdParam;
import vip.xiaonuo.mobile.modular.auth.param.MobileAuthLoginParam;
import vip.xiaonuo.mobile.modular.auth.param.MobileAuthPicCaptchaParam;
import vip.xiaonuo.mobile.modular.auth.param.MobileDeviceAccessParam;
import vip.xiaonuo.mobile.modular.auth.result.ChatContactResult;

/**
 * 移动端登录Service接口
 *
 */
public interface MobileAuthService {
    /**
     * 账号密码登录
     *
     **/
    CommonResult mobileLogin(MobileAuthLoginParam mobileAuthPasswordLoginParam);

    /**
     * 获取手机验证码
     *
     **/
    CommonResult<Void> getPhoneValidCode(MobileAuthPicCaptchaParam mobileAuthPicCaptchaParam);


    String getPhoneValidCodeTest();

    ChatContactResult getChatContact();

    boolean createGroup(String groupId, String groupName);

    /**
     * 移动端设备访问
     * @param param 参数
     * @return 结果
     */
    boolean deviceAccess(MobileDeviceAccessParam param);

    /**
     * 添加客服微信
     * @param userId 用户id
     */
    void addCustomerWechat(CommonIdParam userId);
}
