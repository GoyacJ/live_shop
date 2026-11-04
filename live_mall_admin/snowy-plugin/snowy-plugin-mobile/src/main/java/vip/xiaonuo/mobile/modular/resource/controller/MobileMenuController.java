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

import cn.hutool.core.lang.tree.Tree;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.mobile.modular.resource.entity.MobileMenu;
import vip.xiaonuo.mobile.modular.resource.entity.MobileModule;
import vip.xiaonuo.mobile.modular.resource.param.menu.*;
import vip.xiaonuo.mobile.modular.resource.service.MobileMenuService;

import java.util.List;

/**
 * 移动端菜单控制器
 *
 * @author yubaoshan
 * @date  2023/01/28 22:42
 */
@Api(tags = "移动端菜单控制器")
@Controller
@Valid
public class MobileMenuController {

    @Inject
    private MobileMenuService mobileMenuService;

    /**
     * 获取移动端菜单tree
     *
     * @author yubaoshan
     * @date  2023/01/28 22:42
     */
    @ApiOperation("获取移动端菜单tree")
    @Get
    @Mapping("/mobile/menu/tree")
    public CommonResult<List<Tree<String>>> tree(MobileMenuTreeParam mobileMenuTreeParam) {
        return CommonResult.data(mobileMenuService.tree(mobileMenuTreeParam));
    }

    /**
     * 添加移动端菜单
     *
     * @author yubaoshan
     * @date  2023/01/28 22:42
     */
    @ApiOperation("添加移动端菜单")
    @CommonLog("添加移动端菜单")
    @Post
    @Mapping("/mobile/menu/add")
    public CommonResult<String> add(@Validated MobileMenuAddParam mobileMenuAddParam) {
        mobileMenuService.add(mobileMenuAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑移动端菜单
     *
     * @author yubaoshan
     * @date  2023/01/28 22:42
     */
    @ApiOperation("编辑移动端菜单")
    @CommonLog("编辑移动端菜单")
    @Post
    @Mapping("/mobile/menu/edit")
    public CommonResult<String> edit(@Validated MobileMenuEditParam mobileMenuEditParam) {
        mobileMenuService.edit(mobileMenuEditParam);
        return CommonResult.ok();
    }

    /**
     * 更改移动端菜单所属模块
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:47
     */
    @ApiOperation("更改移动端菜单所属模块")
    @CommonLog("更改移动端菜单所属模块")
    @Post
    @Mapping("/mobile/menu/changeModule")
    public CommonResult<String> changeModule(@Validated MobileMenuChangeModuleParam mobileMenuChangeModuleParam) {
        mobileMenuService.changeModule(mobileMenuChangeModuleParam);
        return CommonResult.ok();
    }

    /**
     * 删除移动端菜单
     *
     * @author yubaoshan
     * @date  2023/01/28 22:42
     */
    @ApiOperation("删除移动端菜单")
    @CommonLog("删除移动端菜单")
    @Post
    @Mapping("/mobile/menu/delete")
    public CommonResult<String> delete(@Validated @NotEmpty(message = "集合不能为空")
                                                   CommonValidList<MobileMenuIdParam> mobileMenuIdParamList) {
        mobileMenuService.delete(mobileMenuIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取移动端菜单详情
     *
     * @author yubaoshan
     * @date  2023/01/28 22:42
     */
    @ApiOperation("获取移动端菜单详情")
    @Get
    @Mapping("/mobile/menu/detail")
    public CommonResult<MobileMenu> detail(@Validated MobileMenuIdParam mobileMenuIdParam) {
        return CommonResult.data(mobileMenuService.detail(mobileMenuIdParam));
    }

    /* ====菜单部分所需要用到的选择器==== */

    /**
     * 获取模块选择器
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取模块选择器")
    @Get
    @Mapping("/mobile/menu/moduleSelector")
    public CommonResult<List<MobileModule>> moduleSelector(MobileMenuSelectorModuleParam mobileMenuSelectorModuleParam) {
        return CommonResult.data(mobileMenuService.moduleSelector(mobileMenuSelectorModuleParam));
    }

    /**
     * 获取菜单树选择器
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取菜单树选择器")
    @Get
    @Mapping("/mobile/menu/menuTreeSelector")
    public CommonResult<List<Tree<String>>> menuTreeSelector(MobileMenuSelectorMenuParam mobileMenuSelectorMenuParam) {
        return CommonResult.data(mobileMenuService.menuTreeSelector(mobileMenuSelectorMenuParam));
    }
}
