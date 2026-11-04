package vip.xiaonuo.pay.service.impl;

import cn.hutool.core.util.IdUtil;
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
import com.ijpay.wxpay.model.v3.Amount;
import com.ijpay.wxpay.model.v3.UnifiedOrderModel;
import lombok.extern.slf4j.Slf4j;
import org.noear.dami.Dami;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.Context;
import vip.xiaonuo.common.enums.OrderTypeMapperEnum;
import vip.xiaonuo.pay.entity.CorporatePaymentVo;
import vip.xiaonuo.pay.entity.CreatePayOrderVo;
import vip.xiaonuo.pay.entity.WxPayV3Bean;
import vip.xiaonuo.pay.service.PayService;

import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付相关操作
 * Created by GMQ on 2022/2/28 13:23
 */
@Component
@Slf4j
public class WxPayServiceImpl implements PayService {

    @Inject
    private WxPayV3Bean wxPayV3Bean;


    /**
     * 创建支付订单
     *
     * @param vo
     * @return T
     * @throws
     * @author GMQ
     * @date 2022/2/28 15:23
     **/
    @Override
    public Map<String, String> createPayOrder(CreatePayOrderVo vo) {
        try {
            String timeExpire = DateTimeZoneUtil.dateToTimeZone(System.currentTimeMillis() + 1000 * 60 * 3);
            UnifiedOrderModel unifiedOrderModel = new UnifiedOrderModel()
                    .setAppid(wxPayV3Bean.getAppId())
                    .setMchid(wxPayV3Bean.getMchId())
                    .setDescription(vo.getDescription())
                    .setOut_trade_no(vo.getOutTradeNo())
                    .setTime_expire(timeExpire)
                    .setAttach(vo.getAttach())
                    .setNotify_url(wxPayV3Bean.getDomain())
                    .setAmount(new Amount().setTotal(NumberUtil.mul(vo.getTotal(),100).intValue()).setCurrency("CNY"));
            log.info("微信统一下单参数 {}", JSONUtil.toJsonStr(unifiedOrderModel));
            X509Certificate certificate = PayKit.getCertificate(wxPayV3Bean.getCertPath());
            String serialNo = certificate.getSerialNumber().toString(16).toUpperCase();
            //serialNo:446FC99814D80A7E2DA15AEFF0E32EF70D013900
            IJPayHttpResponse response = WxPayApi.v3(
                    RequestMethodEnum.POST,
                    WxDomainEnum.CHINA.toString(),
                    BasePayApiEnum.APP_PAY.toString(),
                    wxPayV3Bean.getMchId(),
                    serialNo,
                    null,
                    wxPayV3Bean.getKeyPath(),
                    JSONUtil.toJsonStr(unifiedOrderModel),
                    AuthTypeEnum.RSA.getCode()
            );
            log.info("微信统一下单响应 {}", response);
            // 根据证书序列号查询对应的证书来验证签名结果
            boolean verifySignature = WxPayKit.verifySignature(response, wxPayV3Bean.getPlatformCertPath());
            log.info("verifySignature: {}", verifySignature);
            if (response.getStatus() == 200 && verifySignature) {
                String body = response.getBody();
                JSONObject jsonObject = JSONUtil.parseObj(body);
                String prepayId = jsonObject.getStr("prepay_id");
                Map<String, String> map = WxPayKit.appCreateSign(wxPayV3Bean.getAppId(), wxPayV3Bean.getMchId(), prepayId, wxPayV3Bean.getKeyPath());
                log.info("微信唤起支付参数:{}", map);
                return map;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 企业付款
     *
     * @param vo
     * @return T
     * @throws
     * @author GMQ
     * @date 2022/3/2 12:43
     **/
    @Override
    public <T> T corporatePayment(CorporatePaymentVo vo) {
        return null;
    }

    /**
     * 订单异步通知
     *
     * @return java.lang.String
     * @throws
     * @author GMQ
     * @date 2022/3/3 21:08
     **/
    @Override
    public void orderNotifyResult(Context ctx) {
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
                // Dami.bus().send("payBusinessBy"+attach.getStr("orderType"), jsonObject.getStr("attach"));
                Dami.bus().send("payBusinessBy"+attach.getStr("orderType"), jsonObject.getStr("out_trade_no"));

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


}