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
package vip.xiaonuo.shop.modular.mallpaymentconfig.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.shop.modular.mallorder.entity.MallOrder;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderAddParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderEditParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderIdParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderPageParam;
import vip.xiaonuo.shop.modular.mallpaymentconfig.entity.MallPaymentConfig;
import vip.xiaonuo.shop.modular.mallpaymentconfig.param.MallPaymentConfigAddParam;
import vip.xiaonuo.shop.modular.mallpaymentconfig.param.MallPaymentConfigEditParam;
import vip.xiaonuo.shop.modular.mallpaymentconfig.param.MallPaymentConfigIdParam;
import vip.xiaonuo.shop.modular.mallpaymentconfig.param.MallPaymentConfigPageParam;

import java.util.List;

/**
 * 商城-支付配置Service接口
 *
 **/
public interface MallPaymentConfigService extends IService<MallPaymentConfig> {

    /**
     * 获取商城-支付配置分页
     *
     */
    Page<MallPaymentConfig> page(MallPaymentConfigPageParam mallPaymentConfigPageParam);

    /**
     * 添加商城-添加支付配置
     *
     */
    void add(MallPaymentConfigAddParam mallPaymentConfigAddParam);

    /**
     * 编辑商城-编辑支付配置
     *
     */
    void edit(MallPaymentConfigEditParam mallPaymentConfigEditParam);

    /**
     * 删除商城-支付配置
     *
     */
    void delete(List<MallPaymentConfigIdParam> mallPaymentConfigIdParamList);

    /**
     * 获取商城-支付配置详情
     *
     */
    MallPaymentConfig detail(MallPaymentConfigIdParam mallPaymentConfigIdParam);

}
