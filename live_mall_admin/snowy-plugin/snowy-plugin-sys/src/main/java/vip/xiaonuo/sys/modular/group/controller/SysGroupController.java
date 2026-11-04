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
package vip.xiaonuo.sys.modular.group.controller;

import cn.hutool.core.lang.tree.Tree;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.sys.modular.group.entity.SysGroup;
import vip.xiaonuo.sys.modular.group.param.*;
import vip.xiaonuo.sys.modular.group.service.SysGroupService;
import vip.xiaonuo.sys.modular.user.entity.SysUser;

import java.util.List;

/**
 * 用户组控制器
 *
 * @author chengchuanyao
 * @date 2025/1/6 11:30
 */
@Api(tags = "用户组控制器")
@Controller
@Valid
public class SysGroupController {
    
    @Inject
    private SysGroupService sysGroupService;

    /**
     * 获取用户组分页
     *
     * @author yubaoshan
     * @date  2024/12/21 01:25
     */
    @ApiOperation("获取用户组分页")
    @Get
    @Mapping("/sys/group/page")
    public CommonResult<Page<SysGroup>> page(SysGroupPageParam sysGroupPageParam) {
        return CommonResult.data(sysGroupService.page(sysGroupPageParam));
    }

    /**
     * 添加用户组
     *
     * @author yubaoshan
     * @date  2024/12/21 01:25
     */
    @ApiOperation("添加用户组")
    @CommonLog("添加用户组")
    @Post
    @Mapping("/sys/group/add")
    public CommonResult<String> add(@Body @Validated SysGroupAddParam sysGroupAddParam) {
        sysGroupService.add(sysGroupAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑用户组
     *
     * @author yubaoshan
     * @date  2024/12/21 01:25
     */
    @ApiOperation("编辑用户组")
    @CommonLog("编辑用户组")
    @Post
    @Mapping("/sys/group/edit")
    public CommonResult<String> edit(@Body @Validated SysGroupEditParam sysGroupEditParam) {
        sysGroupService.edit(sysGroupEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除用户组
     *
     * @author yubaoshan
     * @date  2024/12/21 01:25
     */
    @ApiOperation("删除用户组")
    @CommonLog("删除用户组")
    @Post
    @Mapping("/sys/group/delete")
    public CommonResult<String> delete(@Body @Validated @NotEmpty(message = "集合不能为空")
                                       List<SysGroupIdParam> sysGroupIdParamList) {
        sysGroupService.delete(sysGroupIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取用户组详情
     *
     * @author yubaoshan
     * @date  2024/12/21 01:25
     */
    @ApiOperation("获取用户组详情")
    @Get
    @Mapping("/sys/group/detail")
    public CommonResult<SysGroup> detail(@Validated SysGroupIdParam sysGroupIdParam) {
        return CommonResult.data(sysGroupService.detail(sysGroupIdParam));
    }

    /**
     * 获取用户组下的用户
     *
     * @author yubaoshan
     * @date 2024/12/21 01:25
     */
    @ApiOperation("获取用户组下的用户")
    @Get
    @Mapping("/sys/group/ownUser")
    public CommonResult<List<String>> ownUser(@Validated SysGroupIdParam sysGroupIdParam) {
        return CommonResult.data(sysGroupService.ownUser(sysGroupIdParam));
    }

    /**
     * 获取组织树选择器
     *
     * @author yubaoshan
     * @date 2024/12/21 01:25
     */
    @ApiOperation("获取组织树选择器")
    @Get
    @Mapping("/sys/group/orgTreeSelector")
    public CommonResult<List<Tree<String>>> orgTreeSelector() {
        return CommonResult.data(sysGroupService.orgTreeSelector());
    }

    /**
     * 获取用户选择器
     *
     * @author yubaoshan
     * @date 2024/12/21 01:25
     */
    @ApiOperation("获取用户选择器")
    @Get
    @Mapping("/sys/group/userSelector")
    public CommonResult<Page<SysUser>> userSelector(SysGroupSelectorUserParam sysGroupSelectorUserParam) {
        return CommonResult.data(sysGroupService.userSelector(sysGroupSelectorUserParam));
    }

    /**
     * 给用户组授权用户
     *
     * @author yubaoshan
     * @date 2024/12/21 01:25
     */
    @ApiOperation("给用户组授权用户")
    @CommonLog("给用户组授权用户")
    @Post
    @Mapping("/sys/group/grantUser")
    public CommonResult<String> grantUser(@Body @Validated SysGroupGrantUserParam sysGroupGrantUserParam) {
        sysGroupService.grantUser(sysGroupGrantUserParam);
        return CommonResult.ok();
    }
}
