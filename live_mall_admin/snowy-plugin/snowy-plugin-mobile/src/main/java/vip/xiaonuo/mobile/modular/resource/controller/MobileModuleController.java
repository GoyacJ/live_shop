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
package vip.xiaonuo.mobile.modular.resource.controller;

import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.mobile.modular.resource.entity.MobileModule;
import vip.xiaonuo.mobile.modular.resource.param.module.MobileModuleAddParam;
import vip.xiaonuo.mobile.modular.resource.param.module.MobileModuleEditParam;
import vip.xiaonuo.mobile.modular.resource.param.module.MobileModuleIdParam;
import vip.xiaonuo.mobile.modular.resource.param.module.MobileModulePageParam;
import vip.xiaonuo.mobile.modular.resource.service.MobileModuleService;

/**
 * 移动端模块控制器
 *
 * @author xuyuxiang
 * @date 2022/6/27 14:12
 **/
@Api(tags = "移动端模块控制器")
@Controller
@Valid
public class MobileModuleController {

    @Inject
    private MobileModuleService mobileModuleService;

    /**
     * 获取移动端模块分页
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取移动端模块分页")
    @Get
    @Mapping("/mobile/module/page")
    public CommonResult<Page<MobileModule>> page(MobileModulePageParam mobileModulePageParam) {
        return CommonResult.data(mobileModuleService.page(mobileModulePageParam));
    }

    /**
     * 添加移动端模块
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:47
     */
    @ApiOperation("添加移动端模块")
    @CommonLog("添加移动端模块")
    @Post
    @Mapping("/mobile/module/add")
    public CommonResult<String> add(@Validated MobileModuleAddParam mobileModuleAddParam) {
        mobileModuleService.add(mobileModuleAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑移动端模块
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:47
     */
    @ApiOperation("编辑移动端模块")
    @CommonLog("编辑移动端模块")
    @Post
    @Mapping("/mobile/module/edit")
    public CommonResult<String> edit(@Validated MobileModuleEditParam mobileModuleEditParam) {
        mobileModuleService.edit(mobileModuleEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除移动端模块
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("删除移动端模块")
    @CommonLog("删除移动端模块")
    @Post
    @Mapping("/mobile/module/delete")
    public CommonResult<String> delete(@Validated @NotEmpty(message = "集合不能为空")
                                                   CommonValidList<MobileModuleIdParam> mobileModuleIdParamList) {
        mobileModuleService.delete(mobileModuleIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取移动端模块详情
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取移动端模块详情")
    @Get
    @Mapping("/mobile/module/detail")
    public CommonResult<MobileModule> detail(@Validated MobileModuleIdParam mobileModuleIdParam) {
        return CommonResult.data(mobileModuleService.detail(mobileModuleIdParam));
    }
}
