package vip.xiaonuo.mobile.modular.live.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Post;
import org.noear.solon.core.handle.Context;
import org.noear.solon.validation.annotation.NotNull;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.live.api.openim.OpenImApi;

import java.util.Map;

@Api(tags = "直播sdk控制器")
@SaIgnore
@Controller
@Valid
public class OpenController {

    @Inject
    private OpenImApi openImApi;

    @Post
    @Mapping("/mapi/auth/im/getUserToken")
    public CommonResult<Map<String,String>> getUserToken(int platformId, Context context){
        System.out.println(platformId);
        if (0 >= platformId) {
            return CommonResult.error("平台ID不能为空");
        }
        if (ObjectUtil.isEmpty(context.attr("uid"))) {
            return CommonResult.error("用户ID不能为空");
        }
        return CommonResult.data(openImApi.getUserToken(platformId, Convert.toStr(context.attr("uid"))));
    }

    // @ApiOperation("获取管理员token")
    // @Post
    // @Mapping("/live/token/getToken")
    // public CommonResult<Map<String,String>> getToken(){
    //     return CommonResult.data(openImApi.getAdminToken());
    // }



    @ApiOperation("注册用户")
    @Post
    @Mapping("/live/register/registerUser")
    public CommonResult<Map<String,String>> registerUser(@NotNull String userId, @NotNull String nickname, @NotNull String faceURL){
        return CommonResult.data(openImApi.registerUser(userId, nickname, faceURL));
    }

    @ApiOperation("创建群组")
    @Post
    @Mapping("/live/create/createGroup")
    public CommonResult<Map<String,String>> createGroup(@NotNull String ownerUserID,@NotNull String groupName){
        return CommonResult.data(openImApi.createGroup(ownerUserID, groupName));
    }

    @ApiOperation("敏感词过滤")
    @Post
    @Mapping("/live/check/checkSensitiveWord")
    public CommonResult<Map<String,String>> checkSensitiveWord(@NotNull String txt){
        return CommonResult.data(openImApi.checkSensitiveWord(txt));
    }

    @ApiOperation("用户进到群组")
    @Post
    @Mapping("/live/add/addUserToGroup")
    public CommonResult<Map<String,String>> addUserToGroup(@NotNull String groupId,@NotNull String invitedUserID){
        return CommonResult.data(openImApi.addUserToGroup(groupId, invitedUserID));
    }

    @ApiOperation("用户退出群组")
    @Post
    @Mapping("/live/quit/quitGroup")
    public CommonResult<Map<String,String>> quitGroup(@NotNull String groupId,@NotNull String userId){
        return CommonResult.data(openImApi.quitGroup(groupId, userId));
    }

    @ApiOperation("发送信息")
    @Post
    @Mapping("/live/send/sendBarrage")
    public CommonResult<Map<String,String>> sendBarrage(@NotNull String groupId,@NotNull String userId,@NotNull String content){
        return CommonResult.data(openImApi.sendBarrage(groupId, userId,content));
    }
}
