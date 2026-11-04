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
package vip.xiaonuo.shop.modular.mallgoods.service;

import api.shop.dto.MallGoodsSearchDTO;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import org.noear.solon.core.handle.Context;
import vip.xiaonuo.shop.modular.mallgoods.entity.MallGoods;
import vip.xiaonuo.shop.modular.mallgoods.param.*;

import java.io.IOException;
import java.util.List;

/**
 * 商城-商品Service接口
 *
 * @author yuezisheng
 * @date 2025/02/20 15:25
 **/
public interface MallGoodsService extends IService<MallGoods> {

    /**
     * 获取商城-商品分页
     *
     * @author yuezisheng
     * @date 2025/02/20 15:25
     */
    Page<MallGoods> page(MallGoodsPageParam mallGoodsPageParam);

    /**
     * 添加商城-商品
     *
     * @author yuezisheng
     * @date 2025/02/20 15:25
     */
    void add(MallGoodsAddParam mallGoodsAddParam);

    /**
     * 编辑商城-商品
     *
     * @author yuezisheng
     * @date 2025/02/20 15:25
     */
    void edit(MallGoodsEditParam mallGoodsEditParam);

    /**
     * 删除商城-商品
     *
     * @author yuezisheng
     * @date 2025/02/20 15:25
     */
    void delete(List<MallGoodsIdParam> mallGoodsIdParamList);

    /**
     * 获取商城-商品详情
     *
     * @author yuezisheng
     * @date 2025/02/20 15:25
     */
    MallGoods detail(MallGoodsIdParam mallGoodsIdParam);

    /**
     * 获取商城-商品详情
     *
     * @author yuezisheng
     * @date 2025/02/20 15:25
     **/
    MallGoods queryEntity(String id);

    List<MallGoods> detailList();

    /**
     * 导出商品
     *
     * @author
     * @date
     **/
    void exportMallGoods(MallGoodsExportParam mallGoodsExportParam, Context context) throws IOException;

    /**
     * 获取商品信息list
     *
     * @author
     * @date
     **/
    List<MallGoods> mallGoodsList(MallGoodsIdAndTitleParam mallGoodsIdAndTitleParam);

    /**
     * 扣减库存
     * @param goodsId 商品Id
     * @param stock 扣减数量
     * @return 是否成功
     */
    boolean deductionMallGoodsStock(String goodsId, Integer stock);

    /**
     * 回补库存
     * @param goodsId 商品Id
     * @param stock 回补数量
     * @return 是否成功
     */
    boolean supplementGoodsStock(String goodsId, Integer stock);

    /**
     * 搜索商品
     * @param param 参数
     * @return 商品列表
     */
    Page<MallGoods> search(MallGoodsSearchDTO param);
}
