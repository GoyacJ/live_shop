package vip.xiaonuo.live.api.openim.dto;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/9 17:15
 */
public record OpenImErrorResponseDTO(
        int errCode,
        String errMsg,
        String errDlt
) {
    public static OpenImErrorResponseDTO of(String resp) {
        JSONObject jsonObject = JSONUtil.parseObj(resp);
        return new OpenImErrorResponseDTO(
                jsonObject.getInt("errCode"),
                jsonObject.getStr("errMsg"),
                jsonObject.getStr("errDlt")
        );
    }
}
