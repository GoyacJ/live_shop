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

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.common.annotation.CommonWrapper;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.manage.modular.userintegral.param.UserIntegralAddParam;
import vip.xiaonuo.manage.modular.userintegral.param.UserIntegralPageParam;
import vip.xiaonuo.manage.modular.userintegral.wrapper.UserIntegralWrapper;
import vip.xiaonuo.mobile.modular.manage.result.UserIntegralResult;
import vip.xiaonuo.mobile.modular.manage.service.UserIntegralService;
import java.util.List;

/**
 * 用户积分兑换控制器
 *
 */
@Api(tags = "用户积分兑换控制器")
@Controller
@Valid
public class UserIntegralController {

    @Inject
    private UserIntegralService userIntegralService;

    /**
     * 添加用户积分兑换
     *
     */
    @SaIgnore
    @ApiOperation("添加用户积分兑换")
    @Post
    @Mapping("/mapi/auth/integraladd")
    public CommonResult<String> add(UserIntegralAddParam userIntegralAddParam) {
        userIntegralService.add(userIntegralAddParam);
        return CommonResult.ok();
    }

    /**
     * 获取用户积分兑换分页
     *
     */
    @SaIgnore
    @ApiOperation("获取用户积分兑换分页")
    @Get
    @Mapping("/mapi/auth/getintegralpage")
    public CommonResult<Page<UserIntegralResult>> page(UserIntegralPageParam userIntegralPageParam) {
        return CommonResult.data(userIntegralService.page(userIntegralPageParam));
    }

}
