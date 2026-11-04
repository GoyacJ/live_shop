package vip.xiaonuo.mobile.modular.order.service.impl;

import api.order.MallOrderApi;
import api.order.dto.CreateIntegralOrderDTO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.integral.api.IntegralApi;
import vip.xiaonuo.manage.modular.useraddress.entity.UserAddress;
import vip.xiaonuo.manage.modular.userbalancelog.mapper.UserBalanceLogMapper;
import vip.xiaonuo.manage.modular.userinfo.entity.UserInfo;
import vip.xiaonuo.manage.modular.userinfo.mapper.UserInfoMapper;
import vip.xiaonuo.mobile.modular.manage.result.UserAddressResult;
import vip.xiaonuo.mobile.modular.manage.service.UserAddressService;
import vip.xiaonuo.mobile.modular.order.result.IntegralGoodsDetailResult;
import vip.xiaonuo.mobile.modular.order.result.IntegralGoodsListResult;
import vip.xiaonuo.shop.modular.mallintegralgoods.entity.MallIntegralGoods;
import vip.xiaonuo.shop.modular.mallintegralgoods.mapper.MallIntegralGoodsMapper;
import vip.xiaonuo.shop.modular.mallorder.entity.MallOrder;
import vip.xiaonuo.shop.modular.mallorder.mapper.MallOrderMapper;
import vip.xiaonuo.shop.modular.mallordergoods.entity.MallOrderGoods;
import vip.xiaonuo.shop.modular.mallordergoods.mapper.MallOrderGoodsMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: shaojiang
 * CreateTime: 2025/4/7 16:37
 * Description: 积分商城服务实现
 */
@Component
public class IntegralGoodsServiceImpl {
    @Inject
    private MallIntegralGoodsMapper integralGoodsMapper;
    @Inject
    private UserAddressService userAddressService;
    @Inject
    private UserInfoMapper userInfoMapper;
    @Inject
    private MallOrderMapper mallOrderMapper;
    @Inject
    private UserBalanceLogMapper balanceLogMapper;
    @Inject
    private MallOrderGoodsMapper orderGoodsMapper;

    @Inject
    private MallOrderApi mallOrderApi;

    @Inject
    private IntegralApi integralApi;

    /// 积分商品列表
    public Page<IntegralGoodsListResult> getIntegralGoodsList() {
        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.eq("status", "1");

        Page<MallIntegralGoods> paginate = integralGoodsMapper.paginate(CommonPageRequest.defaultPage(), queryWrapper);
        Page<IntegralGoodsListResult> page = paginate.map(mallIntegralGoods -> {
            IntegralGoodsListResult integralGoodsListResult = new IntegralGoodsListResult();
            integralGoodsListResult.setId(mallIntegralGoods.getId());
            integralGoodsListResult.setTitle(mallIntegralGoods.getTitle());
            integralGoodsListResult.setCover(mallIntegralGoods.getCover());
            integralGoodsListResult.setMarketPrice(Convert.toBigDecimal(mallIntegralGoods.getMarketPrice()));
            integralGoodsListResult.setIntegral(Convert.toLong(mallIntegralGoods.getIntegral()));
            return integralGoodsListResult;
        });
        page.setPageNumber(paginate.getPageNumber());
        page.setPageSize(paginate.getPageSize());
        page.setTotalPage(paginate.getTotalPage());
        page.setTotalRow(paginate.getTotalRow());
        return page;
    }

    /// 积分商品详情
    public JSONObject getIntegralGoodsDetail(String id) {
        MallIntegralGoods mallIntegralGoods = integralGoodsMapper.selectOneById(id);
        if (ObjectUtil.isEmpty(mallIntegralGoods)) {
            throw new CommonException("积分商品不存在");
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("default_address", userAddressService.getDefaultAddress());
        jsonObject.set("goods_detail", BeanUtil.toBean(mallIntegralGoods, IntegralGoodsDetailResult.class));

        return jsonObject;
    }

    /**
     * 积分商品兑换
     *
     * @param integralGoodsId
     * @param addressId
     * @param userId
     */
    public void exchangeGoods(String integralGoodsId, String addressId, String userId) {
        MallIntegralGoods mallIntegralGoods = integralGoodsMapper.selectOneById(integralGoodsId);
        if (ObjectUtil.isEmpty(mallIntegralGoods)) {
            throw new CommonException("积分商品不存在");
        }
        UserAddressResult userAddress = userAddressService.getUserAddress(addressId);
        if (ObjectUtil.isEmpty(userAddress)) {
            throw new CommonException("收货地址不存在");
        }
        UserInfo userInfo = userInfoMapper.selectOneById(userId);
        if (ObjectUtil.isEmpty(userInfo)) {
            throw new CommonException("用户不存在");
        }

        BigDecimal integral = integralApi.getIntegralByUserId(userId);
        if (ObjectUtil.isEmpty(integral) || integral.compareTo(BigDecimal.valueOf(mallIntegralGoods.getIntegral())) < 0) {
            throw new CommonException("积分不足");
        }

        playJfOrder(
                userId,
                integralGoodsId,
                1,
                "",
                "积分兑换",
                userInfo.getPId(),
                BeanUtil.copyProperties(userAddress, UserAddress.class)
        );
    }

    /**
     * 积分兑换订单
     *
     * @param userId     下单用户Id
     * @param productId  商品Id
     * @param goodsCount 商品数量
     * @param sourceId   来源信息
     * @param remark     备注
     * @param pid        邀请人信息
     * @param consignee  收货人信息
     * @return 订单号
     */
    private String playJfOrder(String userId, String productId, Integer goodsCount, String sourceId, String remark, String pid, UserAddress consignee) {
        return mallOrderApi.createIntegralOrder(new CreateIntegralOrderDTO(
                userId,
                pid,
                sourceId,
                remark,
                productId,
                goodsCount,
                consignee.getMobile(),
                consignee.getNickname(),
                consignee.getProvinceName(),
                consignee.getCityName(),
                consignee.getCountiesName(),
                consignee.getAddress()
        ));
    }

    public Page<JSONObject> orderList(String userId) {
        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.eq(MallOrder::getCreateUser, userId);
        queryWrapper.eq(MallOrder::getOrderType,"3");
        Page<MallOrder> paginate = mallOrderMapper.paginate(CommonPageRequest.defaultPage(), queryWrapper);
        Page<JSONObject> page = null;
        // 获取订单id集合
        List<String> orderIds = paginate.getRecords().stream().map(MallOrder::getId).collect(Collectors.toList());
        if (ObjectUtil.isNotEmpty(orderIds)) {
            List<MallOrderGoods> mallOrderGoodsList = orderGoodsMapper.selectListByQuery(QueryWrapper.create().in(MallOrderGoods::getOrderId, orderIds));
            page = paginate.map(mallOrder -> {
                JSONObject jsonObject = new JSONObject();
                jsonObject.set("id", mallOrder.getId());
                jsonObject.set("orderNumber", mallOrder.getOrderNumber());
                jsonObject.set("orderFee", Convert.toLong(mallOrder.getOrderFee()));
                jsonObject.set("status", mallOrder.getStatus());
                jsonObject.set("createTime", mallOrder.getCreateTime());
                if (ObjectUtil.isNotEmpty(mallOrderGoodsList)) {
                    jsonObject.set("goods", mallOrderGoodsList.stream().filter(mallOrderGoods -> mallOrderGoods.getOrderId().equals(mallOrder.getId())).map(mallOrderGoods -> {
                        JSONObject goodsJson = new JSONObject();
                        goodsJson.set("goodsId", mallOrderGoods.getGoodsId());
                        goodsJson.set("goodsTitle", mallOrderGoods.getGoodsTitle());
                        goodsJson.set("goodsCover", mallOrderGoods.getGoodsCover());
                        goodsJson.set("goodsCount", mallOrderGoods.getGoodsCount());
                        goodsJson.set("goodsPrice", Convert.toLong(mallOrderGoods.getGoodsPrice()));
                        return goodsJson;
                    }).collect(Collectors.toList()));
                } else {
                    jsonObject.set("goods", new ArrayList<>());
                }
                return jsonObject;
            });
        }

        return page;
    }
}
