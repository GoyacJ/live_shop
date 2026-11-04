package vip.xiaonuo.live.modular.callback;

import cn.dev33.satoken.annotation.SaIgnore;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Post;
import org.springframework.web.bind.annotation.RequestBody;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.live.modular.callback.dto.GroupMessageRequest;
import vip.xiaonuo.live.modular.callback.dto.GroupMessageResponse;

import javax.servlet.http.HttpServletRequest;

@Controller
public class GroupMessageController {

    @Inject
    private CommonCacheOperator commonCacheOperator;

    @SaIgnore
    @Post
    @Mapping("/callbackExample")
    public GroupMessageResponse callbackExample(@RequestBody GroupMessageRequest request, HttpServletRequest httpServletRequest) {
        boolean validJsonContentType = httpServletRequest.getContentType().toLowerCase().contains("application/json");
        if(!validJsonContentType){
            return GroupMessageResponse.createError(1,1,"请求contentType不是application/json类型","",1);
        }
        switch(request.getCallbackCommand()){
            case "callbackAfterJoinGroupCommand":
                handleMemberJoin(request.getGroupID(),request.getUserID());
                break;
            case "callbackAfterQuitGroupCommand":
                handleMenberLeave(request.getGroupID(),request.getUserID());
            default:
                return GroupMessageResponse.createError(1,1,"回调请求失败","",1);
        }
        return GroupMessageResponse.createSuccess(0,0,"回调请求成功","",0);
    }

    private void  handleMemberJoin(String groupId,String userId){
        //更新在线人数 redis
        commonCacheOperator.put("openim_user_count" + groupId,userId);
    }

    private void handleMenberLeave(String groupId,String userId){
        //移除在线用户
        commonCacheOperator.remove("openim_user_count" + groupId,userId);
    }

}
