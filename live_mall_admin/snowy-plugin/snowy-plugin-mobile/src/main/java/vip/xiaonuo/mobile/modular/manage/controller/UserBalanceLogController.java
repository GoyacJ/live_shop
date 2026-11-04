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
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.manage.modular.userbalancelog.param.UserBalanceLogAddParam;
import vip.xiaonuo.manage.modular.userbalancelog.param.UserBalanceLogPageParam;
import vip.xiaonuo.mobile.modular.manage.result.UserBalanceLogResult;
import vip.xiaonuo.mobile.modular.manage.service.UserBalanceLogService;

import java.util.List;

/**
 * 平台-用户余额变更记录控制器
 *
 */
@Api(tags = "平台-用户余额变更记录控制器")
@SaIgnore
@Controller
@Valid
public class UserBalanceLogController {

    @Inject
    private UserBalanceLogService userBalanceLogService;

    /**
     * 获取平台-用户余额变更记录分页
     *
     */
    @ApiOperation("获取平台-用户余额变更记录分页")
    @Get
    @Mapping("/mapi/auth/getUserCashLog")
    public CommonResult<Page<UserBalanceLogResult>> page(UserBalanceLogPageParam userBalanceLogPageParam) {
        return CommonResult.data(userBalanceLogService.page(userBalanceLogPageParam));
    }

    /**
     * 添加平台-用户余额变更记录
     *
     */
    @SaIgnore
    @ApiOperation("添加平台-用户余额变更记录")
    @Post
    @Mapping("/mapi/auth/balancelogadd")
    public CommonResult<String> add(UserBalanceLogAddParam userBalanceLogAddParam) {
        userBalanceLogService.add(userBalanceLogAddParam);
        return CommonResult.ok();
    }


}
