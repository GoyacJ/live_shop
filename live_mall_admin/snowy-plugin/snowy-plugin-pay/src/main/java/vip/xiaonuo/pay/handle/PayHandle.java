package vip.xiaonuo.pay.handle;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.common.enums.OrderTypeMapperEnum;
import vip.xiaonuo.pay.dto.WechatPayCustomResultDTO;
import vip.xiaonuo.pay.entity.CreateH5PayOrderDTO;
import vip.xiaonuo.pay.entity.CreatePayOrderVo;
import vip.xiaonuo.pay.entity.PayRequestVo;
import vip.xiaonuo.pay.entity.PayTypeEnums;
import vip.xiaonuo.pay.service.H5PayService;
import vip.xiaonuo.pay.service.impl.WxPayServiceImpl;

import java.util.Map;

@Slf4j
//@Component
public abstract class PayHandle {

    @Inject
    private WxPayServiceImpl wxPayService;

    @Inject
    private H5PayService h5PayService;

    /**
     * 获取支付分类
     *
     * @author GMQ
     * @date 2022/3/4 16:38
     **/
    public abstract OrderTypeMapperEnum getPayClassify();


    /**
     * 创建支付
     * @param userId    用户id
     * @param outTradeNo    外部订单号
     * @param attach    业务参数
     * @return
     */
    public Map<String, String> pay(String userId, String outTradeNo, String attach, PayRequestVo payRequestVo){
        log.info("{}支付开始:用户{},支付方式{}", this.getPayClassify().getDesc(), userId, payRequestVo.getPayType());
        if (PayTypeEnums.WX.getCode().equals(payRequestVo.getPayType())){
            return this.wxPayLoading(userId,outTradeNo,attach,payRequestVo);
        }
        if (PayTypeEnums.WX_H5.getCode().equals(payRequestVo.getPayType())){
            return this.wxH5PayLoading(userId,outTradeNo,attach,payRequestVo);
        }


        throw new RuntimeException("未匹配到支付类型");
    }

    public Map<String, String> wxPayLoading(String userId,String outTradeNo,String attach, PayRequestVo payRequestVo){
        // 创建预付款订单
        CreatePayOrderVo vo = new CreatePayOrderVo();
        vo.setTotal(payRequestVo.getTotalPrice());
        vo.setDescription(this.getPayClassify().getDesc());
        vo.setAttach(attach);
        vo.setOutTradeNo(outTradeNo);
        log.info("微信支付预生成->生成预支付订单:{}", JSONUtil.toJsonStr(vo));

        return wxPayService.createPayOrder(vo);
    }

    public Map<String, String> wxH5PayLoading(String userId,String outTradeNo,String attach, PayRequestVo payRequestVo){
        // 创建预付款订单
        CreateH5PayOrderDTO vo = new CreateH5PayOrderDTO(
                outTradeNo,
                this.getPayClassify().getDesc(),
                attach,
                payRequestVo.getTotalPrice()
        );
        log.info("微信H5支付预生成->生成预支付订单:{}", JSONUtil.toJsonStr(vo));

        return h5PayService.h5Pay(vo);
    }

    public WechatPayCustomResultDTO queryCustomOrder(String outTradeNo){
        return h5PayService.queryCustomOrder(outTradeNo);
    }


}
