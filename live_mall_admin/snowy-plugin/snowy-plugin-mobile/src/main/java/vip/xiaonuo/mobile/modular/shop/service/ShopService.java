package vip.xiaonuo.mobile.modular.shop.service;

import api.shop.dto.MallSpecialTopicGoodsSearchDTO;
import com.mybatisflex.core.paginate.Page;
import vip.xiaonuo.mobile.modular.shop.dto.ShopSearchDTO;
import vip.xiaonuo.mobile.modular.shop.vo.SpecialTopicImageVO;
import vip.xiaonuo.mobile.modular.shop.vo.ShopSearchVO;

import java.util.List;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/12 17:28
 */
public interface ShopService {

    /**
     * 搜索
     * @param dto 条件
     * @return 结果
     */
    Page<ShopSearchVO> search(ShopSearchDTO dto);

    /**
     * 商城轮播图
     * @return 轮播图列表
     */
    List<SpecialTopicImageVO> carouselImage();

    /**
     * 专题列表
     * @return 专题列表
     */
    List<SpecialTopicImageVO> specialTopicList();

    /**
     * 专题商品列表
     * @return 专题商品列表
     */
    Page<ShopSearchVO> specialTopicGoodsPage(MallSpecialTopicGoodsSearchDTO dto);
}
