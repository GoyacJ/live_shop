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
package vip.xiaonuo.manage.modular.userinfo.provider;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.UploadedFile;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.dev.api.DevFileApi;
import vip.xiaonuo.manage.modular.userinfo.entity.UserInfo;
import vip.xiaonuo.manage.modular.userinfo.param.UserInfoAddParam;
import vip.xiaonuo.manage.modular.userinfo.param.UserInfoEditParam;
import vip.xiaonuo.manage.modular.userinfo.result.UserInfoResult;
import vip.xiaonuo.manage.modular.userinfo.service.UserInfoService;
import vip.xiaonuo.mobile.api.auth.api.MobileUserInfoApi;
import vip.xiaonuo.mobile.api.auth.pojo.MobileLoginUser;
import vip.xiaonuo.mobile.api.auth.pojo.MobileUserInfo;


/**
 **/
@Component("mobileUserInfoApi")
public class MobileUserInfoApiProvider implements MobileUserInfoApi {

    @Inject
    private UserInfoService userInfoService;

    @Override
    public CommonResult getUserInfoByMobile(String mobile) {
        UserInfoResult userInfoResult = userInfoService.getUserInfoByMobile(mobile);
        if (ObjectUtil.isNotEmpty(userInfoResult)) {
            return CommonResult.data(userInfoResult);
        }else {
            return null;
        }
    }

    @Override
    public CommonResult addUserInfo(JSONObject jSONObject) {
        UserInfoAddParam userInfoAddParam = new UserInfoAddParam();
        userInfoAddParam.setMobile((String) jSONObject.get("mobile"));
        userInfoAddParam.setNickname(jSONObject.get("nickname").toString());
        userInfoAddParam.setLevelId(jSONObject.get("levelId").toString());
        userInfoAddParam.setAvatar(jSONObject.get("avatar").toString());
        userInfoService.add(userInfoAddParam);
        return CommonResult.ok();
    }

    @Override
    public CommonResult editUserAvatar(String mobile,UploadedFile file) {
        String url = userInfoService.editUserAvatar(mobile,file);
        return CommonResult.data(url);
    }

    @Override
    public CommonResult editUserBasic(JSONObject jSONObject) {
        UserInfoEditParam userInfoEditParam = jSONObject.toBean(UserInfoEditParam.class);
        userInfoService.edit(userInfoEditParam);
        return CommonResult.ok();
    }
}
