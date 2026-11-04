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
package vip.xiaonuo.manage.modular.userlevel.controller;

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
import vip.xiaonuo.manage.modular.userlevel.entity.UserLevel;
import vip.xiaonuo.manage.modular.userlevel.param.UserLevelAddParam;
import vip.xiaonuo.manage.modular.userlevel.param.UserLevelEditParam;
import vip.xiaonuo.manage.modular.userlevel.param.UserLevelIdParam;
import vip.xiaonuo.manage.modular.userlevel.param.UserLevelPageParam;
import vip.xiaonuo.manage.modular.userlevel.service.UserLevelService;

/**
 * 平台-用户等级控制器
 *
 * @author yuezisheng
 * @date  2025/02/18 09:16
 */
@Api(tags = "平台-用户等级控制器")
@Controller
@Valid
public class UserLevelController {

    @Inject
    private UserLevelService userLevelService;

    /**
     * 获取平台-用户等级分页
     *
     * @author yuezisheng
     * @date  2025/02/18 09:16
     */
    @ApiOperation("获取平台-用户等级分页")
    @SaCheckPermission("/manage/userlevel/page")
    @Get
    @Mapping("/manage/userlevel/page")
    public CommonResult<Page<UserLevel>> page(UserLevelPageParam userLevelPageParam) {
        return CommonResult.data(userLevelService.page(userLevelPageParam));
    }

    /**
     * 添加平台-用户等级
     *
     * @author yuezisheng
     * @date  2025/02/18 09:16
     */
    @ApiOperation("添加平台-用户等级")
    @CommonLog("添加平台-用户等级")
    @SaCheckPermission("/manage/userlevel/add")
    @Post
    @Mapping("/manage/userlevel/add")
    public CommonResult<String> add(UserLevelAddParam userLevelAddParam) {
        userLevelService.add(userLevelAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑平台-用户等级
     *
     * @author yuezisheng
     * @date  2025/02/18 09:16
     */
    @ApiOperation("编辑平台-用户等级")
    @CommonLog("编辑平台-用户等级")
    @SaCheckPermission("/manage/userlevel/edit")
    @Post
    @Mapping("/manage/userlevel/edit")
    public CommonResult<String> edit(UserLevelEditParam userLevelEditParam) {
        userLevelService.edit(userLevelEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除平台-用户等级
     *
     * @author yuezisheng
     * @date  2025/02/18 09:16
     */
    @ApiOperation("删除平台-用户等级")
    @CommonLog("删除平台-用户等级")
    @SaCheckPermission("/manage/userlevel/delete")
    @Post
    @Mapping("/manage/userlevel/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<UserLevelIdParam> userLevelIdParamList) {
        userLevelService.delete(userLevelIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取平台-用户等级详情
     *
     * @author yuezisheng
     * @date  2025/02/18 09:16
     */
    @ApiOperation("获取平台-用户等级详情")
    @SaCheckPermission("/manage/userlevel/detail")
    @Get
    @Mapping("/manage/userlevel/detail")
    public CommonResult<UserLevel> detail(UserLevelIdParam userLevelIdParam) {
        return CommonResult.data(userLevelService.detail(userLevelIdParam));
    }
}
