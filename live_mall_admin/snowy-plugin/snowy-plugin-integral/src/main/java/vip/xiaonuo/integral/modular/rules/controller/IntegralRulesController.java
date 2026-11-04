package vip.xiaonuo.integral.modular.rules.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.common.pojo.dto.CommonChangeStatusParam;
import vip.xiaonuo.integral.modular.rules.entity.IntegralRules;
import vip.xiaonuo.integral.modular.rules.params.IntegralRulesAddParam;
import vip.xiaonuo.integral.modular.rules.params.IntegralRulesEditParam;
import vip.xiaonuo.integral.modular.rules.params.IntegralRulesIdParam;
import vip.xiaonuo.integral.modular.rules.params.IntegralRulesPageParam;
import vip.xiaonuo.integral.modular.rules.service.IntegralRulesService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/28 13:28
 */
@Slf4j
@Api(tags = "积分规则管理控制器")
@Controller
@Valid
@Mapping("/integral/rules")
public class IntegralRulesController {

    @Inject
    private IntegralRulesService integralRulesService;

    @ApiOperation("分页")
    @SaCheckPermission("/integral/rules/page")
    @Get
    @Mapping("/page")
    public CommonResult<Page<IntegralRules>> page(IntegralRulesPageParam param) {
        return CommonResult.data(integralRulesService.page(param));
    }

    @ApiOperation("详情")
    @SaCheckPermission("/integral/rules/detail")
    @Get
    @Mapping("/detail")
    public CommonResult<IntegralRules> detail(String id) {
        return CommonResult.data(integralRulesService.detail(id));
    }

    @ApiOperation("新增")
    @CommonLog("新增积分规则")
    @SaCheckPermission("/integral/rules/add")
    @Post
    @Mapping("/add")
    public CommonResult<Void> add(IntegralRulesAddParam param) {
        integralRulesService.add(param);
        return CommonResult.ok();
    }

    @ApiOperation("编辑")
    @CommonLog("编辑积分规则")
    @SaCheckPermission("/integral/rules/edit")
    @Post
    @Mapping("/edit")
    public CommonResult<Void> edit(IntegralRulesEditParam param) {
        integralRulesService.edit(param);
        return CommonResult.ok();
    }

    @ApiOperation("删除")
    @CommonLog("删除积分规则")
    @SaCheckPermission("/integral/rules/delete")
    @Post
    @Mapping("/delete")
    public CommonResult<Void> delete(@NotEmpty(message = "集合不能为空")
                                     CommonValidList<IntegralRulesIdParam> param) {
        integralRulesService.delete(param);
        return CommonResult.ok();
    }

    @ApiOperation("更改状态")
    @CommonLog("更改积分状态")
    @SaCheckPermission("/integral/rules/change-status")
    @Post
    @Mapping("/change-status")
    public CommonResult<Void> changeStatus(CommonChangeStatusParam param) {
        integralRulesService.changeStatus(param.id(), param.status());
        return CommonResult.ok();
    }
}
