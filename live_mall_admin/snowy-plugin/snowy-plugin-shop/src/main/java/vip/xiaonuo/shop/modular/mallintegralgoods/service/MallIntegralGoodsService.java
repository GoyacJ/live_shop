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
package vip.xiaonuo.shop.modular.mallintegralgoods.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.shop.modular.mallintegralgoods.entity.MallIntegralGoods;
import vip.xiaonuo.shop.modular.mallintegralgoods.param.MallIntegralGoodsAddParam;
import vip.xiaonuo.shop.modular.mallintegralgoods.param.MallIntegralGoodsEditParam;
import vip.xiaonuo.shop.modular.mallintegralgoods.param.MallIntegralGoodsIdParam;
import vip.xiaonuo.shop.modular.mallintegralgoods.param.MallIntegralGoodsPageParam;

import java.util.List;

/**
 * 积分商城Service接口
 *
 * @author 陈金坤
 * @date  2025/04/08 14:04
 **/
public interface MallIntegralGoodsService extends IService<MallIntegralGoods> {

    /**
     * 获取积分商城分页
     *
     * @author 陈金坤
     * @date  2025/04/08 14:04
     */
    Page<MallIntegralGoods> page(MallIntegralGoodsPageParam mallIntegralGoodsPageParam);

    /**
     * 添加积分商城
     *
     * @author 陈金坤
     * @date  2025/04/08 14:04
     */
    void add(MallIntegralGoodsAddParam mallIntegralGoodsAddParam);

    /**
     * 编辑积分商城
     *
     * @author 陈金坤
     * @date  2025/04/08 14:04
     */
    void edit(MallIntegralGoodsEditParam mallIntegralGoodsEditParam);

    /**
     * 删除积分商城
     *
     * @author 陈金坤
     * @date  2025/04/08 14:04
     */
    void delete(List<MallIntegralGoodsIdParam> mallIntegralGoodsIdParamList);

    /**
     * 获取积分商城详情
     *
     * @author 陈金坤
     * @date  2025/04/08 14:04
     */
    MallIntegralGoods detail(MallIntegralGoodsIdParam mallIntegralGoodsIdParam);

    /**
     * 获取积分商城详情
     *
     * @author 陈金坤
     * @date  2025/04/08 14:04
     **/
    MallIntegralGoods queryEntity(String id);

    /**
     * 扣减库存
     * @param goodsId 商品Id
     * @param stock 扣减数量
     * @return 是否成功
     */
    boolean deductionMallGoodsStock(String goodsId, Integer stock);
}
