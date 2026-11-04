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
package vip.xiaonuo.shop.modular.mallthirdpaymen.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.shop.modular.mallorder.entity.MallOrder;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderAddParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderEditParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderIdParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderPageParam;
import vip.xiaonuo.shop.modular.mallthirdpaymen.entity.MallThirdPayment;
import vip.xiaonuo.shop.modular.mallthirdpaymen.param.MallThirdPaymentAddParam;
import vip.xiaonuo.shop.modular.mallthirdpaymen.param.MallThirdPaymentEditParam;
import vip.xiaonuo.shop.modular.mallthirdpaymen.param.MallThirdPaymentIdParam;
import vip.xiaonuo.shop.modular.mallthirdpaymen.param.MallThirdPaymentPageParam;

import java.util.List;

/**
 * 商城-支付Service接口
 *
 **/
public interface MallThirdPaymentService extends IService<MallThirdPayment> {

    /**
     * 支付分页
     *
     */
    Page<MallThirdPayment> page(MallThirdPaymentPageParam mallThirdPaymentPageParam);

    /**
     * 添加支付
     *
     */
    void add(MallThirdPaymentAddParam mallThirdPaymentAddParam);

    /**
     * 编辑支付
     *
     */
    void edit(MallThirdPaymentEditParam mallThirdPaymentEditParam);

    /**
     * 删除支付
     *
     */
    void delete(List<MallThirdPaymentIdParam> mallThirdPaymentIdParamList);

    /**
     * 获取支付详情
     *
     */
    MallThirdPayment detail(MallThirdPaymentIdParam mallThirdPaymentIdParam);

}
