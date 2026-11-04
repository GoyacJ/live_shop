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
package vip.xiaonuo.mobile.modular.manage.controller;


import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.Context;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.manage.modular.useraddress.param.UserAddressAddParam;
import vip.xiaonuo.manage.modular.useraddress.param.UserAddressEditParam;
import vip.xiaonuo.mobile.modular.manage.result.UserAddressResult;
import vip.xiaonuo.mobile.modular.manage.service.UserAddressService;

import java.util.List;

/**
 * 用户地址控制器
 *
 */
@Api(tags = "反馈意见控制器")
@Controller
@Valid
@SaIgnore
public class UserAddressController {

    @Inject
    private UserAddressService userAddressService;

    /**
     * 添加用户收获地址
     *
     */
    @ApiOperation("添加商城-用户收获地址")
    @Post
    @Mapping("/mapi/auth/userAddressAdd")
    public CommonResult<UserAddressResult> add(UserAddressAddParam userAddressAddParam) {
        userAddressService.add(userAddressAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑用户收获地址
     *
     */
    @ApiOperation("编辑商城-用户收获地址")
    @Post
    @Mapping("/mapi/auth/userAddressEdit")
    public CommonResult<String> edit(UserAddressEditParam userAddressEditParam) {
        userAddressService.edit(userAddressEditParam);
        return CommonResult.ok();
    }

    /**
     * 获取用户收获地址List
     *
     */
    @ApiOperation("获取商城-用户收获地址详情")
    @Get
    @Mapping("/mapi/auth/getUserAddressList")
    public CommonResult<List<UserAddressResult>> getUserAddressList(Context context) {
        String uid = context.attrOrDefault("uid", "-");
        return CommonResult.data(userAddressService.getUserAddressList(uid));
    }

    @Post
    @Mapping("/mapi/auth/deleteUserAddress")
    public CommonResult<String> deleteUserAddress(Context context, String id) {
        if (StrUtil.isBlank(id)) {
            return CommonResult.error("id不能为空");
        }
        String uid = context.attrOrDefault("uid", "-");
        userAddressService.deleteUserAddress(uid, id);
        return CommonResult.ok();
    }

    @Post
    @Mapping("/mapi/auth/setDefaultUserAddress")
    public CommonResult<String> setDefaultUserAddress(Context context, String id) {
        if (StrUtil.isBlank(id)) {
            return CommonResult.error("id不能为空");
        }
        String uid = context.attrOrDefault("uid", "-");
        userAddressService.setDefaultUserAddress(uid, id);
        return CommonResult.ok();
    }
}
