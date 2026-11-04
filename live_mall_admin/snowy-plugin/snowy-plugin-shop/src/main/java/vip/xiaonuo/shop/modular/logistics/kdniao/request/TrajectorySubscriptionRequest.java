package vip.xiaonuo.shop.modular.logistics.kdniao.request;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.noear.solon.validation.annotation.NotBlank;
import org.noear.solon.validation.annotation.NotNull;
import api.logistics.common.enums.Shipper;
import api.logistics.processor.request.LogisticsRequest;

import java.util.Objects;

/**
 * <p>轨迹订阅请求</p>
 *
 * @author goya
 * @since 2025/4/25 10:38
 */
public record TrajectorySubscriptionRequest(

        /*
          快递公司编码
         */
        @NotNull(message = "快递公司编码不能为空")
        Shipper shipperCode,

        /*
          快递单号
         */
        @NotBlank(message = "快递单号不能为空")
        String logisticCode,

        /*
          寄件人or收件人 手机号后四位
          顺丰必填、跨越必填
          ● 尽量使用寄件人尾号
          ● 顺丰可能用虚拟号下单录入系统  作为验证标准
          ● 后四位传值错误无法获取到轨迹（可能拉取缓存）
          ● 顺丰后四位正确性可通过微信小程序【顺丰速运+】验证
         */
        String customerName,

        /*
          轨迹排序，0-升序，1-降序，默认0-升序, Int(1)
         */
        Integer sort,

        /*
          订单号 String(30)
         */
        String orderCode,

        /*
          用户自定义回传字段 String(50)
         */
        String callBack,

        /*
          自定义回调地址（http/https）
          物流轨迹优先按此地址推送
          重复订阅时，以最新地址为准
          生效前提：官网必须有已配置的回调地址
          String(255)
         */
        String callBackUrl,

        /*
          收件人信息
         */
        Receiver receiver,

        /*
          发件人信息
         */
        Receiver sender,

        /*
          是否订阅短信：
          0：不需要，1：需要
          Int(1)
         */
        Integer isSendMessage
) implements LogisticsRequest {


    public static TrajectorySubscriptionRequest of(String shipperCode, String logisticCode, String customerName) {
        return new TrajectorySubscriptionRequest(
                Shipper.getByCode(shipperCode),
                logisticCode,
                customerName,
                0,
                null,
                null,
                null,
                null,
                null,
                0
        );
    }

    public static TrajectorySubscriptionRequest of(Shipper shipperCode, String logisticCode, String customerName, String orderCode) {
        return new TrajectorySubscriptionRequest(
                shipperCode,
                logisticCode,
                customerName,
                0,
                orderCode,
                null,
                null,
                null,
                null,
                0
        );
    }

    public String toJsonStr() {
        JSONObject jsonObject = new JSONObject();
        if (Objects.nonNull(this.shipperCode)) {
            jsonObject.putOpt("ShipperCode", this.shipperCode.getCode());
        }
        jsonObject.putOpt("LogisticCode", this.logisticCode);
        jsonObject.putOpt("Sort", this.sort);
        jsonObject.putOpt("CustomerName", this.customerName);
        jsonObject.putOpt("OrderCode", this.orderCode);
        jsonObject.putOpt("Callback", this.callBack);
        jsonObject.putOpt("CallbackUrl", this.callBackUrl);
        if (Objects.nonNull(this.receiver)) {
            jsonObject.putOpt("Receiver", this.receiver.toJsonStr());
        }
        if (Objects.nonNull(this.sender)) {
            jsonObject.putOpt("Sender", this.sender.toJsonStr());
        }
        jsonObject.putOpt("IsSendMessage", isSendMessage);
        return JSONUtil.toJsonStr(jsonObject);
    }


    /**
     * 人信息
     */
    public record Receiver(

            /*
              收件人/发件人公司 String(30)
             */
            String company,

            /*
              收件人/发件人姓名 String(30)
             */
            String name,

            /*
              使用短信接口时
              电话与手机，选填一个 String(20)
             */
            String tel,

            /*
              使用短信接口时
              电话与手机，选填一个 String(20)
             */
            String mobile,

            /*
              省 String(20)
             */
            String provinceName,

            /*
              市 String(20)
             */
            String cityName,

            /*
              区/县 String(20)
             */
            String expAreaName,

            /*
              详细地址 String(100)
             */
            String address
    ) {

        public String toJsonStr() {
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("Company", this.company);
            jsonObject.putOpt("Name", this.name);
            jsonObject.putOpt("Tel", this.tel);
            jsonObject.putOpt("Mobile", this.mobile);
            jsonObject.putOpt("ProvinceName", this.provinceName);
            jsonObject.putOpt("CityName", this.cityName);
            jsonObject.putOpt("ExpAreaName", this.expAreaName);
            jsonObject.putOpt("Address", this.address);
            return JSONUtil.toJsonStr(jsonObject);
        }
    }
}
