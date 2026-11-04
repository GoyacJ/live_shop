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
package vip.xiaonuo.manage.modular.userwithdrawallog.controller;

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
import vip.xiaonuo.manage.modular.userwithdrawallog.entity.UserWithdrawalLog;
import vip.xiaonuo.manage.modular.userwithdrawallog.param.UserWithdrawalLogAddParam;
import vip.xiaonuo.manage.modular.userwithdrawallog.param.UserWithdrawalLogEditParam;
import vip.xiaonuo.manage.modular.userwithdrawallog.param.UserWithdrawalLogIdParam;
import vip.xiaonuo.manage.modular.userwithdrawallog.param.UserWithdrawalLogPageParam;
import vip.xiaonuo.manage.modular.userwithdrawallog.service.UserWithdrawalLogService;

/**
 * 用户提现记录控制器
 *
 * @author yuezisheng
 * @date  2025/02/18 20:31
 */
@Api(tags = "用户提现记录控制器")
@Controller
@Valid
public class UserWithdrawalLogController {

    @Inject
    private UserWithdrawalLogService userWithdrawalLogService;

    /**
     * 获取用户提现记录分页
     *
     * @author yuezisheng
     * @date  2025/02/18 20:31
     */
    @ApiOperation("获取用户提现记录分页")
    @SaCheckPermission("/manage/userwithdrawallog/page")
    @Get
    @Mapping("/manage/userwithdrawallog/page")
    public CommonResult<Page<UserWithdrawalLog>> page(UserWithdrawalLogPageParam userWithdrawalLogPageParam) {
        return CommonResult.data(userWithdrawalLogService.page(userWithdrawalLogPageParam));
    }

    /**
     * 添加用户提现记录
     *
     * @author yuezisheng
     * @date  2025/02/18 20:31
     */
    @ApiOperation("添加用户提现记录")
    @CommonLog("添加用户提现记录")
    @SaCheckPermission("/manage/userwithdrawallog/add")
    @Post
    @Mapping("/manage/userwithdrawallog/add")
    public CommonResult<String> add(UserWithdrawalLogAddParam userWithdrawalLogAddParam) {
        userWithdrawalLogService.add(userWithdrawalLogAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑用户提现记录
     *
     * @author yuezisheng
     * @date  2025/02/18 20:31
     */
    @ApiOperation("编辑用户提现记录")
    @CommonLog("编辑用户提现记录")
    @SaCheckPermission("/manage/userwithdrawallog/edit")
    @Post
    @Mapping("/manage/userwithdrawallog/edit")
    public CommonResult<String> edit(UserWithdrawalLogEditParam userWithdrawalLogEditParam) {
        userWithdrawalLogService.edit(userWithdrawalLogEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除用户提现记录
     *
     * @author yuezisheng
     * @date  2025/02/18 20:31
     */
    @ApiOperation("删除用户提现记录")
    @CommonLog("删除用户提现记录")
    @SaCheckPermission("/manage/userwithdrawallog/delete")
    @Post
    @Mapping("/manage/userwithdrawallog/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<UserWithdrawalLogIdParam> userWithdrawalLogIdParamList) {
        userWithdrawalLogService.delete(userWithdrawalLogIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取用户提现记录详情
     *
     * @author yuezisheng
     * @date  2025/02/18 20:31
     */
    @ApiOperation("获取用户提现记录详情")
    @SaCheckPermission("/manage/userwithdrawallog/detail")
    @Get
    @Mapping("/manage/userwithdrawallog/detail")
    public CommonResult<UserWithdrawalLog> detail(UserWithdrawalLogIdParam userWithdrawalLogIdParam) {
        return CommonResult.data(userWithdrawalLogService.detail(userWithdrawalLogIdParam));
    }
}
