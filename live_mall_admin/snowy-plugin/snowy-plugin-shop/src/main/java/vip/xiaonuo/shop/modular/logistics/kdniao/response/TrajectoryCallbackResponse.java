package vip.xiaonuo.shop.modular.logistics.kdniao.response;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import api.logistics.processor.response.LogisticsResponse;

import java.time.LocalDateTime;

/**
 * <p>轨迹推送响应</p>
 *
 * @author goya
 * @since 2025/4/25 11:26
 */
public record TrajectoryCallbackResponse(
        String eBusinessId,
        LocalDateTime updateTime,
        boolean success,
        String reason
) implements LogisticsResponse {

    public String toJsonStr(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOpt("EBusinessID", this.eBusinessId);
        jsonObject.putOpt("UpdateTime", this.updateTime);
        jsonObject.putOpt("Success", this.success);
        jsonObject.putOpt("Reason", this.reason);
        return JSONUtil.toJsonStr(jsonObject);
    }
}
