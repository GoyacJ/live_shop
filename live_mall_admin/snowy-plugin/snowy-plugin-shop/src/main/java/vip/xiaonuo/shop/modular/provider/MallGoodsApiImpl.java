package vip.xiaonuo.shop.modular.provider;

import api.shop.MallGoodsApi;
import api.shop.dto.MallGoodsDetailsDTO;
import api.shop.dto.MallGoodsSearchDTO;
import api.shop.dto.MallGoodsSearchResDTO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.shop.modular.mallgoods.entity.MallGoods;
import vip.xiaonuo.shop.modular.mallgoods.param.MallGoodsIdParam;
import vip.xiaonuo.shop.modular.mallgoods.service.MallGoodsService;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MallGoodsApiImpl implements MallGoodsApi {

    @Inject
    private MallGoodsService mallGoodsService;

    @Override
    public JSONObject getMallGoods(String productId) {
        MallGoodsIdParam mallGoodsIdParam = new MallGoodsIdParam();
        mallGoodsIdParam.setId(productId);
        MallGoods mallGoods = mallGoodsService.detail(mallGoodsIdParam);
        if (ObjectUtil.isNotEmpty(mallGoods)) {
            return new JSONObject(mallGoods);
        }
        return null;
    }

    @Override
    public List<JSONObject> getGoodsByIds(List<String> productIds) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("id", productIds);
        return mallGoodsService.list(queryWrapper).stream()
                .map(item -> JSONUtil.createObj().set("id", item.getId())
                        .set("title", item.getTitle())
                        .set("cover", item.getHostImage())
                        .set("sales", item.getSales())
                        .set("stock", item.getStock()))
                .collect(Collectors.toList());
    }

    @Override
    public int updateMallGoodsStock(String productId, Integer stock) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", productId);
        MallGoods mallGoods = new MallGoods();
        mallGoods.setStock(stock);
        mallGoodsService.update(mallGoods, queryWrapper);
        return 1;
    }

    @Override
    public boolean deductionMallGoodsStock(String productId, Integer stock) {
        return this.mallGoodsService.deductionMallGoodsStock(productId, stock);
    }

    @Override
    public boolean supplementGoodsStock(String goodsId, Integer stock) {
        return this.mallGoodsService.supplementGoodsStock(goodsId, stock);
    }

    @Override
    public Page<MallGoodsSearchResDTO> search(MallGoodsSearchDTO dto) {
        return this.mallGoodsService.search(dto).map(goods -> BeanUtil.toBean(goods, MallGoodsSearchResDTO.class));
    }

    @Override
    public MallGoodsDetailsDTO detail(String goodsId) {
        MallGoods mallGoods = this.mallGoodsService.getById(goodsId);
        return BeanUtil.toBean(mallGoods, MallGoodsDetailsDTO.class);
    }
}
