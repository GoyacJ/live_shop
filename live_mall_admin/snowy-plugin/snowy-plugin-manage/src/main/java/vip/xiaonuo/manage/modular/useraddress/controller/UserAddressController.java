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
package vip.xiaonuo.manage.modular.useraddress.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.manage.modular.useraddress.entity.UserAddress;
import vip.xiaonuo.manage.modular.useraddress.param.UserAddressAddParam;
import vip.xiaonuo.manage.modular.useraddress.param.UserAddressEditParam;
import vip.xiaonuo.manage.modular.useraddress.param.UserAddressIdParam;
import vip.xiaonuo.manage.modular.useraddress.param.UserAddressPageParam;
import vip.xiaonuo.manage.modular.useraddress.service.UserAddressService;

/**
 * 商城-用户收获地址控制器
 *
 * @author yuezisheng
 * @date  2025/02/18 21:28
 */
@Api(tags = "商城-用户收获地址控制器")
@Controller
@Valid
public class UserAddressController {

    @Inject
    private UserAddressService userAddressService;

    /**
     * 获取商城-用户收获地址分页
     *
     * @author yuezisheng
     * @date  2025/02/18 21:28
     */
    @ApiOperation("获取商城-用户收获地址分页")
    @SaCheckPermission("/manage/useraddress/page")
    @Get
    @Mapping("/manage/useraddress/page")
    public CommonResult<Page<UserAddress>> page(UserAddressPageParam userAddressPageParam) {
        return CommonResult.data(userAddressService.page(userAddressPageParam));
    }

    /**
     * 添加商城-用户收获地址
     *
     * @author yuezisheng
     * @date  2025/02/18 21:28
     */
    @ApiOperation("添加商城-用户收获地址")
    @CommonLog("添加商城-用户收获地址")
    @SaCheckPermission("/manage/useraddress/add")
    @Post
    @Mapping("/manage/useraddress/add")
    public CommonResult<String> add(UserAddressAddParam userAddressAddParam) {
        userAddressService.add(userAddressAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑商城-用户收获地址
     *
     * @author yuezisheng
     * @date  2025/02/18 21:28
     */
    @ApiOperation("编辑商城-用户收获地址")
    @CommonLog("编辑商城-用户收获地址")
    @SaCheckPermission("/manage/useraddress/edit")
    @Post
    @Mapping("/manage/useraddress/edit")
    public CommonResult<String> edit(UserAddressEditParam userAddressEditParam) {
        userAddressService.edit(userAddressEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除商城-用户收获地址
     *
     * @author yuezisheng
     * @date  2025/02/18 21:28
     */
    @ApiOperation("删除商城-用户收获地址")
    @CommonLog("删除商城-用户收获地址")
    @SaCheckPermission("/manage/useraddress/delete")
    @Post
    @Mapping("/manage/useraddress/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<UserAddressIdParam> userAddressIdParamList) {
        userAddressService.delete(userAddressIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取商城-用户收获地址详情
     *
     * @author yuezisheng
     * @date  2025/02/18 21:28
     */
    @ApiOperation("获取商城-用户收获地址详情")
    @SaCheckPermission("/manage/useraddress/detail")
    @Get
    @Mapping("/manage/useraddress/detail")
    public CommonResult<UserAddress> detail(UserAddressIdParam userAddressIdParam) {
        return CommonResult.data(userAddressService.detail(userAddressIdParam));
    }
}
