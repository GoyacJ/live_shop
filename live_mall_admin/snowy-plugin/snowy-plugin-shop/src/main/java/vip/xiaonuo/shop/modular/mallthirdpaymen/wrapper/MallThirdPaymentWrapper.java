/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.shop.modular.mallthirdpaymen.wrapper;

import cn.hutool.json.JSONObject;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import vip.xiaonuo.common.annotation.CommonWrapperField;
import vip.xiaonuo.common.pojo.CommonWrapperInterface;
import vip.xiaonuo.shop.modular.mallorder.entity.MallOrder;
import vip.xiaonuo.shop.modular.mallorder.service.MallOrderService;
import vip.xiaonuo.shop.modular.mallthirdpaymen.entity.MallThirdPayment;

import java.util.Optional;

/**
 * 用户管理的包装类
 *
 */
@Slf4j
public class MallThirdPaymentWrapper implements CommonWrapperInterface<MallThirdPayment> {

    private static MallOrderService mallOrderService;


    static {
        //异步订阅方式，根据bean type获取Bean（已存在或产生时，会通知回调；否则，一直不回调）
        Solon.context().getBeanAsync(MallOrderService.class, bean -> {
            mallOrderService = bean;
        });
    }

    @Override
    public JSONObject doWrap(MallThirdPayment mallThirdPayment) {
        // 后期可通过反射进行处理
        JSONObject entries = new JSONObject();
        // 订单编码 翻译
        Optional.ofNullable(mallThirdPayment.getOrderId()).ifPresent(orderId -> {
            CommonWrapperField wrapperField = null;
            try {
                wrapperField = mallThirdPayment.getClass().getDeclaredField("orderId").getAnnotation(CommonWrapperField.class);
            } catch (NoSuchFieldException e) {
                setLog(orderId);
            }
            QueryWrapper queryWrapper = QueryWrapper.create().select("order_number").eq("id", orderId);
            MallOrder mallOrder = mallOrderService.getMapper().selectOneByQuery(queryWrapper);
            if (null != mallOrder) {
                entries.set(wrapperField.ref(), mallOrder.getOrderNumber());
            }
        });
        return entries;
    }

    protected void setLog(String field) {
        log.info("字段：{}，未找到注解。", field);
    }

}
