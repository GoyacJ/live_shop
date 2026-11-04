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
package vip.xiaonuo.shop.modular.mallgoodsevaluate.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.shop.modular.mallgoods.entity.MallGoods;
import vip.xiaonuo.shop.modular.mallgoods.param.MallGoodsIdParam;
import vip.xiaonuo.shop.modular.mallgoods.param.MallGoodsPageParam;
import vip.xiaonuo.shop.modular.mallgoodsChange.entity.MallGoodsChange;
import vip.xiaonuo.shop.modular.mallgoodsChange.param.MallGoodsChangeAddParam;
import vip.xiaonuo.shop.modular.mallgoodsevaluate.entity.MallGoodsEvaluate;
import vip.xiaonuo.shop.modular.mallgoodsevaluate.param.*;
import vip.xiaonuo.shop.modular.mallgoodsevaluate.result.MallGoodsEvaluateResult;

import java.util.List;

/**
 * 商城-商品评价Service接口
 *
 **/
public interface MallGoodsEvaluateService extends IService<MallGoodsEvaluate> {

    /**
     * 商品评价信息分页
     *
     */
    Page<MallGoodsEvaluateResult> page(MallGoodsEvaluatePageParam mallGoodsEvaluatePageParam);

    /**
     * 获取商品评价信息List
     *
     * @author
     * @date
     **/
    List<MallGoodsEvaluate> mallGoodsEvaluateList(MallGoodsIdForEvaluateParam mallGoodsIdForEvaluateParam);

    /**
     * 获取商品评价信息最新详情
     *
     * @author
     * @date
     **/
    MallGoodsEvaluate mallGoodsEvaluateOneDetail(MallGoodsIdForEvaluateParam mallGoodsIdForEvaluateParam);

    /**
     * 新增商品评价信息
     *
     * @author
     * @date
     **/
    void mallGoodsEvaluateAdd(MallGoodsEvaluateAddParam mallGoodsEvaluateAddParam);

    /**
     * 修改商品评价信息
     *
     * @author
     * @date
     **/
    void mallGoodsEvaluateEdit(MallGoodsEvaluateEditParam mallGoodsEvaluateEditParam);

    /**
     * 删除商品评价信息
     *
     */
    void delete(List<MallGoodsEvaluateIdParam> mallGoodsEvaluateIdParamList);

    /**
     * 显示评价
     *
     **/
    void enablestatus(MallGoodsEvaluateIdParam mallGoodsEvaluateIdParam);

    /**
     * 隐藏评价
     *
     **/
    void disablestatus(MallGoodsEvaluateIdParam mallGoodsEvaluateIdParam);

}
