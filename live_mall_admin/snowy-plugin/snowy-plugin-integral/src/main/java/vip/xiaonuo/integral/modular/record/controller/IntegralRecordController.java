package vip.xiaonuo.integral.modular.record.controller;

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
import vip.xiaonuo.integral.modular.record.entity.IntegralRecord;
import vip.xiaonuo.integral.modular.record.params.IntegralRecordPageParam;
import vip.xiaonuo.integral.modular.record.service.IntegralRecordService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/28 14:05
 */
@Slf4j
@Api(tags = "积分记录管理控制器")
@Controller
@Valid
@Mapping("/integral/record")
public class IntegralRecordController {

    @Inject
    private IntegralRecordService integralRecordService;

    @ApiOperation("积分记录管理分页")
    @SaCheckPermission("/integral/record/page")
    @Get
    @Mapping("/page")
    public CommonResult<Page<IntegralRecord>> page(IntegralRecordPageParam param) {
        return CommonResult.data(integralRecordService.page(param));
    }

    @ApiOperation("详情")
    @SaCheckPermission("/integral/record/detail")
    @Get
    @Mapping("/detail")
    public CommonResult<IntegralRecord> detail(String id) {
        return CommonResult.data(integralRecordService.detail(id));
    }
}
