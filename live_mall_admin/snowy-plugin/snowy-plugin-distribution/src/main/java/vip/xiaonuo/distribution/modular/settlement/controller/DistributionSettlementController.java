package vip.xiaonuo.distribution.modular.settlement.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.dto.CommonPageParam;
import vip.xiaonuo.distribution.api.enums.SettlementType;
import vip.xiaonuo.distribution.modular.distribution.service.DistributionService;
import vip.xiaonuo.distribution.modular.settlement.entity.DistributionSettlement;
import vip.xiaonuo.distribution.modular.settlement.params.ManualSettlementParam;
import vip.xiaonuo.distribution.modular.settlement.service.DistributionSettlementService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/30 17:35
 */
@Slf4j
@Api(tags = "推广结算控制器")
@Controller
@Valid
@Mapping("/distribution/settlement")
public class DistributionSettlementController {

    @Inject
    private DistributionSettlementService distributionSettlementService;

    @Inject
    private DistributionService distributionService;

    @ApiOperation("分页")
    @SaCheckPermission("/distribution/settlement/page")
    @Get
    @Mapping("/page")
    public CommonResult<Page<DistributionSettlement>> page(CommonPageParam param) {
        return CommonResult.data(distributionSettlementService.page(param));
    }

    @ApiOperation("详情")
    @SaCheckPermission("/distribution/settlement/detail")
    @Get
    @Mapping("/detail")
    public CommonResult<DistributionSettlement> detail(String id) {
        return CommonResult.data(distributionSettlementService.detail(id));
    }

    @ApiOperation("手工结算")
    @CommonLog("手工结算")
    @SaCheckPermission("/distribution/settlement/manual-settlement")
    @Post
    @Mapping("/manual-settlement")
    public CommonResult<Void> manualSettlement(ManualSettlementParam param) {
        DistributionSettlement detail = distributionSettlementService.detail(param.id());
        detail.setSettlementType(SettlementType.MANUAL);
        distributionService.settlement(detail);
        return CommonResult.ok();
    }
}
