package vip.xiaonuo.distribution.modular.record.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.dto.CommonPageParam;
import vip.xiaonuo.distribution.modular.record.entity.DistributionRecord;
import vip.xiaonuo.distribution.modular.record.service.DistributionRecordService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/30 17:36
 */
@Slf4j
@Api(tags = "推广记录控制器")
@Controller
@Valid
@Mapping("/distribution/record")
public class DistributionRecordController {

    @Inject
    private DistributionRecordService distributionRecordService;

    @ApiOperation("分页")
    @SaCheckPermission("/distribution/record/page")
    @Get
    @Mapping("/page")
    public CommonResult<Page<DistributionRecord>> page(CommonPageParam param) {
        return CommonResult.data(distributionRecordService.page(param));
    }

    @ApiOperation("详情")
    @SaCheckPermission("/distribution/record/detail")
    @Get
    @Mapping("/detail")
    public CommonResult<DistributionRecord> detail(String id) {
        return CommonResult.data(distributionRecordService.detail(id));
    }
}
