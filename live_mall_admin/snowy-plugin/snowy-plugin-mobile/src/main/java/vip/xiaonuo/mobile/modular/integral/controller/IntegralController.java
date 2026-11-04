package vip.xiaonuo.mobile.modular.integral.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.manage.modular.userbalancelog.param.UserIntegralChangePageParam;
import vip.xiaonuo.mobile.modular.integral.dto.SharingLinksDTO;
import vip.xiaonuo.mobile.modular.integral.service.IntegralService;
import vip.xiaonuo.mobile.modular.manage.result.UserIntegralChangeVO;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/28 18:55
 */
@Api(tags = "积分控制器")
@Controller
@Valid
@SaIgnore
@Mapping("mapi/integral")
public class IntegralController {

    @Inject
    private IntegralService integralService;

    @ApiOperation("获取app分享海报")
    @Get
    @Mapping("sharing-links")
    public CommonResult<String> sharingLinks(SharingLinksDTO dto) {
        return CommonResult.data(integralService.sharingLinks(dto));
    }

    /**
     * 获取平台-用户余额变更记录分页
     *
     */
    @ApiOperation("获取平台-用户积分变更记录分页")
    @Get
    @Mapping("user-integral-change-page")
    public CommonResult<Page<UserIntegralChangeVO>> page(UserIntegralChangePageParam param) {
        return CommonResult.data(integralService.page(param));
    }
}
