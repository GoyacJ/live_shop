package vip.xiaonuo.mobile.modular.order.controller;

import api.logistics.OrderLogisticsApi;
import api.logistics.dto.OrderLogisticsInfoDTO;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.google.common.collect.Lists;
import com.mybatisflex.core.paginate.Page;
import manage.UserApi;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.Context;
import org.noear.solon.data.annotation.Tran;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.mobile.modular.order.result.IntegralGoodsListResult;
import vip.xiaonuo.mobile.modular.order.service.impl.IntegralGoodsServiceImpl;

import java.util.List;


/**
 * Author: shaojiang
 * CreateTime: 2025/4/7 16:33
 * Description: 积分商城接口
 */
@Controller
@SaIgnore
@Valid
public class IntegralGoodsController {
    @Inject
    private IntegralGoodsServiceImpl integralGoodsService;
    @Inject
    private OrderLogisticsApi orderLogisticsApi;

    @Inject
    private UserApi userApi;

    @Get
    @Mapping("/mapi/auth/integral/goods/list")
    public CommonResult<Page<IntegralGoodsListResult>> list() {
        return CommonResult.data(integralGoodsService.getIntegralGoodsList());
    }

    @Get
    @Mapping("/mapi/auth/integral/goods/detail")
    public CommonResult<JSONObject> detail(String id) {
        if (StrUtil.isEmpty(id)) {
            return CommonResult.error("商品不存在");
        }
        return CommonResult.data(integralGoodsService.getIntegralGoodsDetail(id));
    }

    @Get
    @Mapping("/mapi/auth/integral/goods/logistics/info")
    public CommonResult<OrderLogisticsInfoDTO> logisticsInfo(@Param(required = true) String orderNumber) {
        return CommonResult.data(orderLogisticsApi.detail(orderNumber));
    }

    @Tran
    @Post
    @Mapping("/mapi/auth/integral/goods/buy")
    public CommonResult<String> buy(String id, String addressId, Context context) {
        if (CharSequenceUtil.isEmpty(id)) {
            return CommonResult.error("商品不存在");
        }
        if (CharSequenceUtil.isEmpty(addressId)) {
            return CommonResult.error("请选择收货地址");
        }
        Object userId = context.attr("uid");
        if (ObjectUtil.isEmpty(userId)) {
            return CommonResult.error("用户未登录");
        }
        integralGoodsService.exchangeGoods(id, addressId, Convert.toStr(userId));
        return CommonResult.ok();
    }

    @Get
    @Mapping("/mapi/auth/integral/order/list")
    public CommonResult<Page<JSONObject>> orderList(Context context) {
        Object userId = context.attr("uid");
        if (ObjectUtil.isEmpty(userId)) {
            return CommonResult.error("用户不存在");
        }
        return CommonResult.data(integralGoodsService.orderList(Convert.toStr(userId)));
    }

    @Get
    @Mapping("/mapi/auth/integral/order/buy/user/list")
    public CommonResult<List<String>> buyUserList() {
        List<String> userList = Lists.newArrayList();
        // 生成 20 个随机手机号并隐藏中间四位
        for (int i = 0; i < 20; i++) {
            String phone = generateRandomPhoneNumber();
            String maskedPhone = maskPhoneNumber(phone);
            userList.add(maskedPhone);
        }
        return CommonResult.data(userList);
    }

    // 生成一个随机手机号
    public static String generateRandomPhoneNumber() {
        // 简单生成手机号
        return "1" + RandomUtil.randomInt(3, 10) + RandomUtil.randomNumbers(9);
    }

    // 隐藏手机号的中间四位
    public static String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() == 11) {
            return phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7);
        }
        return phoneNumber; // 如果号码不符合标准返回原号码
    }
}
