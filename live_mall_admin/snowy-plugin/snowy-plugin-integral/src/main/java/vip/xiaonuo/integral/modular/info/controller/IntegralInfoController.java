package vip.xiaonuo.integral.modular.info.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.dto.CommonChangeStatusParam;
import vip.xiaonuo.integral.api.dto.ChangeIntegralDTO;
import vip.xiaonuo.integral.modular.info.entity.IntegralInfo;
import vip.xiaonuo.integral.modular.info.params.AdjustIntegralParam;
import vip.xiaonuo.integral.modular.info.params.IntegralInfoPageParam;
import vip.xiaonuo.integral.modular.info.service.IntegralInfoService;
import vip.xiaonuo.integral.modular.integral.service.IntegralService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/29 16:40
 */
@Slf4j
@Api(tags = "积分信息管理控制器")
@Controller
@Valid
@Mapping("/integral/info")
public class IntegralInfoController {

    @Inject
    private IntegralInfoService integralInfoService;

    @Inject
    private IntegralService integralService;

    @ApiOperation("分页")
    @SaCheckPermission("/integral/info/page")
    @Get
    @Mapping("/page")
    public CommonResult<Page<IntegralInfo>> page(IntegralInfoPageParam param) {
        return CommonResult.data(integralInfoService.page(param));
    }

    @ApiOperation("详情")
    @SaCheckPermission("/integral/info/detail")
    @Get
    @Mapping("/detail")
    public CommonResult<IntegralInfo> detail(String id) {
        return CommonResult.data(integralInfoService.detail(id));
    }

    @ApiOperation("调整积分")
    @CommonLog("调整积分")
    @SaCheckPermission("/integral/info/adjust")
    @Post
    @Mapping("/adjust")
    public CommonResult<Void> adjust(AdjustIntegralParam param) {
        integralService.changeIntegral(ChangeIntegralDTO.adjust(param.userId(),
                param.integral(),
                param.integralCategory(),
                param.validityPeriod(),
                param.remark()));
        return CommonResult.ok();
    }

    @ApiOperation("更改状态")
    @CommonLog("更改积分状态")
    @SaCheckPermission("/integral/info/change-status")
    @Post
    @Mapping("/change-status")
    public CommonResult<Void> changeStatus(CommonChangeStatusParam param) {
        integralService.changeIntegral(ChangeIntegralDTO.changeIntegralStatus(
                param.id(),
                param.status()
        ));
        return CommonResult.ok();
    }
}
