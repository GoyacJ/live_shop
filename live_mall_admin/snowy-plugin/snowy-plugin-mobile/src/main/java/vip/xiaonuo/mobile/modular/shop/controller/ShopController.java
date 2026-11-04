package vip.xiaonuo.mobile.modular.shop.controller;

import api.shop.dto.MallSpecialTopicGoodsSearchDTO;
import cn.dev33.satoken.annotation.SaIgnore;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.mobile.modular.shop.dto.ShopSearchDTO;
import vip.xiaonuo.mobile.modular.shop.service.ShopService;
import vip.xiaonuo.mobile.modular.shop.vo.ShopSearchVO;
import vip.xiaonuo.mobile.modular.shop.vo.SpecialTopicImageVO;

import java.util.List;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/12 17:27
 */
@Api(tags = "商城控制器")
@Controller
@Valid
@SaIgnore
@Mapping("mapi/shop")
public class ShopController {

    @Inject
    private ShopService shopService;

    @ApiOperation("搜索")
    @Get
    @Mapping("/search")
    public CommonResult<Page<ShopSearchVO>> search(ShopSearchDTO dto) {
        return CommonResult.data(shopService.search(dto));
    }

    @ApiOperation("商城轮播图")
    @Get
    @Mapping("/carousel-image")
    public CommonResult<List<SpecialTopicImageVO>> carouselImage() {
        return CommonResult.data(shopService.carouselImage());
    }

    @ApiOperation("专题列表")
    @Get
    @Mapping("/special-topic")
    public CommonResult<List<SpecialTopicImageVO>> specialTopicList() {
        return CommonResult.data(shopService.specialTopicList());
    }

    @ApiOperation("专题商品列表")
    @Get
    @Mapping("/special-topic-goods")
    public CommonResult<Page<ShopSearchVO>> specialTopicGoodsList(MallSpecialTopicGoodsSearchDTO dto) {
        return CommonResult.data(shopService.specialTopicGoodsPage(dto));
    }
}
