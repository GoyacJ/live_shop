package vip.xiaonuo.shop.modular.logistics.kdniao.request;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.validation.annotation.NotBlank;
import org.noear.solon.validation.annotation.NotNull;
import api.logistics.common.enums.Shipper;
import api.logistics.processor.request.LogisticsRequest;

import java.util.Objects;

/**
 * <p>即时查询请求</p>
 *
 * @author goya
 * @since 2025/4/24 16:41
 */
public record InstantQueryRequest(
        @NotNull(message = "快递公司编码不能为空")
        Shipper shipperCode,

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
          轨迹按时间排序，0-升序，1-降序，默认0
         */
        Integer sort,
        /*
          订单号
         */
        String orderCode
) implements LogisticsRequest {

    public static InstantQueryRequest of(String shipperCode, String logisticCode, String customerName) {
        return new InstantQueryRequest(Shipper.getByCode(shipperCode), logisticCode, customerName, null, null);
    }

    public static InstantQueryRequest of(Shipper shipperCode, String logisticCode, String customerName, String orderCode) {
        return new InstantQueryRequest(shipperCode, logisticCode, customerName, null, orderCode);
    }

    public String toJsonStr() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOpt("LogisticCode", this.logisticCode);
        if (Objects.nonNull(this.shipperCode)) {
            jsonObject.putOpt("ShipperCode", this.shipperCode.getCode());
        }
        if (StringUtils.isNotEmpty(this.customerName)) {
            jsonObject.putOpt("CustomerName", this.customerName);
        }
        if (Objects.nonNull(this.sort)) {
            jsonObject.putOpt("Sort", this.sort);
        }
        if (StringUtils.isNotEmpty(this.orderCode)) {
            jsonObject.putOpt("OrderCode", this.orderCode);
        }
        return JSONUtil.toJsonStr(jsonObject);
    }

}
