package vip.xiaonuo.integral.modular.withdrawal.controller;

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
import vip.xiaonuo.integral.modular.withdrawal.entity.IntegralWithdrawalRecord;
import vip.xiaonuo.integral.modular.withdrawal.params.IntegralWithdrawalRecordPageParam;
import vip.xiaonuo.integral.modular.withdrawal.service.IntegralWithdrawalRecordService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/28 14:05
 */
@Slf4j
@Api(tags = "积分提现记录管理控制器")
@Controller
@Valid
@Mapping("/integral/withdrawal/record")
public class IntegralWithdrawalRecordController {

    @Inject
    private IntegralWithdrawalRecordService integralWithdrawalRecordService;

    @ApiOperation("积分记录管理分页")
    @SaCheckPermission("/integral/withdrawal/record/page")
    @Get
    @Mapping("/page")
    public CommonResult<Page<IntegralWithdrawalRecord>> page(IntegralWithdrawalRecordPageParam param) {
        return CommonResult.data(integralWithdrawalRecordService.page(param));
    }
}
