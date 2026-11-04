package vip.xiaonuo.pay.dto;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * 微信支付 - 订单查询结果（v3接口返回结构）
 * 文档：https://pay.weixin.qq.com/doc/v3/merchant/4012791837
 *
 * @author goya
 * @since 2025/6/23 14:46
 */
@Data
public class WechatPayOrderResultDTO {

    /**
     * 微信支付的商户appid
     * 示例值：wxd678efh567hg6787
     */
    private String appid;

    /**
     * 商户号
     * 示例值：1230000109
     */
    private String mchid;

    /**
     * 商户订单号（商户系统生成的订单号）
     * 示例值：1217752501201407033233368018
     */
    private String out_trade_no;

    /**
     * 微信支付订单号
     * 示例值：1217752501201407033233368018
     */
    private String transaction_id;

    /**
     * 交易类型：JSAPI、NATIVE、APP、MICROPAY、MWEB、FACEPAY
     * 示例值：APP
     */
    private String trade_type;

    /**
     * 交易状态
     * 示例值：SUCCESS（支付成功）、REFUND（转入退款）、NOTPAY（未支付）等
     */
    private String trade_state;

    /**
     * 交易状态描述，对用户展示的描述信息
     * 示例值：支付成功
     */
    private String trade_state_desc;

    /**
     * 付款银行
     * 示例值：CMC（招商银行），见银行列表：https://pay.weixin.qq.com/wiki/doc/apiv3/terms_definition/chapter1_1_3.shtml
     */
    private String bank_type;

    /**
     * 附加数据，在下单时提交的attach原样返回
     * 示例值：自定义数据
     */
    private String attach;

    /**
     * 支付完成时间，遵循 RFC3339 标准格式
     * 示例值：2018-06-08T10:34:56+08:00
     */
    private OffsetDateTime success_time;

    /**
     * 支付者信息
     */
    private Payer payer;

    /**
     * 金额信息
     */
    private Amount amount;

    /**
     * 场景信息
     */
    private SceneInfo scene_info;

    /**
     * 优惠功能，享受优惠时返回该字段
     */
    private List<PromotionDetail> promotion_detail;

    @Data
    public static class Payer {
        /**
         * 用户在商户appid下的唯一标识
         * 示例值：oUpF8uMuAJO_M2pxb1Q9zNjWeS6o
         */
        private String openid;
    }

    @Data
    public static class Amount {
        /**
         * 订单总金额，单位：分
         * 示例值：100
         */
        private Integer total;

        /**
         * 用户支付金额，单位：分
         * 示例值：90
         */
        private Integer payer_total;

        /**
         * 货币类型，符合 ISO 4217 标准的三位字母代码，默认人民币：CNY
         */
        private String currency;

        /**
         * 用户支付币种，通常与 currency 相同
         */
        private String payer_currency;
    }

    @Data
    public static class SceneInfo {
        /**
         * 商户端设备号（自定义字段，用于识别终端设备）
         * 示例值：013467007045764
         */
        private String device_id;
    }

    @Data
    public static class PromotionDetail {
        /**
         * 券ID
         * 示例值：109519
         */
        private String coupon_id;

        /**
         * 优惠名称
         * 示例值：单品惠-6
         */
        private String name;

        /**
         * 优惠范围：GLOBAL（全场券）、SINGLE（单品券）
         * 示例值：SINGLE
         */
        private String scope;

        /**
         * 优惠类型：CASH（充值型代金券）、NOCASH（免充值型代金券）
         * 示例值：CASH
         */
        private String type;

        /**
         * 优惠券面额，单位：分
         * 示例值：10
         */
        private Integer amount;

        /**
         * 活动ID（或券库存ID）
         * 示例值：931386
         */
        private String stock_id;

        /**
         * 微信支付出资金额，单位：分
         * 示例值：0
         */
        private Integer wechatpay_contribute;

        /**
         * 商户出资金额，单位：分
         * 示例值：10
         */
        private Integer merchant_contribute;

        /**
         * 其他出资方出资金额，单位：分
         * 示例值：0
         */
        private Integer other_contribute;

        /**
         * 优惠币种，默认 CNY
         */
        private String currency;

        /**
         * 单品列表（如果是单品券，需提供详细商品信息）
         */
        private List<GoodsDetail> goods_detail;

        @Data
        public static class GoodsDetail {
            /**
             * 商品编码
             * 示例值：M1006
             */
            private String goods_id;

            /**
             * 商品数量
             * 示例值：1
             */
            private Integer quantity;

            /**
             * 商品原价，单位：分
             * 示例值：100
             */
            private Integer unit_price;

            /**
             * 商品优惠金额，单位：分
             * 示例值：10
             */
            private Integer discount_amount;

            /**
             * 商品备注
             * 示例值：商品备注信息
             */
            private String goods_remark;
        }
    }
}
