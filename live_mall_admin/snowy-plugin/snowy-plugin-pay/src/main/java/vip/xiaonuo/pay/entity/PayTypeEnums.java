package vip.xiaonuo.pay.entity;

/**
 * 支付方式枚举类
 */
public enum PayTypeEnums {

    OVER("over","余额支付"),
    WX("wx","微信支付"),
    INTEGRAL("integral","积分支付"),
    DY("dy","抖音支付"),
    ALI("ali","支付宝支付"),
    WX_H5("wx_h5","微信H5支付"),

    ;

    private String code;

    private String desc;

    PayTypeEnums(String code, String desc) {
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
