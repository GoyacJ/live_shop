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
package vip.xiaonuo.live.modular.shoppingcar.service.impl;

import api.order.MallOrderApi;
import api.order.dto.CreateOrderDTO;
import api.shop.MallGoodsApi;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.enums.OrderTypeMapperEnum;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.live.api.live.dto.CreateShoppingCarOrderDTO;
import vip.xiaonuo.live.api.openim.OpenImApi;
import vip.xiaonuo.live.api.openim.dto.OpenImErrorResponseDTO;
import vip.xiaonuo.live.api.openim.dto.OpenImGroupInfoDTO;
import vip.xiaonuo.live.api.openim.dto.OpenImGroupUpdateRequestDTO;
import vip.xiaonuo.live.modular.livebiz.entity.LiveBiz;
import vip.xiaonuo.live.modular.shoppingcar.dto.GroupExtDTO;
import vip.xiaonuo.live.modular.shoppingcar.entity.ShoppingCar;
import vip.xiaonuo.live.modular.shoppingcar.mapper.ShoppingCarMapper;
import vip.xiaonuo.live.modular.shoppingcar.param.*;
import vip.xiaonuo.live.modular.shoppingcar.service.ShoppingCarService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 直播间购物车表Service接口实现类
 *
 * @author lgq
 * @date 2025/03/04 14:17
 **/
@Slf4j
@Component
public class ShoppingCarServiceImpl extends ServiceImpl<ShoppingCarMapper, ShoppingCar> implements ShoppingCarService {

    @Inject
    private MallGoodsApi mallGoodsApi;

    @Inject
    private ShoppingCarMapper shoppingCarMapper;

    @Inject
    private CommonCacheOperator commonCacheOperator;

    @Inject
    private MallOrderApi mallOrderApi;

    @Inject
    private OpenImApi openImApi;

    @Override
    public Page<ShoppingCar> page(ShoppingCarPageParam shoppingCarPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
//        if(ObjectUtil.isNotEmpty(shoppingCarPageParam.getShoppingId())) {
//            queryWrapper.eq(ShoppingCar::getShoppingId, shoppingCarPageParam.getShoppingId());
//        }
        if (ObjectUtil.isNotEmpty(shoppingCarPageParam.getLiveId())) {
            queryWrapper.eq(ShoppingCar::getLiveId, shoppingCarPageParam.getLiveId());
        }
        if (ObjectUtil.isNotEmpty(shoppingCarPageParam.getGoodsId())) {
            queryWrapper.eq(ShoppingCar::getGoodsId, shoppingCarPageParam.getGoodsId());
        }

        if (ObjectUtil.isNotEmpty(shoppingCarPageParam.getGoodsName())) {
            queryWrapper.eq("mall_goods.title", shoppingCarPageParam.getGoodsName());
        }

        queryWrapper.select("live_biz.main_title as live_name", "mall_goods.title as goodsName", "live_shopping_car.*");
        queryWrapper.innerJoin(LiveBiz.class).on(ShoppingCar::getLiveId, LiveBiz::getId);
        queryWrapper.innerJoin("mall_goods").on("live_shopping_car.goods_id = mall_goods.id");
//        if(ObjectUtil.isNotEmpty(shoppingCarPageParam.getCommodityId())) {
//            queryWrapper.eq(ShoppingCar::getCommodityId, shoppingCarPageParam.getCommodityId());
//        }
        if (ObjectUtil.isAllNotEmpty(shoppingCarPageParam.getSortField(), shoppingCarPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(shoppingCarPageParam.getSortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(shoppingCarPageParam.getSortField()), shoppingCarPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(ShoppingCar::getSort, false);
        }
        Page<ShoppingCar> page = this.page(CommonPageRequest.defaultPage(), queryWrapper);
        page.getRecords().forEach(p -> {
            GroupExtDTO.Tips tips = this.commonCacheOperator.getCache("shop:yellow-bike-second:" + p.getId(), GroupExtDTO.Tips.class);
            if (Objects.nonNull(tips)) {
                p.setYellowBikeSecond(tips.getYellowBikeSecond());
            }
        });
        return page;
    }

    @Tran
    @Override
    public void add(ShoppingCarAddParam shoppingCarAddParam) {
        // 扣减库存
        if (!deductionGoodsStock(shoppingCarAddParam.getGoodsId(), shoppingCarAddParam.getQuantity())) {
            throw new CommonException("库存不足，无法添加");
        }
        boolean exists = this.exists(new QueryWrapper().eq(ShoppingCar::getLiveId, shoppingCarAddParam.getLiveId()).eq(ShoppingCar::getYellowBikeState, 0));
        if (exists) {
            throw new CommonException("当前直播间购物车已存在小黄车展示商品");
        }
        ShoppingCar shoppingCar = BeanUtil.toBean(shoppingCarAddParam, ShoppingCar.class);
        this.save(shoppingCar);
    }

    /**
     * 扣减商品库存
     * 增加购物车则从商城商品扣减库存
     *
     * @param goodsId  商品Id
     * @param quantity 扣减数量
     * @return 结果
     */
    @SuppressWarnings("all")
    private boolean deductionGoodsStock(String goodsId, Integer quantity) {
        return mallGoodsApi.deductionMallGoodsStock(goodsId, quantity);
    }

    @Tran
    @Override
    public void edit(ShoppingCarEditParam shoppingCarEditParam) {
        // 回补库存
        if (!supplementGoodsStock(shoppingCarEditParam.getId())) {
            throw new CommonException("库存异常");
        }

        // 扣减库存
        if (!deductionGoodsStock(shoppingCarEditParam.getGoodsId(), shoppingCarEditParam.getQuantity())) {
            throw new CommonException("库存不足，无法添加");
        }
        boolean exists = this.exists(new QueryWrapper().eq(ShoppingCar::getLiveId, shoppingCarEditParam.getLiveId()).eq(ShoppingCar::getYellowBikeState, 0));
        if (exists) {
            throw new CommonException("当前直播间购物车已存在小黄车展示商品");
        }
        ShoppingCar shoppingCar = this.queryEntity(shoppingCarEditParam.getId());
        BeanUtil.copyProperties(shoppingCarEditParam, shoppingCar);
        this.updateById(shoppingCar);
    }

    @Tran
    @Override
    public void delete(List<ShoppingCarIdParam> shoppingCarIdParamList) {
        // 回补库存
        if (shoppingCarIdParamList.stream().allMatch(s -> supplementGoodsStock(s.getId()))) {
            // 执行删除
            this.removeByIds(CollStreamUtil.toList(shoppingCarIdParamList, ShoppingCarIdParam::getId));
        }
    }

    /**
     * 回补库存
     *
     * @param id 购物车Id
     * @return 结果
     */
    private boolean supplementGoodsStock(String id) {
        ShoppingCar shoppingCar = this.getById(id);
        if (Objects.isNull(shoppingCar)) {
            return false;
        }
        return mallGoodsApi.supplementGoodsStock(shoppingCar.getGoodsId(), shoppingCar.getQuantity());
    }

    @Override
    public ShoppingCar detail(ShoppingCarIdParam shoppingCarIdParam) {
        return this.queryEntity(shoppingCarIdParam.getId());
    }

    @Override
    public ShoppingCar queryEntity(String id) {
        ShoppingCar shoppingCar = this.getById(id);
        // if(ObjectUtil.isEmpty(shoppingCar)) {
        //     throw new CommonException("直播间购物车表不存在，id值为：{}", id);
        // }
        return shoppingCar;
    }

    /**
     * 扣减购物车库存
     * 分布式锁+乐观锁实现
     * TODO: 后期可将购物车用Redis重构,因为购物车在直播结束就不存在
     *
     * @param shopCarId 购物车Id
     * @param stock     数量
     * @return
     */
    @Override
    @Tran
    public boolean deductionCarGoodsStock(String shopCarId, Integer stock) {
        String lockKey = "shop_car:stock:" + shopCarId;

        try {
            boolean locked = commonCacheOperator.tryLock(lockKey);
            if (!locked) {
                return false;
            }
            // 后续库存扣减逻辑
            return shoppingCarMapper.updateStock(shopCarId, stock) > 0;
        } catch (Exception e) {
            log.error("扣减库存异常", e);
            return false;
        } finally {
            commonCacheOperator.unLock(lockKey);
        }
    }

    @Override
    public String createShoppingCarOrder(CreateShoppingCarOrderDTO param) {
        ShoppingCar shoppingCar = this.getOne(QueryWrapper.create().eq(ShoppingCar::getLiveId, param.sourceId())
                .eq(ShoppingCar::getGoodsId, param.goodsId()));
        if (Objects.isNull(shoppingCar)) {
            throw new CommonException("购物车商品不存在");
        }

        // 1. 扣减库存
        if (!deductionCarGoodsStock(shoppingCar.getId(), param.goodsNum())) {
            throw new CommonException("库存不足，无法下单");
        }

        CreateOrderDTO createParam = new CreateOrderDTO();
        createParam.setCreateUserId(param.createUserId());
        createParam.setPid(param.pid());
        createParam.setSourceId(param.sourceId());
        createParam.setRemark(param.remark());
        createParam.setGoodsId(param.goodsId());
        createParam.setGoodsNum(param.goodsNum());
        createParam.setMobile(param.mobile());
        createParam.setNickname(param.nickname());
        createParam.setProvinceName(param.provinceName());
        createParam.setCityName(param.cityName());
        createParam.setCountiesName(param.countiesName());
        createParam.setAddress(param.address());

        createParam.setOrderType(OrderTypeMapperEnum.ZBJ);

        // 获取商品价格
        BigDecimal price = shoppingCar.getPrice();

        // 计算订单总价
        BigDecimal orderTotalPrice = NumberUtil.round(NumberUtil.mul(price, param.goodsNum()), 2);
        createParam.setOrderFee(orderTotalPrice);

        // 2. 创建订单
        return mallOrderApi.createOrder(createParam);
    }

    @Override
    public boolean top(ShoppingCarChangeParam param) {
        ShoppingCar shoppingCar = this.getById(param.id());
        if (Objects.isNull(shoppingCar)) {
            return false;
        }

        String liveId = shoppingCar.getLiveId();
        List<ShoppingCar> shoppingCars = this.list(new QueryWrapper().eq(ShoppingCar::getLiveId, liveId).ne(ShoppingCar::getId, shoppingCar.getId()).orderBy(ShoppingCar::getSort, false));
        if (IterUtil.isNotEmpty(shoppingCars)) {
            ShoppingCar car = shoppingCars.get(0);

            if (shoppingCar.getSort() == null) {
                if (car.getSort() != null) {
                    shoppingCar.setSort(car.getSort() + 1);
                } else {
                    shoppingCar.setSort(1);
                }
            } else {
                if (car.getSort() != null && car.getSort() > shoppingCar.getSort()) {
                    shoppingCar.setSort(car.getSort() + 1);
                }
            }
        } else {
            if (shoppingCar.getSort() == null) {
                shoppingCar.setSort(1);
            }
        }

        String goodsId = shoppingCar.getGoodsId();
        JSONObject mallGoods = this.mallGoodsApi.getMallGoods(goodsId);

        GroupExtDTO ext = new GroupExtDTO();
        ext.setState("1");
        GroupExtDTO.Top top = BeanUtil.toBean(shoppingCar, GroupExtDTO.Top.class);
        top.setHostImage(mallGoods.getStr("hostImage"));
        top.setGoodsName(mallGoods.getStr("title"));
        ext.setTop(top);

        changeGroupInfo(shoppingCar.getLiveId(), ext);
        return this.updateById(shoppingCar);
    }

    @Override
    @Tran
    public boolean yellowBikeTips(ShoppingCarChangeParam param) {
        ShoppingCar shoppingCar = this.getById(param.id());
        if (Objects.isNull(shoppingCar)) {
            return false;
        }

        String liveId = shoppingCar.getLiveId();
        boolean exists = this.exists(new QueryWrapper().eq(ShoppingCar::getLiveId, liveId).eq(ShoppingCar::getYellowBikeState, 0));
        if (exists) {
            ShoppingCar car = this.getOne(new QueryWrapper().eq(ShoppingCar::getLiveId, liveId).eq(ShoppingCar::getYellowBikeState, 0));
            car.setYellowBikeState(1);
            car.setYellowBikeSecond(null);
            this.commonCacheOperator.remove("shop:yellow-bike-second:" + car.getId());
            this.updateById(car);
        }

        shoppingCar.setYellowBikeState(0);

        String goodsId = shoppingCar.getGoodsId();
        JSONObject mallGoods = this.mallGoodsApi.getMallGoods(goodsId);

        GroupExtDTO ext = new GroupExtDTO();
        ext.setState("2");
        GroupExtDTO.Tips tips = BeanUtil.toBean(shoppingCar, GroupExtDTO.Tips.class);
        tips.setHostImage(mallGoods.getStr("hostImage"));
        tips.setGoodsName(mallGoods.getStr("title"));
        tips.setYellowBikeSecond(param.yellowBikeSecond());
        // 设置小黄车截止时间
        tips.setYellowBikeEndTime(LocalDateTime.now().plusSeconds(param.yellowBikeSecond()));

        ext.setTips(tips);

        changeGroupInfo(shoppingCar.getLiveId(), ext);
        this.commonCacheOperator.put("shop:yellow-bike-second:" + shoppingCar.getId(), tips, param.yellowBikeSecond().intValue());
        return this.updateById(shoppingCar);
    }

    /**
     * 修改群组扩展信息
     *
     * @param liveId 直播Id
     */
    private void changeGroupInfo(String liveId, GroupExtDTO ext) {
        List<OpenImGroupInfoDTO> groupsInfo = openImApi.getGroupsInfo(Lists.newArrayList(liveId));
        if (IterUtil.isEmpty(groupsInfo)) {
            throw new CommonException("群组信息错误");
        }

        OpenImGroupInfoDTO dto = groupsInfo.get(0);
        String ex = dto.ex();
        String newEx;
        if (StringUtils.isEmpty(ex)) {
            newEx = JSONUtil.toJsonStr(ext);
        } else {
            GroupExtDTO bean = JSONUtil.toBean(ex, GroupExtDTO.class);
            bean.setState(ext.getState());
            if (StringUtils.equals("1", ext.getState())) {
                bean.setTop(ext.getTop());
            } else if (StringUtils.equals("2", ext.getState())) {
                bean.setTips(ext.getTips());
            }
            newEx = JSONUtil.toJsonStr(bean);
        }

        OpenImGroupUpdateRequestDTO res = OpenImGroupUpdateRequestDTO.of(liveId, newEx);
        OpenImErrorResponseDTO resp = openImApi.updateGroupInfo(res);
        if (resp.errCode() != 0) {
            throw new CommonException(resp.errMsg() + resp.errDlt());
        }
    }
}
