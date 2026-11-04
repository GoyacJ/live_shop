package api.logistics.dto;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import api.logistics.common.enums.KdniaoLogisticsState;
import api.logistics.common.enums.KdniaoLogisticsStateEx;
import api.logistics.common.enums.Shipper;
import api.logistics.processor.response.LogisticsResponse;

import java.util.List;

/**
 * <p>即时查询响应</p>
 *
 * @author goya
 * @since 2025/4/24 16:49
 */
public record InstantQueryResponse(

        /*
          用户Id String(10) Y
         */
        String eBusinessId,

        /*
          快递公司编码 String(10) Y
         */
        Shipper shipperCode,

        /*
          快递单号 String(30) Y
         */
        String logisticCode,

        /*
          成功与否(true/false) Bool(10) Y
         */
        boolean success,

        /*
          失败原因 String(50)
          success = true ,且此字段为空则表示有轨迹
          success = true ,且此字段不为空则表示无轨迹 ==》 暂无轨迹信息
         */
        String reason,

        /*
          普通物流状态 String(5) Y
         */
        KdniaoLogisticsState state,

        /*
          扩展物流状态 String(5) Y
         */
        KdniaoLogisticsStateEx stateEx,

        /*
          当前所在城市 String(20) Y
         */
        String location,

        /*
          轨迹 Y
         */
        List<Traces> traces,

        /*
          订单编号 String(30)
         */
        String orderCode,

         /*
          用户自定义回传字段 String(50)
         */
        String callBack,

         /*
          派件网点的名称 String(50)
         */
        String station,

         /*
          派件网点的电话 String(50)
         */
        String stationTel,

        /*
          派件快递员 String(50)
         */
        String deliveryMan,

        /*
          派件快递员手机号 String(50)
         */
        String deliveryManTel,

        /*
          下一站城市 String(50)
         */
        String nextCity
) implements LogisticsResponse {

    public static InstantQueryResponse of(String response) {
        if (StringUtils.isEmpty(response)) {
            return null;
        }
        JSONObject jsonObject = JSONUtil.parseObj(response);
        InstantQueryResponse instantQueryResponse;
        if (Boolean.TRUE.equals(jsonObject.getBool("Success", false))) {

            String resReason = jsonObject.getStr("Reason");
            if (StringUtils.isEmpty(resReason)) {

                List<Traces> tracesList = jsonObject.getJSONArray("Traces").stream().map(item -> Traces.of(JSONUtil.parseObj(item))).toList();

                instantQueryResponse = new InstantQueryResponse(
                        jsonObject.getStr("EBusinessID"),
                        Shipper.getByCode(jsonObject.getStr("ShipperCode")),
                        jsonObject.getStr("LogisticCode"),
                        true,
                        resReason,
                        KdniaoLogisticsState.getByCode(jsonObject.getInt("State")),
                        KdniaoLogisticsStateEx.getByCode(jsonObject.getInt("StateEx")),
                        jsonObject.getStr("Location"),
                        tracesList,
                        jsonObject.getStr("OrderCode"),
                        jsonObject.getStr("CallBack"),
                        jsonObject.getStr("Station"),
                        jsonObject.getStr("StationTel"),
                        jsonObject.getStr("DeliveryMan"),
                        jsonObject.getStr("DeliveryManTel"),
                        jsonObject.getStr("NextCity")
                );
            } else if (StringUtils.equals("暂无轨迹信息", resReason)) {
                instantQueryResponse = new InstantQueryResponse(
                        jsonObject.getStr("EBusinessID"),
                        Shipper.getByCode(jsonObject.getStr("ShipperCode")),
                        jsonObject.getStr("LogisticCode"),
                        true,
                        "暂无轨迹信息",
                        KdniaoLogisticsState.getByCode(jsonObject.getInt("State")),
                        KdniaoLogisticsStateEx.getByCode(jsonObject.getInt("StateEx")),
                        jsonObject.getStr("Location"),
                        Lists.newArrayList(),
                        jsonObject.getStr("OrderCode"),
                        jsonObject.getStr("CallBack"),
                        jsonObject.getStr("Station"),
                        jsonObject.getStr("StationTel"),
                        jsonObject.getStr("DeliveryMan"),
                        jsonObject.getStr("DeliveryManTel"),
                        jsonObject.getStr("NextCity")
                );
            } else {
                instantQueryResponse = new InstantQueryResponse(
                        jsonObject.getStr("EBusinessID"),
                        Shipper.getByCode(jsonObject.getStr("ShipperCode")),
                        jsonObject.getStr("LogisticCode"),
                        true,
                        resReason,
                        KdniaoLogisticsState.getByCode(jsonObject.getInt("State")),
                        KdniaoLogisticsStateEx.getByCode(jsonObject.getInt("StateEx")),
                        jsonObject.getStr("Location"),
                        Lists.newArrayList(),
                        jsonObject.getStr("OrderCode"),
                        jsonObject.getStr("CallBack"),
                        jsonObject.getStr("Station"),
                        jsonObject.getStr("StationTel"),
                        jsonObject.getStr("DeliveryMan"),
                        jsonObject.getStr("DeliveryManTel"),
                        jsonObject.getStr("NextCity")
                );
            }
        } else {
            instantQueryResponse = new InstantQueryResponse(
                    jsonObject.getStr("EBusinessID"),
                    Shipper.getByCode(jsonObject.getStr("ShipperCode")),
                    jsonObject.getStr("LogisticCode"),
                    true,
                    jsonObject.getStr("Reason"),
                    KdniaoLogisticsState.getByCode(jsonObject.getInt("State")),
                    KdniaoLogisticsStateEx.getByCode(jsonObject.getInt("StateEx")),
                    jsonObject.getStr("Location"),
                    Lists.newArrayList(),
                    jsonObject.getStr("OrderCode"),
                    jsonObject.getStr("CallBack"),
                    jsonObject.getStr("Station"),
                    jsonObject.getStr("StationTel"),
                    jsonObject.getStr("DeliveryMan"),
                    jsonObject.getStr("DeliveryManTel"),
                    jsonObject.getStr("NextCity")
            );
        }

        return instantQueryResponse;

    }


    /**
     * 轨迹信息
     *
     * @param acceptTime    轨迹发生时间,示例：2021-01-01 09:00:00 Y
     * @param acceptStation 轨迹描述 Y
     */
    public record Traces(
            /*
              轨迹发生时间,示例：2021-01-01 09:00:00 String(32) Y
             */
            String acceptTime,

            /*
              轨迹描述 String(500) Y
             */
            String acceptStation,

            /*
              历史节点所在城市 String(20)
             */
            String location,

            /*
              同 StateEx String(50) Y
             */
            KdniaoLogisticsStateEx action,

            /*
              备注 String(20)
             */
            String remark
    ) {

        public static Traces of(JSONObject jsonObject) {
            return new Traces(
                    jsonObject.getStr("AcceptTime"),
                    jsonObject.getStr("AcceptStation"),
                    jsonObject.getStr("Location"),
                    KdniaoLogisticsStateEx.getByCode(jsonObject.getInt("Action")),
                    jsonObject.getStr("Remark")
            );
        }
    }
}
