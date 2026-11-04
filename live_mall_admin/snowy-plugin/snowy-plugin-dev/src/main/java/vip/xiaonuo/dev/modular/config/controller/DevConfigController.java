/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.dev.modular.config.controller;

import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.dev.modular.config.entity.DevConfig;
import vip.xiaonuo.dev.modular.config.enums.DevConfigCategoryEnum;
import vip.xiaonuo.dev.modular.config.param.*;
import vip.xiaonuo.dev.modular.config.service.DevConfigService;

import java.util.List;

/**
 * 配置控制器
 *
 * @author xuyuxiang
 * @date 2022/4/22 10:56
 **/
@Api(tags = "配置控制器")
@Controller
@Valid
public class DevConfigController {

    @Inject
    private DevConfigService devConfigService;

    /**
     * 获取配置分页
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取配置分页")
    @Get
    @Mapping("/dev/config/page")
    public CommonResult<Page<DevConfig>> page(DevConfigPageParam devConfigPageParam) {
        return CommonResult.data(devConfigService.page(devConfigPageParam));
    }

    /**
     * 获取系统基础配置
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取系统基础配置")
    @Get
    @Mapping("/dev/config/sysBaseList")
    public CommonResult<List<DevConfig>> sysBaseList() {
        return CommonResult.data(devConfigService.sysBaseList());
    }

    /**
     * 获取配置列表
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取配置列表")
    @Get
    @Mapping("/dev/config/list")
    public CommonResult<List<DevConfig>> list(DevConfigListParam devConfigListParam) {
        return CommonResult.data(devConfigService.list(devConfigListParam));
    }

    /**
     * 添加配置
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:47
     */
    @ApiOperation("添加配置")
    @CommonLog("添加配置")
    @Post
    @Mapping("/dev/config/add")
    public CommonResult<String> add(@Validated DevConfigAddParam devConfigAddParam) {
        devConfigService.add(devConfigAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑配置
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:47
     */
    @ApiOperation("编辑配置")
    @CommonLog("编辑配置")
    @Post
    @Mapping("/dev/config/edit")
    public CommonResult<String> edit(@Validated DevConfigEditParam devConfigEditParam) {
        devConfigService.edit(devConfigEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除配置
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("删除配置")
    @CommonLog("删除配置")
    @Post
    @Mapping("/dev/config/delete")
    public CommonResult<String> delete(@Validated @NotEmpty(message = "集合不能为空")
                                       CommonValidList<DevConfigIdParam> devConfigIdParamList) {
        devConfigService.delete(devConfigIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取配置详情
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取配置详情")
    @Get
    @Mapping("/dev/config/detail")
    public CommonResult<DevConfig> detail(@Validated DevConfigIdParam devConfigIdParam) {
        return CommonResult.data(devConfigService.detail(devConfigIdParam));
    }

    /**
     * 配置批量更新
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("配置批量更新")
    @CommonLog("配置批量更新")
    @Post
    @Mapping("/dev/config/editBatch")
    public CommonResult<String> editBatch(@Validated @NotEmpty(message = "集合不能为空")
                                          CommonValidList<DevConfigBatchParam> devConfigBatchParamList) {
        devConfigService.editBatch(devConfigBatchParamList);
        return CommonResult.ok();
    }
}
