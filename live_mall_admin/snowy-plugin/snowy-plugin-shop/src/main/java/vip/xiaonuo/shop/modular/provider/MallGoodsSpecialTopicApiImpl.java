package vip.xiaonuo.shop.modular.provider;

import api.shop.MallGoodsSpecialTopicApi;
import api.shop.dto.MallGoodsSearchResDTO;
import api.shop.dto.MallSpecialTopicGoodsSearchDTO;
import api.shop.dto.SpecialTopicImageResDTO;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.shop.modular.specialtopic.entity.MallGoodsSpecialTopic;
import vip.xiaonuo.shop.modular.specialtopic.service.MallGoodsSpecialTopicService;

import java.util.List;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/13 09:15
 */
@Slf4j
@Component
public class MallGoodsSpecialTopicApiImpl implements MallGoodsSpecialTopicApi {

    @Inject
    private MallGoodsSpecialTopicService mallGoodsSpecialTopicService;

    @Override
    public List<SpecialTopicImageResDTO> carouselImage() {
        return mallGoodsSpecialTopicService.list(new QueryWrapper().eq(MallGoodsSpecialTopic::getCarousel, 0))
                .stream()
                .map(r -> {
                    SpecialTopicImageResDTO dto = new SpecialTopicImageResDTO();
                    dto.setSpecialTopicId(r.getId());
                    dto.setSpecialTopicName(r.getName());
                    dto.setImg(r.getBackground());
                    return dto;
                }).toList();
    }

    @Override
    public List<SpecialTopicImageResDTO> specialTopicList() {
        return mallGoodsSpecialTopicService.list()
                .stream()
                .map(r -> {
                    SpecialTopicImageResDTO dto = new SpecialTopicImageResDTO();
                    dto.setSpecialTopicId(r.getId());
                    dto.setSpecialTopicName(r.getName());
                    dto.setCarousel(r.getCarousel());
                    dto.setImg(r.getBackground());
                    return dto;
                }).toList();
    }

    @Override
    public Page<MallGoodsSearchResDTO> specialTopicGoodsPage(MallSpecialTopicGoodsSearchDTO dto) {
        return mallGoodsSpecialTopicService.specialTopicGoodsPage(dto);
    }
}
