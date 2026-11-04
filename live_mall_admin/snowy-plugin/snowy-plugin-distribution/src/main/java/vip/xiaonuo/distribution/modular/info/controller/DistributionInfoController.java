package vip.xiaonuo.distribution.modular.info.controller;

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
import vip.xiaonuo.common.pojo.dto.CommonIdParam;
import vip.xiaonuo.common.pojo.dto.CommonPageParam;
import vip.xiaonuo.distribution.modular.info.entity.DistributionInfo;
import vip.xiaonuo.distribution.modular.info.params.DistributionInfoAddParam;
import vip.xiaonuo.distribution.modular.info.params.DistributionInfoEditParam;
import vip.xiaonuo.distribution.modular.info.service.DistributionInfoService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/30 17:19
 */
@Slf4j
@Api(tags = "推广管理控制器")
@Controller
@Valid
@Mapping("/distribution/info")
public class DistributionInfoController {


    @Inject
    private DistributionInfoService distributionInfoService;

    @ApiOperation("分页")
    @SaCheckPermission("/distribution/info/page")
    @Get
    @Mapping("/page")
    public CommonResult<Page<DistributionInfo>> page(CommonPageParam param) {
        return CommonResult.data(distributionInfoService.page(param));
    }

    @ApiOperation("详情")
    @SaCheckPermission("/distribution/info/detail")
    @Get
    @Mapping("/detail")
    public CommonResult<DistributionInfo> detail(String id) {
        return CommonResult.data(distributionInfoService.detail(id));
    }

    @ApiOperation("新增")
    @CommonLog("新增推广配置")
    @SaCheckPermission("/distribution/info/add")
    @Post
    @Mapping("/add")
    public CommonResult<Void> add(DistributionInfoAddParam param) {
        distributionInfoService.add(param);
        return CommonResult.ok();
    }

    @ApiOperation("编辑")
    @CommonLog("编辑推广配置")
    @SaCheckPermission("/distribution/info/edit")
    @Post
    @Mapping("/edit")
    public CommonResult<Void> edit(DistributionInfoEditParam param) {
        distributionInfoService.edit(param);
        return CommonResult.ok();
    }

    @ApiOperation("删除")
    @CommonLog("删除推广配置")
    @SaCheckPermission("/distribution/info/delete")
    @Post
    @Mapping("/delete")
    public CommonResult<Void> delete(@NotEmpty(message = "集合不能为空")
                                     CommonValidList<CommonIdParam> param) {
        distributionInfoService.delete(param);
        return CommonResult.ok();
    }

    @ApiOperation("更改状态")
    @CommonLog("更改推广配置")
    @SaCheckPermission("/distribution/info/change-status")
    @Post
    @Mapping("/change-status")
    public CommonResult<Void> changeStatus(CommonChangeStatusParam param) {
        distributionInfoService.changeStatus(param.id(), param.status());
        return CommonResult.ok();
    }
}
