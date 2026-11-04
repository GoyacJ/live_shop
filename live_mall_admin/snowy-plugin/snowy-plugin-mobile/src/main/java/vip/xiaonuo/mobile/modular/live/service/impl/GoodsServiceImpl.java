package vip.xiaonuo.mobile.modular.live.service.impl;

import api.shop.MallGoodsApi;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.mobile.modular.live.result.GoodsDetailResult;

/**
 * Author: shaojiang
 * CreateTime: 2025/4/17 18:38
 * Description:
 */
@Component
public class GoodsServiceImpl {
    @Inject
    private MallGoodsApi mallGoodsApi;

    public GoodsDetailResult getGoodsDetail(String goodsId) {
        JSONObject mallGoods = mallGoodsApi.getMallGoods(goodsId);
        if (ObjectUtil.isEmpty(mallGoods)) {
            throw new CommonException("未找到商品");
        }

        if (StrUtil.isNotEmpty(mallGoods.getStr("carouseImage"))) {
            mallGoods.set("slider", mallGoods.getStr("carouseImage").split(","));
        }

        return BeanUtil.toBean(mallGoods, GoodsDetailResult.class);
    }
}
