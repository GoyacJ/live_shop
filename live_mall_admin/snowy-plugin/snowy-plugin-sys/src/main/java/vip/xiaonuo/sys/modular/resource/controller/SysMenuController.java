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
package vip.xiaonuo.sys.modular.resource.controller;

import cn.hutool.core.lang.tree.Tree;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.sys.modular.resource.entity.SysMenu;
import vip.xiaonuo.sys.modular.resource.entity.SysModule;
import vip.xiaonuo.sys.modular.resource.param.menu.*;
import vip.xiaonuo.sys.modular.resource.service.SysMenuService;

import org.noear.solon.validation.annotation.NotEmpty;
import java.util.List;

/**
 * 菜单控制器
 *
 * @author xuyuxiang
 * @date 2022/6/27 14:09
 **/
@Api(tags = "菜单控制器")
@Controller
@Valid
public class SysMenuController {

    @Inject
    private SysMenuService sysMenuService;

    /**
     * 获取菜单分页
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取菜单分页")
    @Get
    @Mapping("/sys/menu/page")
    public CommonResult<Page<SysMenu>> page(SysMenuPageParam sysMenuPageParam) {
        return CommonResult.data(sysMenuService.page(sysMenuPageParam));
    }

    /**
     * 获取菜单树
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取菜单树")
    @Get
    @Mapping("/sys/menu/tree")
    public CommonResult<List<Tree<String>>> tree(SysMenuTreeParam sysMenuTreeParam) {
        return CommonResult.data(sysMenuService.tree(sysMenuTreeParam));
    }

    /**
     * 添加菜单
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:47
     */
    @ApiOperation("添加菜单")
    @CommonLog("添加菜单")
    @Post
    @Mapping("/sys/menu/add")
    public CommonResult<String> add(@Validated SysMenuAddParam sysMenuAddParam) {
        sysMenuService.add(sysMenuAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑菜单
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:47
     */
    @ApiOperation("编辑菜单")
    @CommonLog("编辑菜单")
    @Post
    @Mapping("/sys/menu/edit")
    public CommonResult<String> edit(@Validated SysMenuEditParam sysMenuEditParam) {
        sysMenuService.edit(sysMenuEditParam);
        return CommonResult.ok();
    }

    /**
     * 更改菜单所属模块
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:47
     */
    @ApiOperation("更改菜单所属模块")
    @CommonLog("更改菜单所属模块")
    @Post
    @Mapping("/sys/menu/changeModule")
    public CommonResult<String> changeModule(@Validated SysMenuChangeModuleParam sysMenuChangeModuleParam) {
        sysMenuService.changeModule(sysMenuChangeModuleParam);
        return CommonResult.ok();
    }

    /**
     * 删除菜单
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("删除菜单")
    @CommonLog("删除菜单")
    @Post
    @Mapping("/sys/menu/delete")
    public CommonResult<String> delete(@Validated @NotEmpty(message = "集合不能为空")
                                                   CommonValidList<SysMenuIdParam> sysMenuIdParamList) {
        sysMenuService.delete(sysMenuIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取菜单详情
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取菜单详情")
    @Get
    @Mapping("/sys/menu/detail")
    public CommonResult<SysMenu> detail(@Validated SysMenuIdParam sysMenuIdParam) {
        return CommonResult.data(sysMenuService.detail(sysMenuIdParam));
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
    @Mapping("/sys/menu/moduleSelector")
    public CommonResult<List<SysModule>> moduleSelector(SysMenuSelectorModuleParam sysMenuSelectorModuleParam) {
        return CommonResult.data(sysMenuService.moduleSelector(sysMenuSelectorModuleParam));
    }

    /**
     * 获取菜单树选择器
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取菜单树选择器")
    @Get
    @Mapping("/sys/menu/menuTreeSelector")
    public CommonResult<List<Tree<String>>> menuTreeSelector(SysMenuSelectorMenuParam sysMenuSelectorMenuParam) {
        return CommonResult.data(sysMenuService.menuTreeSelector(sysMenuSelectorMenuParam));
    }
}
