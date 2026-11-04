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
package vip.xiaonuo.sys.modular.org.controller;

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
import vip.xiaonuo.sys.modular.org.entity.SysOrg;
import vip.xiaonuo.sys.modular.org.param.*;
import vip.xiaonuo.sys.modular.org.service.SysOrgService;
import vip.xiaonuo.sys.modular.user.entity.SysUser;

import org.noear.solon.validation.annotation.NotEmpty;
import java.util.List;

/**
 * 组织控制器
 *
 * @author xuyuxiang
 * @date 2022/4/24 19:55
 */
@Api(tags = "组织控制器")
@Controller
@Valid
public class SysOrgController {

    @Inject
    private SysOrgService sysOrgService;

    /**
     * 获取组织分页
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取组织分页")
    @Get
    @Mapping("/sys/org/page")
    public CommonResult<Page<SysOrg>> page(SysOrgPageParam sysOrgPageParam) {
        return CommonResult.data(sysOrgService.page(sysOrgPageParam));
    }

    /**
     * 获取组织树
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取组织树")
    @Get@Mapping("/sys/org/tree")
    public CommonResult<List<Tree<String>>> tree() {
        return CommonResult.data(sysOrgService.tree());
    }

    /**
     * 添加组织
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:47
     */
    @ApiOperation("添加组织")
    @CommonLog("添加组织")
    @Post
    @Mapping("/sys/org/add")
    public CommonResult<String> add(@Validated SysOrgAddParam sysOrgAddParam) {
        sysOrgService.add(sysOrgAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑组织
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:47
     */
    @ApiOperation("编辑组织")
    @CommonLog("编辑组织")
    @Post@Mapping("/sys/org/edit")
    public CommonResult<String> edit(@Validated SysOrgEditParam sysOrgEditParam) {
        sysOrgService.edit(sysOrgEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除组织
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("删除组织")
    @CommonLog("删除组织")
    @Post
    @Mapping("/sys/org/delete")
    public CommonResult<String> delete(@Validated @NotEmpty(message = "集合不能为空")
                                                   CommonValidList<SysOrgIdParam> sysOrgIdParamList) {
        sysOrgService.delete(sysOrgIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取组织详情
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取组织详情")
    @Get
    @Mapping("/sys/org/detail")
    public CommonResult<SysOrg> detail(@Validated SysOrgIdParam sysOrgIdParam) {
        return CommonResult.data(sysOrgService.detail(sysOrgIdParam));
    }

    /* ====组织部分所需要用到的选择器==== */

    /**
     * 获取组织树选择器
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取组织树选择器")
    @Get
    @Mapping("/sys/org/orgTreeSelector")
    public CommonResult<List<Tree<String>>> orgTreeSelector() {
        return CommonResult.data(sysOrgService.orgTreeSelector());
    }

    /**
     * 获取用户选择器
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取用户选择器")
    @Get@Mapping("/sys/org/userSelector")
    public CommonResult<Page<SysUser>> userSelector(SysOrgSelectorUserParam sysOrgSelectorUserParam) {
        return CommonResult.data(sysOrgService.userSelector(sysOrgSelectorUserParam));
    }
}
