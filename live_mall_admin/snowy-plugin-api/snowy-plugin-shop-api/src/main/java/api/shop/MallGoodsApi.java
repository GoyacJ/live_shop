package api.shop;

import api.shop.dto.MallGoodsDetailsDTO;
import api.shop.dto.MallGoodsSearchDTO;
import api.shop.dto.MallGoodsSearchResDTO;
import cn.hutool.json.JSONObject;
import com.mybatisflex.core.paginate.Page;

import java.util.List;

public interface MallGoodsApi {

    /**
     * 获取商品详情
     * @param productId  商品id
     * @return
     */
    JSONObject getMallGoods(String productId);

    List<JSONObject> getGoodsByIds(List<String> productIds);

    /**
     * 更新商品库存
     * @param productId
     * @return
     */
    int updateMallGoodsStock(String productId,Integer stock);

    /**
     * 扣减商城商品库存
     * @param productId 商品Id
     * @param stock 数量
     * @return 结果
     */
    boolean deductionMallGoodsStock(String productId, Integer stock);

    /**
     * 回补商城商品库存
     * @param goodsId 商品Id
     * @param stock 数量
     * @return 结果
     */
    boolean supplementGoodsStock(String goodsId, Integer stock);

    /**
     * 搜索
     * @param dto
     * @return
     */
    Page<MallGoodsSearchResDTO> search(MallGoodsSearchDTO dto);

    /**
     * 商品详情
     * @param goodsId 商品Id
     * @return 商品信息
     */
    MallGoodsDetailsDTO detail(String goodsId);
}
