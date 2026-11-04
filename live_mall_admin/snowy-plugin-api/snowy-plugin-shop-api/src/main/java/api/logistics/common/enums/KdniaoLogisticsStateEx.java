package api.logistics.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>快递鸟扩展编码</p>
 *
 * @author goya
 * @since 2025/4/24 16:56
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum KdniaoLogisticsStateEx {

    ON_THE_WAY_01(201, "到达派件城市", KdniaoLogisticsState.ON_THE_WAY),
    ON_THE_WAY_02(202, "派件中", KdniaoLogisticsState.ON_THE_WAY),
    ON_THE_WAY_04(204, "到达转运中心", KdniaoLogisticsState.ON_THE_WAY),
    ON_THE_WAY_05(205, "到达派件网点", KdniaoLogisticsState.ON_THE_WAY),
    ON_THE_WAY_06(206, "寄件网点发件", KdniaoLogisticsState.ON_THE_WAY),
    ON_THE_WAY_11(211, "已放入快递柜或驿站", KdniaoLogisticsState.ON_THE_WAY),


    SIGN_FOR_RECEIPT_01(301, "正常签收", KdniaoLogisticsState.SIGN_FOR_RECEIPT),
    SIGN_FOR_RECEIPT_02(302, "轨迹异常后最终签收", KdniaoLogisticsState.SIGN_FOR_RECEIPT),
    SIGN_FOR_RECEIPT_04(304, "代收签收", KdniaoLogisticsState.SIGN_FOR_RECEIPT),
    SIGN_FOR_RECEIPT_11(311, "快递柜或驿站签收", KdniaoLogisticsState.SIGN_FOR_RECEIPT),


    PROBLEM_ITEM_01(401, "发货无信息", KdniaoLogisticsState.PROBLEM_ITEM),
    PROBLEM_ITEM_02(402, "超时未签收", KdniaoLogisticsState.PROBLEM_ITEM),
    PROBLEM_ITEM_03(403, "超时未更新", KdniaoLogisticsState.PROBLEM_ITEM),
    PROBLEM_ITEM_04(404, "拒收(退件)", KdniaoLogisticsState.PROBLEM_ITEM),
    PROBLEM_ITEM_05(405, "派件异常", KdniaoLogisticsState.PROBLEM_ITEM),
    PROBLEM_ITEM_06(406, "退货签收", KdniaoLogisticsState.PROBLEM_ITEM),
    PROBLEM_ITEM_07(407, "退货未签收", KdniaoLogisticsState.PROBLEM_ITEM),
    PROBLEM_ITEM_12(412, "快递柜或驿站超时未取", KdniaoLogisticsState.PROBLEM_ITEM),
    PROBLEM_ITEM_13(413, "单号已拦截", KdniaoLogisticsState.PROBLEM_ITEM),
    PROBLEM_ITEM_14(414, "破损", KdniaoLogisticsState.PROBLEM_ITEM),
    PROBLEM_ITEM_15(415, "客户取消发货", KdniaoLogisticsState.PROBLEM_ITEM),
    PROBLEM_ITEM_16(416, "无法联系", KdniaoLogisticsState.PROBLEM_ITEM),
    PROBLEM_ITEM_17(417, "配送延迟", KdniaoLogisticsState.PROBLEM_ITEM),
    PROBLEM_ITEM_18(418, "快件取出", KdniaoLogisticsState.PROBLEM_ITEM),
    PROBLEM_ITEM_19(419, "重新派送", KdniaoLogisticsState.PROBLEM_ITEM),
    PROBLEM_ITEM_20(420, "收货地址不详细", KdniaoLogisticsState.PROBLEM_ITEM),
    PROBLEM_ITEM_21(421, "收件人电话错误", KdniaoLogisticsState.PROBLEM_ITEM),
    PROBLEM_ITEM_22(422, "错分件", KdniaoLogisticsState.PROBLEM_ITEM),
    PROBLEM_ITEM_23(423, "超区件", KdniaoLogisticsState.PROBLEM_ITEM),


    CUSTOMS_CLEARANCE_01(601, "待清关", KdniaoLogisticsState.CUSTOMS_CLEARANCE),
    CUSTOMS_CLEARANCE_02(602, "清关中", KdniaoLogisticsState.CUSTOMS_CLEARANCE),
    CUSTOMS_CLEARANCE_03(603, "已清关", KdniaoLogisticsState.CUSTOMS_CLEARANCE),
    CUSTOMS_CLEARANCE_04(604, "清关异常", KdniaoLogisticsState.CUSTOMS_CLEARANCE),


    PENDING_ACCEPTANCE(10, "待揽件", null),
    ;

    @EnumValue
    private final Integer code;

    private final String description;

    private final KdniaoLogisticsState state;

    /**
     * 根据 code 获取枚举实例
     *
     * @param code 枚举中的 code 字段值
     * @return 匹配的枚举实例，若无匹配则返回 null
     */
    public static KdniaoLogisticsStateEx getByCode(Integer code) {
        for (KdniaoLogisticsStateEx type : KdniaoLogisticsStateEx.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }
}
