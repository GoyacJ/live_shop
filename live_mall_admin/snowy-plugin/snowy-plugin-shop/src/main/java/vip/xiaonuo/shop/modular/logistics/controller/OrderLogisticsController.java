package vip.xiaonuo.shop.modular.logistics.controller;

import api.logistics.dto.OrderLogisticsInfoDTO;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.UploadedFile;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.shop.modular.logistics.param.OrderLogisticsSettingParam;
import vip.xiaonuo.shop.modular.logistics.service.OrderLogisticsService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/27 09:57
 */
@Slf4j
@Api(tags = "订单物流信息控制器")
@Controller
@Valid
@Mapping("/shop/logistics")
public class OrderLogisticsController {

    @Inject
    private OrderLogisticsService orderLogisticsService;

    @ApiOperation("物流信息导入-发货模板")
    @CommonLog("物流信息导入-发货模板")
    @SaCheckPermission("/shop/logistics/import")
    @Post
    @Mapping("/import")
    public CommonResult<Void> importLogistics(@ApiParam(value = "文件", required = true) UploadedFile file) {
        if (orderLogisticsService.importLogistics(file)) {
            return CommonResult.ok();
        }
        return CommonResult.error();
    }

    /**
     * 设置物流信息
     */
    @ApiOperation("设置物流信息")
    @CommonLog("设置物流信息")
    @SaCheckPermission("/shop/logistics/setting")
    @Post
    @Mapping("/setting")
    public CommonResult<Void> setting(OrderLogisticsSettingParam param) {
        if (orderLogisticsService.setting(param)) {
            return CommonResult.ok();
        }
        return CommonResult.error();
    }

    @ApiOperation("物流信息详情")
    @SaCheckPermission("/shop/logistics/detail")
    @Get
    @Mapping("/detail")
    public CommonResult<OrderLogisticsInfoDTO> detail(@Param(required = true) String orderNumber) {
        return CommonResult.data(orderLogisticsService.detail(orderNumber));
    }
}
