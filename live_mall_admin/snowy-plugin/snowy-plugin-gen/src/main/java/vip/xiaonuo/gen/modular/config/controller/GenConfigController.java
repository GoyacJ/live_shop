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
package vip.xiaonuo.gen.modular.config.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.gen.modular.config.entity.GenConfig;
import vip.xiaonuo.gen.modular.config.param.GenConfigEditParam;
import vip.xiaonuo.gen.modular.config.param.GenConfigIdParam;
import vip.xiaonuo.gen.modular.config.param.GenConfigListParam;
import vip.xiaonuo.gen.modular.config.service.GenConfigService;
import java.util.List;

/**
 * 代码生成详细配置控制器
 *
 * @author yubaoshan
 * @date 2022/10/25 22:33
 **/
@Api(tags = "代码生成详细配置控制器")
@Controller
@Valid
public class GenConfigController {

    @Inject
    private GenConfigService genConfigService;

    /**
     * 获取代码生成详细配置分页
     *
     * @author yubaoshan
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取代码生成详细配置分页")
    @Get
    @Mapping("/gen/config/list")
    public CommonResult<List<GenConfig>> list(GenConfigListParam genConfigListParam) {
        return CommonResult.data(genConfigService.list(genConfigListParam));
    }

    /**
     * 编辑代码生成详细配置
     *
     * @author yubaoshan
     * @date 2022/4/24 20:47
     */
    @ApiOperation("编辑代码生成详细配置")
    @CommonLog("编辑代码生成详细配置")
    @Post
    @Mapping("/gen/config/edit")
    public CommonResult<String> edit(GenConfigEditParam genConfigEditParam) {
        genConfigService.edit(genConfigEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除代码生成详细配置
     *
     * @author yubaoshan
     * @date 2022/4/24 20:00
     */
    @ApiOperation("删除代码生成详细配置")
    @CommonLog("删除代码生成详细配置")
    @Post
    @Mapping("/gen/config/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                               CommonValidList<GenConfigIdParam> genConfigIdParamList) {
        genConfigService.delete(genConfigIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取代码生成详细配置详情
     *
     * @author yubaoshan
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取代码生成详细配置详情")
    @Get
    @Mapping("/gen/config/detail")
    public CommonResult<GenConfig> detail(GenConfigIdParam genConfigIdParam) {
        return CommonResult.data(genConfigService.detail(genConfigIdParam));
    }

    /**
     * 批量编辑代码生成详细配置
     *
     * @author yubaoshan
     * @date 2022/4/24 20:47
     */
    @ApiOperation("批量编辑代码生成详细配置")
    @CommonLog("批量编辑代码生成详细配置")
    @Post
    @Mapping("/gen/config/editBatch")
    public CommonResult<String> editBatch(@NotEmpty(message = "集合不能为空")
                                                      CommonValidList<GenConfigEditParam> genConfigEditParamList) {
        genConfigService.editBatch(genConfigEditParamList);
        return CommonResult.ok();
    }
}
