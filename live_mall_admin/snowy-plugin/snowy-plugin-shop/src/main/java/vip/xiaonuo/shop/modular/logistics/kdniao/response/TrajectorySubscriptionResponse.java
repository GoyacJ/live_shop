package vip.xiaonuo.shop.modular.logistics.kdniao.response;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import api.logistics.common.enums.Shipper;
import api.logistics.processor.request.LogisticsRequest;

import java.time.LocalDateTime;

/**
 * <p>轨迹订阅响应</p>
 *
 * @author goya
 * @since 2025/4/25 10:38
 */
public record TrajectorySubscriptionResponse(

          /*
          用户Id String(10) Y
         */
        String eBusinessId,

        /*
          更新时间，示例：
          2021-01-01 09:00:00
          String(32)
          Y
         */
        LocalDateTime updateTime,


        /*
          成功与否(true/false) Bool(10) Y
         */
        boolean success,

        /*
          快递公司编码 String(10) Y
         */
        Shipper shipperCode,

        /*
          快递单号 String(30) Y
         */
        String logisticCode
) implements LogisticsRequest {

    public static TrajectorySubscriptionResponse of(String response) {
        if (StringUtils.isEmpty(response)) {
            return null;
        }
        JSONObject jsonObject = JSONUtil.parseObj(response);
        return new TrajectorySubscriptionResponse(
                jsonObject.getStr("EBusinessID"),
                jsonObject.getLocalDateTime("UpdateTime", null),
                jsonObject.getBool("Success"),
                Shipper.getByCode(jsonObject.getStr("ShipperCode")),
                jsonObject.getStr("LogisticCode")
        );
    }
}
