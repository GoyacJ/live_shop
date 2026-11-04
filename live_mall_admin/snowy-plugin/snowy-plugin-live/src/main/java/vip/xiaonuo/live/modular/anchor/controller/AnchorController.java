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
package vip.xiaonuo.live.modular.anchor.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.json.JSONObject;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.live.modular.anchor.entity.Anchor;
import vip.xiaonuo.live.modular.anchor.param.AnchorAddParam;
import vip.xiaonuo.live.modular.anchor.param.AnchorEditParam;
import vip.xiaonuo.live.modular.anchor.param.AnchorIdParam;
import vip.xiaonuo.live.modular.anchor.param.AnchorPageParam;
import vip.xiaonuo.live.modular.anchor.service.AnchorService;

import java.util.List;

/**
 * 主播信息表控制器
 *
 * @author lgq
 * @date  2025/03/21 14:22
 */
@Api(tags = "主播信息表控制器")
@Controller
@Valid
public class AnchorController {

    @Inject
    private AnchorService anchorService;

    /**
     * 获取主播信息表分页
     *
     * @author lgq
     * @date  2025/03/21 14:22
     */
    @ApiOperation("获取主播信息表分页")
    @SaCheckPermission("/live/anchor/page")
    @Get
    @Mapping("/live/anchor/page")
    public CommonResult<Page<Anchor>> page(AnchorPageParam anchorPageParam) {
        return CommonResult.data(anchorService.page(anchorPageParam));
    }

    /**
     * 添加主播信息表
     *
     * @author lgq
     * @date  2025/03/21 14:22
     */
    @ApiOperation("添加主播信息表")
    @CommonLog("添加主播信息表")
    @SaCheckPermission("/live/anchor/add")
    @Post
    @Mapping("/live/anchor/add")
    public CommonResult<String> add(AnchorAddParam anchorAddParam) {
        anchorService.add(anchorAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑主播信息表
     *
     * @author lgq
     * @date  2025/03/21 14:22
     */
    @ApiOperation("编辑主播信息表")
    @CommonLog("编辑主播信息表")
    @SaCheckPermission("/live/anchor/edit")
    @Post
    @Mapping("/live/anchor/edit")
    public CommonResult<String> edit(AnchorEditParam anchorEditParam) {
        anchorService.edit(anchorEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除主播信息表
     *
     * @author lgq
     * @date  2025/03/21 14:22
     */
    @ApiOperation("删除主播信息表")
    @CommonLog("删除主播信息表")
    @SaCheckPermission("/live/anchor/delete")
    @Post
    @Mapping("/live/anchor/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<AnchorIdParam> anchorIdParamList) {
        anchorService.delete(anchorIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取主播信息表详情
     *
     * @author lgq
     * @date  2025/03/21 14:22
     */
    @ApiOperation("获取主播信息表详情")
    @SaCheckPermission("/live/anchor/detail")
    @Get
    @Mapping("/live/anchor/detail")
    public CommonResult<Anchor> detail(AnchorIdParam anchorIdParam) {
        return CommonResult.data(anchorService.detail(anchorIdParam));
    }
    /**
     * 获取主播信息列表
     *
     */
    @ApiOperation("获取主播信息列表")
    @SaCheckPermission("/live/anchor/page")
    @Get
    @Mapping("/live/anchor/list")
    public CommonResult<List<Anchor>> list() {
        return CommonResult.data(anchorService.getLiveAnchorList());
    }
}
