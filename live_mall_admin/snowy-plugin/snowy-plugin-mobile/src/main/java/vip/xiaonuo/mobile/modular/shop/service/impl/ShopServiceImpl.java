package vip.xiaonuo.mobile.modular.shop.service.impl;

import api.shop.MallGoodsApi;
import api.shop.MallGoodsSpecialTopicApi;
import api.shop.dto.MallGoodsSearchDTO;
import api.shop.dto.MallGoodsSearchResDTO;
import api.shop.dto.MallSpecialTopicGoodsSearchDTO;
import com.mybatisflex.core.paginate.Page;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.mobile.modular.shop.dto.ShopSearchDTO;
import vip.xiaonuo.mobile.modular.shop.service.ShopService;
import vip.xiaonuo.mobile.modular.shop.vo.ShopSearchVO;
import vip.xiaonuo.mobile.modular.shop.vo.SpecialTopicImageVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/12 17:28
 */
@Slf4j
@Component
public class ShopServiceImpl implements ShopService {

    @Inject
    private MallGoodsApi mallGoodsApi;

    @Inject
    private MallGoodsSpecialTopicApi mallGoodsSpecialTopicApi;

    @Override
    public Page<ShopSearchVO> search(ShopSearchDTO dto) {
        Page<MallGoodsSearchResDTO> search = mallGoodsApi.search(new MallGoodsSearchDTO(dto.title(), dto.sales(), dto.price(),dto.sort(), dto.current(), dto.size()));
        return search.map(ShopServiceImpl::convertShopSearchVo);
    }

    @Override
    public List<SpecialTopicImageVO> carouselImage() {
        return mallGoodsSpecialTopicApi.carouselImage().stream().map(r -> {
            SpecialTopicImageVO vo = new SpecialTopicImageVO();
            vo.setSpecialTopicId(r.getSpecialTopicId());
            vo.setSpecialTopicName(r.getSpecialTopicName());
            vo.setImg(r.getImg());
            return vo;
        }).toList();
    }

    @Override
    public List<SpecialTopicImageVO> specialTopicList() {
        return mallGoodsSpecialTopicApi.specialTopicList().stream().map(r -> {
            SpecialTopicImageVO vo = new SpecialTopicImageVO();
            vo.setSpecialTopicId(r.getSpecialTopicId());
            vo.setSpecialTopicName(r.getSpecialTopicName());
            vo.setCarousel(r.getCarousel());
            vo.setImg(r.getImg());
            return vo;
        }).toList();
    }

    @Override
    public Page<ShopSearchVO> specialTopicGoodsPage(MallSpecialTopicGoodsSearchDTO dto) {
        Page<MallGoodsSearchResDTO> search = mallGoodsSpecialTopicApi.specialTopicGoodsPage(dto);
        return search.map(ShopServiceImpl::convertShopSearchVo);
    }

    @NotNull
    private static ShopSearchVO convertShopSearchVo(MallGoodsSearchResDTO s) {
        ShopSearchVO vo = new ShopSearchVO();
        vo.setGoodsId(s.getId());
        vo.setGoodsImg(s.getHostImage());
        vo.setGoodsTitle(s.getTitle());
        vo.setGoodsPrice(BigDecimal.valueOf(s.getPrice()));
        vo.setCostPrice(BigDecimal.valueOf(s.getCostPrice()));
        return vo;
    }
}
