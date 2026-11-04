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
package vip.xiaonuo.manage.modular.userbalancelog.controller;

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
import vip.xiaonuo.manage.modular.userbalancelog.entity.UserBalanceLog;
import vip.xiaonuo.manage.modular.userbalancelog.param.UserBalanceLogAddParam;
import vip.xiaonuo.manage.modular.userbalancelog.param.UserBalanceLogEditParam;
import vip.xiaonuo.manage.modular.userbalancelog.param.UserBalanceLogIdParam;
import vip.xiaonuo.manage.modular.userbalancelog.param.UserBalanceLogPageParam;
import vip.xiaonuo.manage.modular.userbalancelog.service.UserBalanceLogService;

/**
 * 平台-用户余额变更记录控制器
 *
 * @author yuezisheng
 * @date  2025/02/18 17:55
 */
@Api(tags = "平台-用户余额变更记录控制器")
@Controller
@Valid
public class UserBalanceLogController {

    @Inject
    private UserBalanceLogService userBalanceLogService;

    /**
     * 获取平台-用户余额变更记录分页
     *
     * @author yuezisheng
     * @date  2025/02/18 17:55
     */
    @ApiOperation("获取平台-用户余额变更记录分页")
    @SaCheckPermission("/manage/userbalancelog/page")
    @Get
    @Mapping("/manage/userbalancelog/page")
    public CommonResult<Page<UserBalanceLog>> page(UserBalanceLogPageParam userBalanceLogPageParam) {
        return CommonResult.data(userBalanceLogService.page(userBalanceLogPageParam));
    }

    /**
     * 添加平台-用户余额变更记录
     *
     * @author yuezisheng
     * @date  2025/02/18 17:55
     */
    @ApiOperation("添加平台-用户余额变更记录")
    @CommonLog("添加平台-用户余额变更记录")
    @SaCheckPermission("/manage/userbalancelog/add")
    @Post
    @Mapping("/manage/userbalancelog/add")
    public CommonResult<String> add(UserBalanceLogAddParam userBalanceLogAddParam) {
        userBalanceLogService.add(userBalanceLogAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑平台-用户余额变更记录
     *
     * @author yuezisheng
     * @date  2025/02/18 17:55
     */
    @ApiOperation("编辑平台-用户余额变更记录")
    @CommonLog("编辑平台-用户余额变更记录")
    @SaCheckPermission("/manage/userbalancelog/edit")
    @Post
    @Mapping("/manage/userbalancelog/edit")
    public CommonResult<String> edit(UserBalanceLogEditParam userBalanceLogEditParam) {
        userBalanceLogService.edit(userBalanceLogEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除平台-用户余额变更记录
     *
     * @author yuezisheng
     * @date  2025/02/18 17:55
     */
    @ApiOperation("删除平台-用户余额变更记录")
    @CommonLog("删除平台-用户余额变更记录")
    @SaCheckPermission("/manage/userbalancelog/delete")
    @Post
    @Mapping("/manage/userbalancelog/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<UserBalanceLogIdParam> userBalanceLogIdParamList) {
        userBalanceLogService.delete(userBalanceLogIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取平台-用户余额变更记录详情
     *
     * @author yuezisheng
     * @date  2025/02/18 17:55
     */
    @ApiOperation("获取平台-用户余额变更记录详情")
    @SaCheckPermission("/manage/userbalancelog/detail")
    @Get
    @Mapping("/manage/userbalancelog/detail")
    public CommonResult<UserBalanceLog> detail(UserBalanceLogIdParam userBalanceLogIdParam) {
        return CommonResult.data(userBalanceLogService.detail(userBalanceLogIdParam));
    }
}
