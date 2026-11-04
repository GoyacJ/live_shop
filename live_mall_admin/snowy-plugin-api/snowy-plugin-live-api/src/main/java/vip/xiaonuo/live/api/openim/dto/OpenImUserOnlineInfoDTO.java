package vip.xiaonuo.live.api.openim.dto;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.List;
import java.util.Objects;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/30 16:47
 */
public record OpenImUserOnlineInfoDTO(
        String userID,
        /*
          在线状态，在线：1，离线：0
         */
        int status,
        List<PlatformStatus> detailPlatformStatus
) {

    public static OpenImUserOnlineInfoDTO of(JSONObject json) {
        return new OpenImUserOnlineInfoDTO(
                json.getStr("userID"),
                json.getInt("status"),
                json.containsKey("detailPlatformStatus") ? json.getJSONArray("detailPlatformStatus").stream().map(obj -> PlatformStatus.of(JSONUtil.parseObj(obj))).toList() : null
        );
    }


    public record PlatformStatus(

            /*
              用户登录时的终端类型，取值为1-9
             */
            int platformID,

            /*
              连接 ID
             */
            String connID,

            /*
              app 是否处于后台
             */
            boolean isBackground,

            /*
              token 信息
             */
            String token
    ) {
        public static PlatformStatus of(JSONObject json) {
            if (Objects.isNull(json)) {
                return null;
            }
            return new PlatformStatus(
                    json.getInt("platformID"),
                    json.getStr("connID"),
                    json.getBool("isBackground"),
                    json.getStr("token")
            );
        }
    }
}
