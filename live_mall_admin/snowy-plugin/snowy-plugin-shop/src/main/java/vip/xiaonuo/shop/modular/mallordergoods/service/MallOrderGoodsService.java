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
package vip.xiaonuo.shop.modular.mallordergoods.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.shop.modular.mallordergoods.entity.MallOrderGoods;
import vip.xiaonuo.shop.modular.mallordergoods.param.*;

import java.util.List;

/**
 * 商城-订单商品关联Service接口
 *
 **/
public interface MallOrderGoodsService extends IService<MallOrderGoods> {

    /**
     * 获取订单商品关联page
     *
     */
    Page<MallOrderGoods> page(MallOrderGoodsPageParam mallOrderGoodsPageParam);

    /**
     * 添加订单商品关联信息
     *
     */
    void add(MallOrderGoodsAddParam mallOrderGoodsAddParam);

    /**
     * 编辑订单商品关联信息
     *
     */
    void edit(MallOrderGoodsEditParam mallOrderGoodsEditParam);

    /**
     * 删除商品订单关联信息
     *
     */
    void delete(List<MallOrderGoodsIdParam> mallOrderGoodsIdParamList);

    /**
     * 获取订单商品关联信息详情
     *
     */
    MallOrderGoods detail(MallOrderGoodsIdParam mallOrderGoodsIdParam);

    /**
     * 根据订单id获取关联信息
     *
     */
    List<MallOrderGoods> getOrderGoodsListByOrderId(MallOrderGoodsIdsParam mallOrderGoodsIdsParam);

    /**
     * 根据商品id获取关联信息
     *
     */
    List<MallOrderGoods> getOrderGoodsListByGoodsId(MallOrderGoodsIdsParam mallOrderGoodsIdsParam);

    /**
     * 创建订单商品
     * @param userId 创建用户Id
     * @param orderId 订单Id
     * @param goodsNum 商品数量
     * @param goodsId 商品信息
     */
    void createMallOrderGoods(String userId, String orderId, Integer goodsNum,String goodsId);

    /**
     * 创建积分订单商品
     * @param userId 创建用户Id
     * @param orderId 订单Id
     * @param goodsNum 商品数量
     * @param goodsId 商品信息
     */
    void createMallOrderIntegralGoods(String userId, String orderId, Integer goodsNum,String goodsId);
}
