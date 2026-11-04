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
package vip.xiaonuo.biz.modular.user.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.lang.tree.Tree;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.noear.solon.annotation.*;
import org.noear.solon.boot.web.MimeType;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.UploadedFile;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.biz.modular.org.entity.BizOrg;
import vip.xiaonuo.biz.modular.position.entity.BizPosition;
import vip.xiaonuo.biz.modular.user.entity.BizUser;
import vip.xiaonuo.biz.modular.user.param.*;
import vip.xiaonuo.biz.modular.user.result.BizUserRoleResult;
import vip.xiaonuo.biz.modular.user.service.BizUserService;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;

import java.io.IOException;
import java.util.List;

/**
 * 人员控制器
 *
 * @author xuyuxiang
 * @date 2022/4/22 9:34
 **/
@Api(tags = "人员控制器")
@Controller
@Valid
public class BizUserController {

    @Inject
    private BizUserService bizUserService;

    /**
     * 获取人员分页
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取人员分页")
    @SaCheckPermission("/biz/user/page")
    @Get
    @Mapping("/biz/user/page")
    public CommonResult<Page<BizUser>> page(BizUserPageParam bizUserPageParam) {
        return CommonResult.data(bizUserService.page(bizUserPageParam));
    }

    /**
     * 添加人员
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:47
     */
    @ApiOperation("添加人员")
    @CommonLog("添加人员")
    @SaCheckPermission("/biz/user/add")
    @Post
    @Mapping("/biz/user/add")
    public CommonResult<String> add(@Validated BizUserAddParam bizUserAddParam) {
        bizUserService.add(bizUserAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑人员
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:47
     */
    @ApiOperation("编辑人员")
    @CommonLog("编辑人员")
    @SaCheckPermission("/biz/user/edit")
    @Post
    @Mapping("/biz/user/edit")
    public CommonResult<String> edit(@Validated BizUserEditParam bizUserEditParam) {
        bizUserService.edit(bizUserEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除人员
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("删除人员")
    @CommonLog("删除人员")
    @SaCheckPermission("/biz/user/delete")
    @Post
    @Mapping("/biz/user/delete")
    public CommonResult<String> delete( @Validated @NotEmpty(message = "集合不能为空")
                                                   CommonValidList<BizUserIdParam> bizUserIdParamList) {
        bizUserService.delete(bizUserIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取人员详情
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取人员详情")
    @SaCheckPermission("/biz/user/detail")
    @Get
    @Mapping("/biz/user/detail")
    public CommonResult<BizUser> detail(@Validated BizUserIdParam bizUserIdParam) {
        return CommonResult.data(bizUserService.detail(bizUserIdParam));
    }

    /**
     * 禁用人员
     *
     * @author xuyuxiang
     * @date 2021/10/13 14:01
     **/
    @ApiOperation("禁用人员")
    @CommonLog("禁用人员")
    @SaCheckPermission("/biz/user/disableUser")
    @Post
    @Mapping("/biz/user/disableUser")
    public CommonResult<String> disableUser(BizUserIdParam bizUserIdParam) {
        bizUserService.disableUser(bizUserIdParam);
        return CommonResult.ok();
    }

    /**
     * 启用人员
     *
     * @author xuyuxiang
     * @date 2021/10/13 14:01
     **/
    @ApiOperation("启用人员")
    @CommonLog("启用人员")
    @SaCheckPermission("/biz/user/enableUser")
    @Post
    @Mapping("/biz/user/enableUser")
    public CommonResult<String> enableUser(@Validated BizUserIdParam bizUserIdParam) {
        bizUserService.enableUser(bizUserIdParam);
        return CommonResult.ok();
    }

    /**
     * 重置人员密码
     *
     * @author xuyuxiang
     * @date 2021/10/13 14:01
     **/
    @ApiOperation("重置人员密码")
    @CommonLog("重置人员密码")
    @SaCheckPermission("/biz/user/resetPassword")
    @Post
    @Mapping("/biz/user/resetPassword")
    public CommonResult<String> resetPassword(@Validated BizUserIdParam bizUserIdParam) {
        bizUserService.resetPassword(bizUserIdParam);
        return CommonResult.ok();
    }

    /**
     * 人员拥有角色
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取人员拥有角色")
    @SaCheckPermission("/biz/user/ownRole")
    @Get
    @Mapping("/biz/user/ownRole")
    public CommonResult<List<String>> ownRole(@Validated BizUserIdParam bizUserIdParam) {
        return CommonResult.data(bizUserService.ownRole(bizUserIdParam));
    }

    /**
     * 给人员授权角色
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("给人员授权角色")
    @CommonLog("给人员授权角色")
    @SaCheckPermission("/biz/user/grantRole")
    @Post
    @Mapping("/biz/user/grantRole")
    public CommonResult<String> grantRole(@Validated BizUserGrantRoleParam bizUserGrantRoleParam) {
        bizUserService.grantRole(bizUserGrantRoleParam);
        return CommonResult.ok();
    }

    /**
     * 人员导入
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("人员导入")
    @CommonLog("人员导入")
    @SaCheckPermission("/biz/user/import")
    @Post
    @Mapping("/biz/user/import")
    public CommonResult<String> importUser(@ApiParam(value="文件", required = true) UploadedFile file) {
        bizUserService.importUser(file);
        return CommonResult.ok();
    }

    /**
     * 人员导出
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("人员导出")
    @CommonLog("人员导出")
    @SaCheckPermission("/biz/user/export")
    @Get
    @Produces(MimeType.APPLICATION_OCTET_STREAM_VALUE)
    @Mapping("/biz/user/export")
    public void exportUser(BizUserExportParam bizUserExportParam, Context ctx) throws IOException {
        bizUserService.exportUser(bizUserExportParam, ctx);
    }

    /**
     * 按模板导出人员个人信息
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("导出人员个人信息")
    @CommonLog("导出人员个人信息")
    @SaCheckPermission("/biz/user/exportUserInfo")
    @Get
    @Mapping(value = "/biz/user/exportUserInfo")
    public void exportUserInfo(BizUserIdParam bizUserIdParam, Context response) throws IOException {
        bizUserService.exportUserInfo(bizUserIdParam, response);
    }

    /* ====人员部分所需要用到的选择器==== */

    /**
     * 获取机构树选择器
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取机构树选择器")
    @SaCheckPermission("/biz/user/orgTreeSelector")
    @Get
    @Mapping("/biz/user/orgTreeSelector")
    public CommonResult<List<Tree<String>>> orgTreeSelector() {
        return CommonResult.data(bizUserService.orgTreeSelector());
    }

    /**
     * 获取机构列表选择器
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取机构列表选择器")
    @SaCheckPermission("/biz/user/orgListSelector")
    @Get
    @Mapping("/biz/user/orgListSelector")
    public CommonResult<Page<BizOrg>> orgListSelector(BizUserSelectorOrgListParam bizUserSelectorOrgListParam) {
        return CommonResult.data(bizUserService.orgListSelector(bizUserSelectorOrgListParam));
    }

    /**
     * 获取岗位选择器
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取岗位选择器")
    @SaCheckPermission("/biz/user/positionSelector")
    @Get
    @Mapping("/biz/user/positionSelector")
    public CommonResult<Page<BizPosition>> positionSelector(BizUserSelectorPositionParam bizUserSelectorPositionParam) {
        return CommonResult.data(bizUserService.positionSelector(bizUserSelectorPositionParam));
    }

    /**
     * 获取角色选择器
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取角色选择器")
    @SaCheckPermission("/biz/user/roleSelector")
    @Get
    @Mapping("/biz/user/roleSelector")
    public CommonResult<Page<BizUserRoleResult>> roleSelector(BizUserSelectorRoleParam bizUserSelectorRoleParam) {
        return CommonResult.data(bizUserService.roleSelector(bizUserSelectorRoleParam));
    }

    /**
     * 获取人员选择器
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取人员选择器")
    @SaCheckPermission("/biz/user/userSelector")
    @Get
    @Mapping("/biz/user/userSelector")
    public CommonResult<Page<BizUser>> userSelector(BizUserSelectorUserParam bizUserSelectorUserParam) {
        return CommonResult.data(bizUserService.userSelector(bizUserSelectorUserParam));
    }
}
