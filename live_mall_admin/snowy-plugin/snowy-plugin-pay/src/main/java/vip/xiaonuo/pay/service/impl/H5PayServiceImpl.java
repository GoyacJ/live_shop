package vip.xiaonuo.pay.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.enums.AuthTypeEnum;
import com.ijpay.core.enums.RequestMethodEnum;
import com.ijpay.core.kit.PayKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.core.utils.DateTimeZoneUtil;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.enums.WxDomainEnum;
import com.ijpay.wxpay.enums.v3.BasePayApiEnum;
import com.ijpay.wxpay.model.v3.*;
import lombok.extern.slf4j.Slf4j;
import org.noear.dami.Dami;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.util.IpUtil;
import vip.xiaonuo.pay.dto.WechatPayCustomResultDTO;
import vip.xiaonuo.pay.dto.WechatPayOrderResultDTO;
import vip.xiaonuo.pay.entity.CreateH5PayOrderDTO;
import vip.xiaonuo.pay.entity.WxPayV3Bean;
import vip.xiaonuo.pay.service.H5PayService;

import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/23 09:20
 */
@Slf4j
@Component
public class H5PayServiceImpl implements H5PayService {

    @Inject
    private WxPayV3Bean wxPayV3Bean;

    @Override
    public Map<String, String> h5Pay(CreateH5PayOrderDTO dto) {
        try {
            String timeExpire = DateTimeZoneUtil.dateToTimeZone(System.currentTimeMillis() + 1000 * 60 * 3);
            SceneInfo sceneInfo = new SceneInfo();
            sceneInfo.setPayer_client_ip(IpUtil.global().getRealIp(Context.current()));
            sceneInfo.setDevice_id("");
            sceneInfo.setStore_info(new StoreInfo("001", "颐心相伴", null, null));
            sceneInfo.setH5_info(new H5Info("Wap", "颐心相伴", "https://admin.live.weilaizhihuigu.cn/", null, null));
            UnifiedOrderModel unifiedOrderModel = new UnifiedOrderModel()
                    .setAppid(wxPayV3Bean.getAppId())
                    .setMchid(wxPayV3Bean.getMchId())
                    .setDescription(dto.description())
                    .setOut_trade_no(dto.orderNo())
                    .setTime_expire(timeExpire)
                    .setScene_info(sceneInfo)
                    .setAttach(dto.attach())
                    .setNotify_url(wxPayV3Bean.getH5Domain())
                    .setAmount(new Amount().setTotal(NumberUtil.mul(dto.total(), 100).intValue()).setCurrency("CNY"));
            log.info("微信H5统一下单参数 {}", JSONUtil.toJsonStr(unifiedOrderModel));
            X509Certificate certificate = PayKit.getCertificate(wxPayV3Bean.getCertPath());
            String serialNo = certificate.getSerialNumber().toString(16).toUpperCase();
            //serialNo:446FC99814D80A7E2DA15AEFF0E32EF70D013900
            IJPayHttpResponse response = WxPayApi.v3(
                    RequestMethodEnum.POST,
                    WxDomainEnum.CHINA.toString(),
                    BasePayApiEnum.H5_PAY.toString(),
                    wxPayV3Bean.getMchId(),
                    serialNo,
                    null,
                    wxPayV3Bean.getKeyPath(),
                    JSONUtil.toJsonStr(unifiedOrderModel),
                    AuthTypeEnum.RSA.getCode()
            );
            log.info("微信H5统一下单响应 {}", response);
            // 根据证书序列号查询对应的证书来验证签名结果
            boolean verifySignature = WxPayKit.verifySignature(response, wxPayV3Bean.getPlatformCertPath());
            log.info("verifySignature: {}", verifySignature);
            if (response.getStatus() == 200 && verifySignature) {
                String body = response.getBody();
                JSONObject jsonObject = JSONUtil.parseObj(body);
                String h5Url = jsonObject.getStr("h5_url");
                String prepayId = jsonObject.getStr("prepay_id");
                Map<String, String> map = WxPayKit.appCreateSign(wxPayV3Bean.getAppId(), wxPayV3Bean.getMchId(), prepayId, wxPayV3Bean.getKeyPath());
                map.put("h5Url", h5Url);
                log.info("微信唤起支付参数:{}", map);
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public void h5PayNotify(Context ctx) {
        Map<String, String> map = new HashMap<>(12);
        try {
            String timestamp = ctx.header("Wechatpay-Timestamp");
            String nonce = ctx.header("Wechatpay-Nonce");
            String serialNo = ctx.header("Wechatpay-Serial");
            String signature = ctx.header("Wechatpay-Signature");

            String result = ctx.body();

            log.info("微信支付订单异步通知->Wechatpay-Timestamp={}", timestamp);
            log.info("微信支付订单异步通知->Wechatpay-Nonce={}", nonce);
            log.info("微信支付订单异步通知->Wechatpay-Serial={}", serialNo);
            log.info("微信支付订单异步通知->Wechatpay-Signature={}", signature);
            log.info("微信支付订单异步通知->接收数据：body={}", result);

            // 需要通过证书序列号查找对应的证书，verifyNotify 中有验证证书的序列号
            String plainText = WxPayKit.verifyNotify(serialNo, result, signature, nonce, timestamp,
                    wxPayV3Bean.getApiKey3(), wxPayV3Bean.getPlatformCertPath());

            log.info("微信支付订单异步通知->接收数据：notifyResult={}", plainText);

            if (StrUtil.isNotEmpty(plainText)) {
                ctx.status(200);
                map.put("code", "SUCCESS");
                map.put("message", "SUCCESS");
                log.info("微信支付订单异步通知->开始处理业务");
                JSONObject jsonObject = JSONUtil.parseObj(plainText);
                JSONObject attach = JSONUtil.parseObj(jsonObject.getStr("attach"));
                //发送bus
                Dami.bus().send("h5PayBusinessBy" + attach.getStr("orderType"), jsonObject.getStr("out_trade_no"));

            } else {
                ctx.status(500);
                map.put("code", "ERROR");
                map.put("message", "签名错误");
            }
            ctx.headerAdd("Content-type", ContentType.JSON.toString());
            ctx.output(JSONUtil.toJsonStr(map).getBytes(StandardCharsets.UTF_8));


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public WechatPayCustomResultDTO queryCustomOrder(String outTradeNo) {
        try {
            Map<String, String> params = new HashMap<>(16);
            params.put("mchid", wxPayV3Bean.getMchId());

            log.info("统一下单参数 {}", JSONUtil.toJsonStr(params));
            X509Certificate certificate = PayKit.getCertificate(wxPayV3Bean.getCertPath());
            String serialNo = certificate.getSerialNumber().toString(16).toUpperCase();
            IJPayHttpResponse response = WxPayApi.v3(
                    RequestMethodEnum.GET,
                    WxDomainEnum.CHINA.toString(),
                    String.format(BasePayApiEnum.ORDER_QUERY_BY_OUT_TRADE_NO.toString(), outTradeNo),
                    wxPayV3Bean.getMchId(),
                    serialNo,
                    null,
                    wxPayV3Bean.getKeyPath(),
                    params,
                    AuthTypeEnum.RSA.getCode()
            );
            log.info("查询响应 {}", response);
            // 根据证书序列号查询对应的证书来验证签名结果
            boolean verifySignature = WxPayKit.verifySignature(response, wxPayV3Bean.getPlatformCertPath());
            if (response.getStatus() == 200 && verifySignature) {
                String body = response.getBody();
                return JSONUtil.toBean(body, WechatPayCustomResultDTO.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public WechatPayOrderResultDTO queryOrderDetail(String transactionId) {
        return null;
    }
}
