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
package vip.xiaonuo.live.modular.shoppingcar.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.live.api.live.dto.CreateShoppingCarOrderDTO;
import vip.xiaonuo.live.modular.shoppingcar.entity.ShoppingCar;
import vip.xiaonuo.live.modular.shoppingcar.param.*;

import java.util.List;

/**
 * 直播间购物车表Service接口
 *
 * @author lgq
 * @date  2025/03/04 14:17
 **/
public interface ShoppingCarService extends IService<ShoppingCar> {

    /**
     * 获取直播间购物车表分页
     *
     * @author lgq
     * @date  2025/03/04 14:17
     */
    Page<ShoppingCar> page(ShoppingCarPageParam shoppingCarPageParam);

    /**
     * 添加直播间购物车表
     *
     * @author lgq
     * @date  2025/03/04 14:17
     */
    void add(ShoppingCarAddParam shoppingCarAddParam);

    /**
     * 编辑直播间购物车表
     *
     * @author lgq
     * @date  2025/03/04 14:17
     */
    void edit(ShoppingCarEditParam shoppingCarEditParam);

    /**
     * 删除直播间购物车表
     *
     * @author lgq
     * @date  2025/03/04 14:17
     */
    void delete(List<ShoppingCarIdParam> shoppingCarIdParamList);

    /**
     * 获取直播间购物车表详情
     *
     * @author lgq
     * @date  2025/03/04 14:17
     */
    ShoppingCar detail(ShoppingCarIdParam shoppingCarIdParam);

    /**
     * 获取直播间购物车表详情
     *
     * @author lgq
     * @date  2025/03/04 14:17
     **/
    ShoppingCar queryEntity(String id);

    /**
     * 扣减购物车库存
     *
     * @param shopCarId 购物车Id
     * @param stock     数量
     * @return 结果
     */
    boolean deductionCarGoodsStock(String shopCarId, Integer stock);

    /**
     * 创建订单
     * @param param 创建订单参数
     */
    String createShoppingCarOrder(@Validated CreateShoppingCarOrderDTO param);

    /**
     * 直播间购物车商品置顶
     * @param param 参数
     * @return 结果
     */
    boolean top(ShoppingCarChangeParam param);

    /**
     * 直播间购物车商品小黄车提示
     * @param param 参数
     * @return 结果
     */
    boolean yellowBikeTips(ShoppingCarChangeParam param);
}
