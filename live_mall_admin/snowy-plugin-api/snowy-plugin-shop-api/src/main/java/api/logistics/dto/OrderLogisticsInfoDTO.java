package api.logistics.dto;

import api.logistics.common.enums.KdniaoLogisticsState;
import api.logistics.common.enums.KdniaoLogisticsStateEx;
import api.logistics.common.enums.Shipper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/27 10:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderLogisticsInfoDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -3239061271882344997L;

    private boolean success;
    private Shipper shipperCode;
    private String logisticCode;
    private String reason;
    private String orderCode;
    private KdniaoLogisticsState state;
    private KdniaoLogisticsStateEx stateEx;
    private String location;
    private String callBack;
    private List<Traces> traces;
    private String station;
    private String stationTel;
    private String deliveryMan;
    private String deliveryManTel;
    private String nextCity;


    public static OrderLogisticsInfoDTO of(InstantQueryDTO data) {

        return new OrderLogisticsInfoDTO(
                data.success(),
                data.shipperCode(),
                data.logisticCode(),
                data.reason(),
                data.orderCode(),
                data.state(),
                data.stateEx(),
                data.location(),
                data.callBack(),
                data.traces().stream().map(Traces::of).toList(),
                data.station(),
                data.stationTel(),
                data.deliveryMan(),
                data.deliveryManTel(),
                data.nextCity()
        );
    }


    public static OrderLogisticsInfoDTO of(TrajectoryCallbackRequest.Data data) {

        return new OrderLogisticsInfoDTO(
                data.success(),
                data.shipperCode(),
                data.logisticCode(),
                data.reason(),
                data.orderCode(),
                data.state(),
                data.stateEx(),
                data.location(),
                data.callBack(),
                data.traces().stream().map(Traces::of).toList(),
                data.station(),
                data.stationTel(),
                data.deliveryMan(),
                data.deliveryManTel(),
                data.nextCity()
        );
    }


    /**
     * 轨迹信息
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Traces implements Serializable{
        @Serial
        private static final long serialVersionUID = 4299723998036862065L;

        private String acceptTime;
        private String acceptStation;
        private String location;
        private KdniaoLogisticsStateEx action;
        private String remark;

        public static Traces of(InstantQueryDTO.Traces traces) {
            return new Traces(
                    traces.acceptTime(),
                    traces.acceptStation(),
                    traces.location(),
                    traces.action(),
                    traces.remark()
            );
        }

        public static Traces of(TrajectoryCallbackRequest.Data.Traces traces) {
            return new Traces(
                    traces.acceptTime(),
                    traces.acceptStation(),
                    traces.location(),
                    traces.action(),
                    traces.remark()
            );
        }
    }
}
