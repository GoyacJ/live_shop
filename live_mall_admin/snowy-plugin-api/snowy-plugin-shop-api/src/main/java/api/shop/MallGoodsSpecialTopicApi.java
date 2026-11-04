package api.shop;

import api.shop.dto.MallGoodsSearchResDTO;
import api.shop.dto.MallSpecialTopicGoodsSearchDTO;
import api.shop.dto.SpecialTopicImageResDTO;
import com.mybatisflex.core.paginate.Page;

import java.util.List;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/13 09:14
 */
public interface MallGoodsSpecialTopicApi {

    /**
     * 获取轮播图
     * @return 轮播图列表
     */
    List<SpecialTopicImageResDTO> carouselImage();

    /**
     * 专题列表
     * @return 专题列表
     */
    List<SpecialTopicImageResDTO> specialTopicList();

    /**
     * 专题商品列表
     * @param dto 参数
     * @return 专题商品列表
     */
    Page<MallGoodsSearchResDTO> specialTopicGoodsPage(MallSpecialTopicGoodsSearchDTO dto);
}
