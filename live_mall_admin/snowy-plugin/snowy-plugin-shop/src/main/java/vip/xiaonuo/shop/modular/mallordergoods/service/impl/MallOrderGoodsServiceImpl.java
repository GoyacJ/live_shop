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
package vip.xiaonuo.shop.modular.mallordergoods.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.shop.modular.mallgoods.entity.MallGoods;
import vip.xiaonuo.shop.modular.mallgoods.service.MallGoodsService;
import vip.xiaonuo.shop.modular.mallintegralgoods.entity.MallIntegralGoods;
import vip.xiaonuo.shop.modular.mallintegralgoods.service.MallIntegralGoodsService;
import vip.xiaonuo.shop.modular.mallordergoods.entity.MallOrderGoods;
import vip.xiaonuo.shop.modular.mallordergoods.mapper.MallOrderGoodsMapper;
import vip.xiaonuo.shop.modular.mallordergoods.param.*;
import vip.xiaonuo.shop.modular.mallordergoods.service.MallOrderGoodsService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * 商城-订单商品关联Service接口实现类
 **/
@Component
public class MallOrderGoodsServiceImpl extends ServiceImpl<MallOrderGoodsMapper, MallOrderGoods> implements MallOrderGoodsService {

    @Inject
    private MallGoodsService mallGoodsService;

    @Inject
    private MallIntegralGoodsService mallIntegralGoodsService;

    @Override
    public Page<MallOrderGoods> page(MallOrderGoodsPageParam mallOrderGoodsPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Tran
    @Override
    public void add(MallOrderGoodsAddParam mallOrderGoodsAddParam) {
        if (ObjectUtil.isEmpty(mallOrderGoodsAddParam)) {
            throw new CommonException("商城-订单商品关联信息不能为空");
        }
        try {
            MallOrderGoods mallOrderGoods = BeanUtil.toBean(mallOrderGoodsAddParam, MallOrderGoods.class);
            this.save(mallOrderGoods);
        } catch (Exception e) {
            throw new CommonException("商城-订单商品关联信息添加失败");
        }
    }

    @Override
    public void edit(MallOrderGoodsEditParam mallOrderGoodsEditParam) {
        MallOrderGoods MallOrderGoods = this.getById(mallOrderGoodsEditParam.getId());
        if (ObjectUtil.isEmpty(MallOrderGoods)) {
            throw new CommonException("商城-订单商品关联信息不存在，id值为：{}", mallOrderGoodsEditParam.getId());
        }
        try {
            BeanUtil.copyProperties(mallOrderGoodsEditParam, MallOrderGoods);
            this.updateById(MallOrderGoods);
        } catch (Exception e) {
            throw new CommonException("商城-订单商品关联信息修改失败");
        }
    }

    @Tran
    @Override
    public void delete(List<MallOrderGoodsIdParam> mallOrderGoodsIdParamList) {
        try {
            // 执行删除
            this.removeByIds(CollStreamUtil.toList(mallOrderGoodsIdParamList, MallOrderGoodsIdParam::getId));
        } catch (Exception e) {
            throw new CommonException("商城-订单商品关联信息删除失败");
        }
    }

    @Override
    public MallOrderGoods detail(MallOrderGoodsIdParam mallOrderGoodsIdParam) {
        MallOrderGoods mallOrderGoods = this.getById(mallOrderGoodsIdParam.getId());
        if (ObjectUtil.isEmpty(mallOrderGoods)) {
            throw new CommonException("商城-订单商品关联信息不存在，id值为：{}", mallOrderGoodsIdParam.getId());
        }
        return mallOrderGoods;
    }

    @Override
    public List<MallOrderGoods> getOrderGoodsListByOrderId(MallOrderGoodsIdsParam mallOrderGoodsIdParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (ObjectUtil.isNotEmpty(mallOrderGoodsIdParam.getOrderId())) {
            queryWrapper.eq("order_id", mallOrderGoodsIdParam.getOrderId());
        }
        List<MallOrderGoods> list = this.list(queryWrapper);
        return list;
    }

    @Override
    public List<MallOrderGoods> getOrderGoodsListByGoodsId(MallOrderGoodsIdsParam mallOrderGoodsIdParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (ObjectUtil.isNotEmpty(mallOrderGoodsIdParam.getGoodsId())) {
            queryWrapper.eq("goods_id", mallOrderGoodsIdParam.getGoodsId());
        }
        List<MallOrderGoods> list = this.list(queryWrapper);
        return list;
    }

    @Override
    public void createMallOrderGoods(String userId, String orderId, Integer goodsNum, String goodsId) {
        MallGoods mallGoods = mallGoodsService.getById(goodsId);
        if (Objects.isNull(mallGoods)) {
            throw new CommonException("商城-商品不存在");
        }

        MallOrderGoods mallOrderGoods = new MallOrderGoods();
        mallOrderGoods.setId(IdUtil.getSnowflakeNextIdStr());
        mallOrderGoods.setCreateUser(userId);
        mallOrderGoods.setOrderId(orderId);
        mallOrderGoods.setGoodsId(mallGoods.getId());
        mallOrderGoods.setGoodsCount(String.valueOf(goodsNum));
        mallOrderGoods.setGoodsTitle(mallGoods.getTitle());
        mallOrderGoods.setGoodsPrice(BigDecimal.valueOf(mallGoods.getPrice()));
        mallOrderGoods.setGoodsCover(mallGoods.getHostImage());
        mallOrderGoods.setGiftPoints(mallGoods.getGiftPoints());
        this.save(mallOrderGoods);
    }

    @Override
    public void createMallOrderIntegralGoods(String userId, String orderId, Integer goodsNum, String goodsId) {
        MallIntegralGoods mallGoods = mallIntegralGoodsService.getById(goodsId);
        if (Objects.isNull(mallGoods)) {
            throw new CommonException("商城-商品不存在");
        }

        MallOrderGoods mallOrderGoods = new MallOrderGoods();
        mallOrderGoods.setId(IdUtil.getSnowflakeNextIdStr());
        mallOrderGoods.setCreateUser(userId);
        mallOrderGoods.setOrderId(orderId);
        mallOrderGoods.setGoodsId(mallGoods.getId());
        mallOrderGoods.setGoodsCount(String.valueOf(goodsNum));
        mallOrderGoods.setGoodsTitle(mallGoods.getTitle());
        mallOrderGoods.setGoodsPrice(BigDecimal.valueOf(mallGoods.getIntegral()));
        mallOrderGoods.setGoodsCover(mallGoods.getCover());
        this.save(mallOrderGoods);
    }
}
