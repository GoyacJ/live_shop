package api.logistics.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/24 17:38
 */
@Getter
@AllArgsConstructor
public enum KdniaoRequestType {

    R1002("1002", "/api/dist", "即时查询(免费版)"),
    R1008("1008", "/api/dist", "轨迹订阅（免费版）"),

    /**
     * 8001和8002区别
     * ● 无需传快递编码（ShipperCode）自动识别单号所属快递公司
     * ● 若ShipperCode传值快递公司编码，则按照传值的快递编码获取物流信息
     * ● 计费方式不同
     *       （8001按单计算 / 8002按次计算）
     */
    R8001("8001", "/api/dist", "即时查询"),
    R8002("8002", "/api/dist", "快递查询"),


    R8008("8008", "/api/dist", "轨迹订阅"),



    R2201("2201", "", "预创建订单"),
    R2202("2202", "", "创建订单"),
    R2203("2203", "", "预取消订单"),
    R2204("2204", "", "取消订单"),
    R2205("2205", "", "订单信息查询"),
    R2206("2206", "", "业务员轨迹查询"),
    R2207("2207", "", "加价"),
    R2208("2208", "", "加价类型查询"),
    R2211("2211", "", "创建门店"),
    R2212("2212", "", "更新门店"),
    R2213("2213", "", "上传图片"),
    R2214("2214", "", "创建品牌总部"),
    R2215("2215", "", "查询配送员轨迹地图"),
    R2218("2218", "", "查询门店审核状态"),
    R2209("2209", "", "加价详情查询");

    private final String code;
    private final String api;
    private final String desc;
}
