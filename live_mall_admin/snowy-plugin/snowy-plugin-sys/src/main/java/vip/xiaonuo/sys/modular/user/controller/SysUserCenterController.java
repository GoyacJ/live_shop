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
package vip.xiaonuo.sys.modular.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.tree.Tree;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.UploadedFile;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.sys.modular.org.entity.SysOrg;
import vip.xiaonuo.sys.modular.position.entity.SysPosition;
import vip.xiaonuo.sys.modular.role.entity.SysRole;
import vip.xiaonuo.sys.modular.user.entity.SysUser;
import vip.xiaonuo.sys.modular.user.param.*;
import vip.xiaonuo.sys.modular.user.result.SysUserMessageDetailResult;
import vip.xiaonuo.sys.modular.user.result.SysUserMessageResult;
import vip.xiaonuo.sys.modular.user.result.SysUserPicValidCodeResult;
import vip.xiaonuo.sys.modular.user.result.SysUserPositionResult;
import vip.xiaonuo.sys.modular.user.service.SysUserService;

import java.util.List;

/**
 * 用户个人控制器
 *
 * @author xuyuxiang
 * @date 2022/4/22 9:34
 **/
@Api(tags = "用户个人控制器")
@Controller
@Valid
public class SysUserCenterController {

    @Inject
    private SysUserService sysUserService;

    /**
     * 获取图片验证码
     *
     * @author xuyuxiang
     * @date 2022/7/8 9:26
     **/
    @ApiOperation("获取图片验证码")
    @Get
    @Mapping("/sys/userCenter/getPicCaptcha")
    public CommonResult<SysUserPicValidCodeResult> getPicCaptcha() {
        return CommonResult.data(sysUserService.getPicCaptcha());
    }

    /**
     * 找回密码获取手机验证码
     *
     * @author xuyuxiang
     * @date 2021/10/13 14:01
     **/
    @ApiOperation("找回密码获取手机验证码")
    @Get
    @Mapping("/sys/userCenter/findPasswordGetPhoneValidCode")
    public CommonResult<String> findPasswordGetPhoneValidCode(@Validated SysUserGetPhoneValidCodeParam sysUserGetPhoneValidCodeParam) {
        return CommonResult.data(sysUserService.findPasswordGetPhoneValidCode(sysUserGetPhoneValidCodeParam));
    }

    /**
     * 找回密码获取邮箱验证码
     *
     * @author xuyuxiang
     * @date 2021/10/13 14:01
     **/
    @ApiOperation("找回密码获取邮箱验证码")
    @Get
    @Mapping("/sys/userCenter/findPasswordGetEmailValidCode")
    public CommonResult<String> findPasswordGetEmailValidCode(@Validated SysUserGetEmailValidCodeParam sysUserGetEmailValidCodeParam) {
        return CommonResult.data(sysUserService.findPasswordGetEmailValidCode(sysUserGetEmailValidCodeParam));
    }

    /**
     * 通过手机号找回用户密码
     *
     * @author xuyuxiang
     * @date 2021/10/13 14:01
     **/
    @ApiOperation("通过手机号找回用户密码")
    @CommonLog("通过手机号找回用户密码")
    @Post
    @Mapping("/sys/userCenter/findPasswordByPhone")
    public CommonResult<String> findPasswordByPhone(@Validated SysUserFindPwdByPhoneParam sysUserFindPwdByPhoneParam) {
        sysUserService.findPasswordByPhone(sysUserFindPwdByPhoneParam);
        return CommonResult.ok();
    }

    /**
     * 通过邮箱找回用户密码
     *
     * @author xuyuxiang
     * @date 2021/10/13 14:01
     **/
    @ApiOperation("通过邮箱找回用户密码")
    @CommonLog("通过邮箱找回用户密码")
    @Post
    @Mapping("/sys/userCenter/findPasswordByEmail")
    public CommonResult<String> findPasswordByEmail(@Validated SysUserFindPwdByEmailParam sysUserFindPwdByEmailParam) {
        sysUserService.findPasswordByEmail(sysUserFindPwdByEmailParam);
        return CommonResult.ok();
    }

    /**
     * 修改用户密码
     *
     * @author xuyuxiang
     * @date 2021/10/13 14:01
     **/
    @ApiOperation("修改用户密码")
    @CommonLog("修改用户密码")
    @Post
    @Mapping("/sys/userCenter/updatePassword")
    public CommonResult<String> updatePassword(@Validated SysUserUpdatePwdParam sysUserUpdatePwdParam) {
        sysUserService.updatePassword(sysUserUpdatePwdParam);
        return CommonResult.ok();
    }

    /**
     * 修改用户头像
     *
     * @author xuyuxiang
     * @date 2021/10/13 14:01
     **/
    @ApiOperation("修改用户头像")
    @CommonLog("修改用户头像")
    @Post
    @Mapping("/sys/userCenter/updateAvatar")
    public CommonResult<String> updateAvatar(@ApiParam(value="文件", required = true) UploadedFile file) {
        return CommonResult.data(sysUserService.updateAvatar(file));
    }

    /**
     * 修改用户签名图片
     *
     * @author xuyuxiang
     * @date 2021/10/13 14:01
     **/
    @ApiOperation("修改用户签名图片")
    @CommonLog("修改用户签名图片")
    @Post
    @Mapping("/sys/userCenter/updateSignature")
    public CommonResult<String> updateSignature(@Validated SysUserSignatureParam sysUserSignatureParam) {
        sysUserService.updateSignature(sysUserSignatureParam);
        return CommonResult.ok();
    }

    /**
     * 获取登录用户的菜单（B端、PC端菜单）
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取登录用户PC菜单")
    @Get
    @Mapping("/sys/userCenter/loginMenu")
    public CommonResult<List<Tree<String>>> loginMenu() {
        SysUserIdParam sysUserIdParam = new SysUserIdParam();
        sysUserIdParam.setId(StpUtil.getLoginIdAsString());
        return CommonResult.data(sysUserService.ownMenu(sysUserIdParam));
    }

    /**
     * 获取登录用户的菜单（B端、移动端菜单）
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取登录用户移动端菜单")
    @Get
    @Mapping("/sys/userCenter/loginMobileMenu")
    public CommonResult<List<Tree<String>>> loginMobileMenu() {
        SysUserIdParam sysUserIdParam = new SysUserIdParam();
        sysUserIdParam.setId(StpUtil.getLoginIdAsString());
        return CommonResult.data(sysUserService.ownMobileMenu(sysUserIdParam));
    }

    /**
     * 获取登录用户组织树
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取登录用户组织树")
    @Get
    @Mapping("/sys/userCenter/loginOrgTree")
    public CommonResult<List<Tree<String>>> loginOrgTree() {
        SysUserIdParam sysUserIdParam = new SysUserIdParam();
        sysUserIdParam.setId(StpUtil.getLoginIdAsString());
        return CommonResult.data(sysUserService.loginOrgTree(sysUserIdParam));
    }

    /**
     * 获取登录用户的职位信息
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取登录用户的职位信息")
    @Get
    @Mapping("/sys/userCenter/loginPositionInfo")
    public CommonResult<List<SysUserPositionResult>> loginPositionInfo() {
        SysUserIdParam sysUserIdParam = new SysUserIdParam();
        sysUserIdParam.setId(StpUtil.getLoginIdAsString());
        return CommonResult.data(sysUserService.loginPositionInfo(sysUserIdParam));
    }

    /**
     * 编辑个人信息
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:47
     */
    @ApiOperation("编辑个人信息")
    @CommonLog("编辑个人信息")
    @Post
    @Mapping("/sys/userCenter/updateUserInfo")
    public CommonResult<String> updateUserInfo(@Validated SysUserUpdateInfoParam sysUserUpdateInfoParam) {
        sysUserService.updateUserInfo(sysUserUpdateInfoParam);
        return CommonResult.ok();
    }

    /**
     * 编辑个人工作台
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:47
     */
    @ApiOperation("编辑个人工作台")
    @CommonLog("编辑个人工作台")
    @Post
    @Mapping("/sys/userCenter/updateUserWorkbench")
    public CommonResult<String> updateUserWorkbench(@Validated SysUserUpdateWorkbenchParam sysUserUpdateWorkbenchParam) {
        sysUserService.updateUserWorkbench(sysUserUpdateWorkbenchParam);
        return CommonResult.ok();
    }

    /**
     * 获取登录用户的工作台
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取登录用户的工作台")
    @Get
    @Mapping("/sys/userCenter/loginWorkbench")
    public CommonResult<String> loginWorkbench() {
        SysUserIdParam sysUserIdParam = new SysUserIdParam();
        sysUserIdParam.setId(StpUtil.getLoginIdAsString());
        return CommonResult.data(sysUserService.loginWorkbench(sysUserIdParam));
    }

    /**
     * 获取登录用户的站内信分页
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取登录用户的站内信分页")
    @Get
    @Mapping("/sys/userCenter/loginUnreadMessagePage")
    public CommonResult<Page<SysUserMessageResult>> loginMessagePage(SysUserMessagePageParam sysUserMessagePageParam) {
        return CommonResult.data(sysUserService.loginMessagePage(sysUserMessagePageParam));
    }

    /**
     * 读取登录用户站内信详情
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("读取登录用户站内信详情")
    @Get
    @Mapping("/sys/userCenter/loginUnreadMessageDetail")
    public CommonResult<SysUserMessageDetailResult> loginMessageDetail(@Validated SysUserMessageIdParam sysUserMessageIdParam) {
        return CommonResult.data(sysUserService.loginMessageDetail(sysUserMessageIdParam));
    }

    /**
     * 根据id集合获取组织集合
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("根据id集合获取组织集合")
    @Post
    @Mapping("/sys/userCenter/getOrgListByIdList")
    public CommonResult<List<SysOrg>> getOrgListByIdList(@Validated SysUserIdListParam sysUserIdListParam) {
        return CommonResult.data(sysUserService.getOrgListByIdList(sysUserIdListParam));
    }

    /**
     * 根据id集合获取用户集合
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("根据id集合获取用户集合")
    @Post
    @Mapping("/sys/userCenter/getUserListByIdList")
    public CommonResult<List<SysUser>> getUserListByIdList(@Validated SysUserIdListParam sysUserIdListParam) {
        return CommonResult.data(sysUserService.getUserListByIdList(sysUserIdListParam));
    }

    /**
     * 根据id集合获取职位集合
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("根据id集合获取职位集合")
    @Post
    @Mapping("/sys/userCenter/getPositionListByIdList")
    public CommonResult<List<SysPosition>> getPositionListByIdList(@Validated SysUserIdListParam sysUserIdListParam) {
        return CommonResult.data(sysUserService.getPositionListByIdList(sysUserIdListParam));
    }

    /**
     * 根据id集合获取角色集合
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("根据id集合获取角色集合")
    @Post
    @Mapping("/sys/userCenter/getRoleListByIdList")
    public CommonResult<List<SysRole>> getRoleListByIdList(@Validated SysUserIdListParam sysUserIdListParam) {
        return CommonResult.data(sysUserService.getRoleListByIdList(sysUserIdListParam));
    }
}
