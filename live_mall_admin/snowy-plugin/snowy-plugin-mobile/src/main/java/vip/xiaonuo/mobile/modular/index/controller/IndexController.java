package vip.xiaonuo.mobile.modular.index.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.mobile.modular.index.service.IndexService;
import vip.xiaonuo.mobile.modular.index.vo.PrivacyAgreementVO;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/12 18:02
 */
@SaIgnore
@Api(tags = "首页控制器")
@Controller
@Valid
@Mapping("/index")
public class IndexController {

    @Inject
    private IndexService indexService;

    @ApiOperation("获取用户协议和隐私政策")
    @Get
    @Mapping("/privacy-agreement")
    public CommonResult<PrivacyAgreementVO> privacyAgreement() {
        return CommonResult.data(indexService.privacyAgreement());
    }
}
