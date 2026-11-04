package vip.xiaonuo.pay.entity;

/**
 * 支付分类枚举类
 */
public enum PayClassifyEnums {

    RECHARGE("1","余额充值"),
    PURCHASE_GOODS("2","购买商品"),


    ;

    private String code;

    private String desc;

    PayClassifyEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
