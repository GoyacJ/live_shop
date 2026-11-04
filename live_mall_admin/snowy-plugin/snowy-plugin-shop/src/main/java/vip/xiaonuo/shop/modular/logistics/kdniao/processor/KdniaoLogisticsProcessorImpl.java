package vip.xiaonuo.shop.modular.logistics.kdniao.processor;

import api.logistics.common.constants.LogisticsConstants;
import api.logistics.common.enums.KdniaoRequestType;
import api.logistics.dto.InstantQueryResponse;
import api.logistics.dto.OrderLogisticsInfoDTO;
import api.logistics.dto.TrajectoryCallbackRequest;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.google.common.collect.Maps;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.net.PercentCodec;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import org.noear.solon.net.http.HttpUtils;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.shop.modular.logistics.configuration.properties.LogisticsProperties;
import vip.xiaonuo.shop.modular.logistics.entity.OrderLogistics;
import vip.xiaonuo.shop.modular.logistics.kdniao.request.InstantQueryRequest;
import vip.xiaonuo.shop.modular.logistics.kdniao.request.TrajectorySubscriptionRequest;
import vip.xiaonuo.shop.modular.logistics.kdniao.response.TrajectoryCallbackResponse;
import vip.xiaonuo.shop.modular.logistics.kdniao.response.TrajectorySubscriptionResponse;
import vip.xiaonuo.shop.modular.logistics.service.OrderLogisticsService;
import vip.xiaonuo.shop.modular.mallorder.service.MallOrderService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>快递鸟</p>
 *
 * @author goya
 * @since 2025/4/24 15:50
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class KdniaoLogisticsProcessorImpl implements KdniaoLogisticsProcessor {

    @Inject
    private LogisticsProperties logisticsProperties;

    @Inject
    private CommonCacheOperator commonCacheOperator;

    @Inject
    private OrderLogisticsService orderLogisticsService;

    @Inject
    private MallOrderService mallOrderService;


    @Override
    public InstantQueryResponse instantQuery(InstantQueryRequest request) {
        return InstantQueryResponse.of(request(request.toJsonStr(), KdniaoRequestType.R8001));
    }

    @Override
    public TrajectorySubscriptionResponse trajectorySubscription(TrajectorySubscriptionRequest request) {
        return TrajectorySubscriptionResponse.of(request(request.toJsonStr(), KdniaoRequestType.R8008));
    }

    @Override
    @Tran
    public String trajectoryCallback(String requestData, String eBusinessId, String requestType, String dataSign, String dataType) {

        log.info("[物流信息] --- |快递鸟| ---:{}", requestData);

        TrajectoryCallbackRequest trajectoryPushRequest = TrajectoryCallbackRequest
                .of(urlDecoder(requestData));

        log.info("[物流信息] --- |快递鸟| ---:{}", trajectoryPushRequest);
        // 转换内部数据
        this.handler(trajectoryPushRequest);

        log.info("[物流信息] --- |快递鸟| ---数据转换成功");


        if (IterUtil.isNotEmpty(trajectoryPushRequest.data())) {
            TrajectoryCallbackResponse response = new TrajectoryCallbackResponse(
                    trajectoryPushRequest.eBusinessId(),
                    trajectoryPushRequest.pushTime(),
                    true,
                    "推送成功"
            );
            return response.toJsonStr();
        } else {
            TrajectoryCallbackResponse response = new TrajectoryCallbackResponse(
                    trajectoryPushRequest.eBusinessId(),
                    trajectoryPushRequest.pushTime(),
                    false,
                    "推送失败"
            );
            return response.toJsonStr();
        }
    }

    /**
     * 请求
     *
     * @param requestJson 请求Json
     * @param requestType 请求接口类型
     * @return 响应
     */
    private String request(String requestJson, KdniaoRequestType requestType) {
        final String url = logisticsProperties.getKdniao().getUrl() + requestType.getApi();

        try {
            return HttpUtils.http(url)
                    .headers(createHeaders())
                    .data(createRequest(requestJson, requestType))
                    .post();
        } catch (Exception e) {
            log.error("快递鸟请求失败,请求数据:{},请求接口编码:{},请求完整地址:{}", requestJson, requestType.getCode(), url, e);
            throw new CommonException("快递鸟请求失败");
        }
    }

    private Map<String, String> createHeaders() {
        Map<String, String> headers = Maps.newHashMap();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("accept", "*/*");
        headers.put("connection", "Keep-Alive");
        return headers;
    }

    /**
     * 创建请求
     *
     * @param requestData 请求数据
     * @param requestType 请求接口
     * @return 请求参数
     */
    private Map<String, String> createRequest(String requestData, KdniaoRequestType requestType) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("EBusinessID", logisticsProperties.getKdniao().getUserId());
        params.put("RequestType", requestType.getCode());
        params.put("DataType", "2");
        params.put("DataSign", dataSign(requestData + logisticsProperties.getKdniao().getApiKey()));
        params.put("RequestData", PercentCodec.encode(requestData, StandardCharsets.UTF_8));
        log.info("快递鸟请求参数:{}", params);
        return params;
    }

    /**
     * 数据签名
     * 把(InstantQueryRequest(未编码)+ApiKey)进行MD5加密（32位小写），然后Base64编码，最后URL(utf-8)编码
     *
     * @param requestData 请求数据
     * @return 签名
     */
    private String dataSign(String requestData) throws Exception {
        String md5 = DigestUtil.md5Hex(requestData);
        String base64 = Base64.encode(md5);
        return PercentCodec.encode(base64, StandardCharsets.UTF_8);
    }


    /**
     * URL解码,快递鸟回调需要使用此方式
     *
     * @param str
     * @return
     */
    private String urlDecoder(String str) {
        String result = null;
        try {
            result = URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public void handler(Object outerData) {
        if (outerData instanceof TrajectoryCallbackRequest request) {

            List<TrajectoryCallbackRequest.Data> dataList = request.data();

            dataList.forEach(data -> {
                OrderLogisticsInfoDTO dto = OrderLogisticsInfoDTO.of(data);

                // 如果返回失败,并且redis中已经存在,不做处理
                if (!dto.isSuccess() && commonCacheOperator.exists(LogisticsConstants.Logistics_CACHE_PREFIX + dto.getOrderCode() + dto.getLogisticCode())) {
                    return;
                }

                // 将具体轨迹放入缓存,过期时间为30天
                commonCacheOperator.put(LogisticsConstants.Logistics_CACHE_PREFIX + dto.getOrderCode() + dto.getLogisticCode(), dto, 2592000);
                if (dto.isSuccess()) {

                    // 更新订单快递信息
                    OrderLogistics orderLogistics = this.orderLogisticsService.getOne(QueryWrapper.create().eq(OrderLogistics::getOrderCode, dto.getOrderCode()));
                    if (Objects.isNull(orderLogistics)) {
                        return;
                    }

                    orderLogistics.setLogisticCode(dto.getLogisticCode());
                    orderLogistics.setShipper(dto.getShipperCode());
                    orderLogistics.setState(dto.getState());
                    orderLogistics.setStateEx(dto.getStateEx());
                    orderLogistics.setLocation(dto.getLocation());
                    orderLogistics.setNextCity(dto.getNextCity());
                    orderLogistics.setDeliveryMan(dto.getDeliveryMan());
                    orderLogistics.setDeliveryManTel(dto.getDeliveryManTel());
                    this.orderLogisticsService.updateById(orderLogistics);
                }
            });
        }
    }
}
