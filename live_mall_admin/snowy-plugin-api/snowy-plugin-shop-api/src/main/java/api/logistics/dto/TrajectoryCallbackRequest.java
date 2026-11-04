package api.logistics.dto;

import api.logistics.common.enums.KdniaoLogisticsState;
import api.logistics.common.enums.KdniaoLogisticsStateEx;
import api.logistics.common.enums.Shipper;
import api.logistics.processor.request.LogisticsRequest;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>轨迹推送请求</p>
 *
 * @author goya
 * @since 2025/4/25 11:26
 */
public record TrajectoryCallbackRequest(

        /*
          推送时间，示例：2021-01-01 09:00:00 Y
         */
        LocalDateTime pushTime,

        /*
          用户ID String(10) Y
         */
        String eBusinessId,

        /*
          推送轨迹的快递单号个数 String(2) Y
         */
        Integer count,

        /*
          推送轨迹的轨迹数据集合 Y
         */
        List<Data> data
) {


    public static TrajectoryCallbackRequest of(String response) {
        if (StringUtils.isEmpty(response)) {
            return null;
        }
        JSONObject jsonObject = JSONUtil.parseObj(response);
        return new TrajectoryCallbackRequest(
                jsonObject.getLocalDateTime("PushTime", null),
                jsonObject.getStr("EBusinessID"),
                jsonObject.getInt("Count"),
                jsonObject.getJSONArray("Data").stream().map(item -> Data.of(JSONUtil.parseObj(item))).toList()
        );
    }

    /**
     * 推送轨迹的轨迹数据集合
     * 均可用作自定义回传，会在推送轨迹时附带此字段，
     * 区别：OrderCode：以用户侧最新传值为准
     * Callback： 以用户侧首次传值为准
     */
    public record Data(
           /*
              成功与否(true/false) Bool(10) Y
             */
            boolean success,

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
              失败原因 String(50)
              success = true ,且此字段为空则表示有轨迹
              success = true ,且此字段不为空则表示无轨迹 ==》 暂无轨迹信息
             */
            String reason,

            /*
              订单编号 String(30)
             */
            String orderCode,

            /*
              普通物流状态 String(5) Y
             */
            KdniaoLogisticsState state,

            /*
              额外物流状态 String(5) Y
             */
            KdniaoLogisticsStateEx stateEx,

            /*
              历史节点所在城市 String(20)
             */
            String location,

            /*
              用户自定义回传字段 String(50)
             */
            String callBack,

            /*
              轨迹 Y
             */
            List<Traces> traces,

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
    ) implements LogisticsRequest {


        public static Data of(JSONObject jsonObject) {
            if (Objects.isNull(jsonObject)) {
                return null;
            }
            String eBusinessId = jsonObject.getStr("EBusinessID");
            Shipper shipperCode = Shipper.getByCode(jsonObject.getStr("ShipperCode"));
            String logisticCode = jsonObject.getStr("LogisticCode");
            boolean success = jsonObject.getBool("Success", false);
            String reason = jsonObject.getStr("Reason");
            KdniaoLogisticsState state = KdniaoLogisticsState.getByCode(jsonObject.getInt("State"));
            String orderCode = jsonObject.getStr("OrderCode");
            String callBack = jsonObject.getStr("CallBack");
            List<Traces> traces = jsonObject.getJSONArray("Traces").stream().map(item -> Traces.of(JSONUtil.parseObj(item))).toList();
            if (IterUtil.isNotEmpty(traces)) {
                success = true;
            }
            return new Data(
                    success,
                    eBusinessId,
                    shipperCode,
                    logisticCode,
                    reason,
                    orderCode,
                    state,
                    KdniaoLogisticsStateEx.getByCode(jsonObject.getInt("StateEx")),
                    jsonObject.getStr("Location"),
                    callBack,
                    traces,
                    jsonObject.getStr("Station"),
                    jsonObject.getStr("StationTel"),
                    jsonObject.getStr("DeliveryMan"),
                    jsonObject.getStr("DeliveryManTel"),
                    jsonObject.getStr("NextCity")
            );
        }


        /**
         * 轨迹信息
         *
         * @param acceptTime    轨迹发生时间,示例：2021-01-01 09:00:00 Y
         * @param acceptStation 轨迹描述 Y
         */
        public record Traces(
                /*
                  轨迹发生时间,示例：2021-01-01 09:00:00 Y
                 */
                String acceptTime,

                /*
                  轨迹描述 Y
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
}
