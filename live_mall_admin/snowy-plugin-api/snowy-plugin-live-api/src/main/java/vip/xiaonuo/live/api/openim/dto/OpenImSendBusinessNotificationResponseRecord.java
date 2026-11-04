package vip.xiaonuo.live.api.openim.dto;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.Objects;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/8 10:39
 */
public record OpenImSendBusinessNotificationResponseRecord(
        int errCode,
        String errMsg,
        String errDlt,
        Data data
) {

    public static OpenImSendBusinessNotificationResponseRecord of(String jsonStr) {
        JSONObject resJson = JSONUtil.parseObj(jsonStr);
        return new OpenImSendBusinessNotificationResponseRecord(
                resJson.getInt("errCode"),
                resJson.getStr("errMsg"),
                resJson.getStr("errDlt"),
                resJson.containsKey("data") ? Data.of(resJson.getJSONObject("data")) : null
        );
    }

    public record Data(
            String serverMsgId,
            String clientMsgId,
            long sendTime
    ) {
        public static Data of(JSONObject json) {
            if (Objects.isNull(json)) {
                return null;
            }
            return new Data(
                    json.getStr("serverMsgID"),
                    json.getStr("clientMsgID"),
                    json.getLong("sendTime")
            );
        }
    }
}
