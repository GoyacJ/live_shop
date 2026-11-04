package vip.xiaonuo.live.modular.openim;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.noear.solon.annotation.Body;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Post;

@SaIgnore
@Controller
public class OpenImCallback {

    @Post
    @Mapping("/openIm/callbackCommand")
    public Dict callbackCommand(@Body Dict param){

        System.out.println(JSONUtil.toJsonStr(param));
        /**
         * 字段	值	说明
         * actionCode	0	表示业务系统的回调是否正确执行。0表示操作成功。
         * errCode	0	表示自定义错误码，此处填 0 代表忽略回调结果。
         * errMsg	"An error message"	自定义错误码对应的简单错误信息。
         * errDlt	"Detailed error information"	自定义错误码对应的详细错误信息。
         * nextCode	1	下一步执行指令，1表示拒绝继续执行，actionCode 等于0时设置。
         */
        Dict result = new Dict();
        result.put("actionCode",0);
        result.put("errCode",0);
        result.put("errMsg","errrrrrrrrr");
        result.put("nextCode",1);
        return result;
    }

}
