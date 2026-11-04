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
package vip.xiaonuo.shop.modular.mallorder.service;

import api.order.dto.CreateIntegralOrderDTO;
import api.order.dto.CreateMallOrderDTO;
import api.order.dto.CreateOrderDTO;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import org.noear.solon.core.handle.Context;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.shop.modular.mallorder.entity.MallOrder;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderAddParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderEditParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderIdParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderPageParam;

import java.util.List;

/**
 * 商城-订单Service接口
 *
 **/
public interface MallOrderService extends IService<MallOrder> {

    /**
     * 获取商城-订单分页
     *
     */
    Page<MallOrder> page(MallOrderPageParam mallOrderPageParam);

    /**
     * 添加商城-添加订单
     *
     */
    void add(MallOrderAddParam mallOrderAddParam);

    /**
     * 创建订单
     * @param param 创建订单参数
     * @return 订单Id
     */
    String createMallOrder(@Validated CreateMallOrderDTO param);


    /**
     * 创建积分订单
     * @param dto 参数
     * @return 订单Id
     */
    String createIntegralOrder(CreateIntegralOrderDTO dto);

    /**
     * 创建订单
     * @param param 创建订单参数
     * @return 订单Id
     */
    String createOrder(@Validated CreateOrderDTO param);

    /**
     * 编辑商城-编辑订单
     *
     */
    void edit(MallOrderEditParam mallOrderEditParam);

    /**
     * 删除商城-订单
     *
     */
    void delete(List<MallOrderIdParam> MallOrderIdParamList);

    /**
     * 获取商城-订单详情
     *
     */
    MallOrder detail(MallOrderIdParam mallOrderIdParam);

    /**
     * 获取商城-
     *
     */
    List<MallOrder> mallOrderList(MallOrderIdParam mallOrderIdParam);

    /**
     * 导出订单
     */
    void exportOrders(String orderIds, Context ctx);

    /**
     * 确认收货
     */
    boolean confirmOrder(String userId, String orderId);
}
