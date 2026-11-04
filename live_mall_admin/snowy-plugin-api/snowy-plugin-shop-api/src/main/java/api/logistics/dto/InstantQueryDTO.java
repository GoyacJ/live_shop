package api.logistics.dto;

import api.logistics.common.enums.KdniaoLogisticsState;
import api.logistics.common.enums.KdniaoLogisticsStateEx;
import api.logistics.common.enums.Shipper;
import api.logistics.processor.response.LogisticsResponse;

import java.util.List;

/**
 * <p>即时查询结果</p>
 *
 * @author goya
 * @since 2025/4/24 16:49
 */
public record InstantQueryDTO(

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


    public static InstantQueryDTO of(InstantQueryResponse response) {
        return new InstantQueryDTO(
                response.shipperCode(),
                response.logisticCode(),
                response.success(),
                response.reason(),
                response.state(),
                response.stateEx(),
                response.location(),
                Traces.of(response.traces()),
                response.orderCode(),
                response.callBack(),
                response.station(),
                response.stationTel(),
                response.deliveryMan(),
                response.deliveryManTel(),
                response.nextCity()
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

        public static Traces of(InstantQueryResponse.Traces response){
            return new Traces(
                    response.acceptTime(),
                    response.acceptStation(),
                    response.location(),
                    response.action(),
                    response.remark()
            );
        }

        public static List<Traces> of(List<InstantQueryResponse.Traces> response){
            return response.stream().map(r -> new Traces(
                    r.acceptTime(),
                    r.acceptStation(),
                    r.location(),
                    r.action(),
                    r.remark()
            )).toList();

        }
    }
}
