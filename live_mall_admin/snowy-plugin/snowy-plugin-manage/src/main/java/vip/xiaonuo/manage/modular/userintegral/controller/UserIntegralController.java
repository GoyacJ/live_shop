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
package vip.xiaonuo.manage.modular.userintegral.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.annotation.CommonWrapper;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.manage.modular.userintegral.entity.UserIntegral;
import vip.xiaonuo.manage.modular.userintegral.param.UserIntegralAddParam;
import vip.xiaonuo.manage.modular.userintegral.param.UserIntegralEditParam;
import vip.xiaonuo.manage.modular.userintegral.param.UserIntegralIdParam;
import vip.xiaonuo.manage.modular.userintegral.param.UserIntegralPageParam;
import vip.xiaonuo.manage.modular.userintegral.service.UserIntegralService;
import vip.xiaonuo.manage.modular.userintegral.wrapper.UserIntegralWrapper;

/**
 * 用户积分兑换控制器
 *
 * @author yuezisheng
 * @date  2025/02/18 11:07
 */
@Api(tags = "用户积分兑换控制器")
@Controller
@Valid
public class UserIntegralController {

    @Inject
    private UserIntegralService userIntegralService;

    /**
     * 获取用户积分兑换分页
     *
     * @author yuezisheng
     * @date  2025/02/18 11:07
     */
    @ApiOperation("获取用户积分兑换分页")
    @SaCheckPermission("/manage/userintegral/page")
    @Get
    @CommonWrapper(value = UserIntegralWrapper.class)
    @Mapping("/manage/userintegral/page")
    public CommonResult<Page<UserIntegral>> page(UserIntegralPageParam userIntegralPageParam) {
        return CommonResult.data(userIntegralService.page(userIntegralPageParam));
    }

    /**
     * 添加用户积分兑换
     *
     * @author yuezisheng
     * @date  2025/02/18 11:07
     */
    @ApiOperation("添加用户积分兑换")
    @CommonLog("添加用户积分兑换")
    @SaCheckPermission("/manage/userintegral/add")
    @Post
    @Mapping("/manage/userintegral/add")
    public CommonResult<String> add(UserIntegralAddParam userIntegralAddParam) {
        userIntegralService.add(userIntegralAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑用户积分兑换
     *
     * @author yuezisheng
     * @date  2025/02/18 11:07
     */
    @ApiOperation("编辑用户积分兑换")
    @CommonLog("编辑用户积分兑换")
    @SaCheckPermission("/manage/userintegral/edit")
    @Post
    @Mapping("/manage/userintegral/edit")
    public CommonResult<String> edit(UserIntegralEditParam userIntegralEditParam) {
        userIntegralService.edit(userIntegralEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除用户积分兑换
     *
     * @author yuezisheng
     * @date  2025/02/18 11:07
     */
    @ApiOperation("删除用户积分兑换")
    @CommonLog("删除用户积分兑换")
    @SaCheckPermission("/manage/userintegral/delete")
    @Post
    @Mapping("/manage/userintegral/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<UserIntegralIdParam> userIntegralIdParamList) {
        userIntegralService.delete(userIntegralIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取用户积分兑换详情
     *
     * @author yuezisheng
     * @date  2025/02/18 11:07
     */
    @ApiOperation("获取用户积分兑换详情")
    @Get
    @CommonWrapper(value = UserIntegralWrapper.class)
    @Mapping("/manage/userintegral/detail")
    public CommonResult<UserIntegral> detail(UserIntegralIdParam userIntegralIdParam) {
        return CommonResult.data(userIntegralService.detail(userIntegralIdParam));
    }
}
