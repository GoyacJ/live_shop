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
package vip.xiaonuo.live.modular.streamingtype.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.live.modular.shoppingcar.entity.ShoppingCar;
import vip.xiaonuo.live.modular.shoppingcar.param.ShoppingCarAddParam;
import vip.xiaonuo.live.modular.shoppingcar.param.ShoppingCarEditParam;
import vip.xiaonuo.live.modular.shoppingcar.param.ShoppingCarIdParam;
import vip.xiaonuo.live.modular.shoppingcar.param.ShoppingCarPageParam;
import vip.xiaonuo.live.modular.shoppingcar.service.ShoppingCarService;
import vip.xiaonuo.live.modular.streamingtype.entity.LiveStreamingType;
import vip.xiaonuo.live.modular.streamingtype.param.LiveStreamingTypeAddParam;
import vip.xiaonuo.live.modular.streamingtype.param.LiveStreamingTypeEditParam;
import vip.xiaonuo.live.modular.streamingtype.param.LiveStreamingTypeIdParam;
import vip.xiaonuo.live.modular.streamingtype.param.LiveStreamingTypePageParam;
import vip.xiaonuo.live.modular.streamingtype.service.LiveStreamingTypeService;

import java.util.List;

/**
 * 直播类型控制器
 *
 */
@Api(tags = "直播类型控制器")
@Controller
@Valid
public class LiveStreamingTypeController {

    @Inject
    private LiveStreamingTypeService liveStreamingTypeService;

    /**
     * 获取直播类型分页
     *
     */
    @ApiOperation("获取直播类型分页")
    @SaCheckPermission("/live/livestreamingtype/page")
    @Get
    @Mapping("/live/livestreamingtype/page")
    public CommonResult<Page<LiveStreamingType>> page(LiveStreamingTypePageParam liveStreamingTypePageParam) {
        return CommonResult.data(liveStreamingTypeService.page(liveStreamingTypePageParam));
    }

    /**
     * 添加直播类型
     *
     */
    @ApiOperation("添加直播类型")
    @CommonLog("添加直播类型")
    @SaCheckPermission("/live/livestreamingtype/add")
    @Post
    @Mapping("/live/livestreamingtype/add")
    public CommonResult<String> add(LiveStreamingTypeAddParam liveStreamingTypeAddParam) {
        liveStreamingTypeService.add(liveStreamingTypeAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑直播类型
     *
     */
    @ApiOperation("编辑直播类型")
    @CommonLog("编辑直播类型")
    @SaCheckPermission("/live/livestreamingtype/edit")
    @Post
    @Mapping("/live/livestreamingtype/edit")
    public CommonResult<String> edit(LiveStreamingTypeEditParam liveStreamingTypeEditParam) {
        liveStreamingTypeService.edit(liveStreamingTypeEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除直播类型
     *
     */
    @ApiOperation("删除直播类型")
    @CommonLog("删除直播类型")
    @SaCheckPermission("/live/livestreamingtype/delete")
    @Post
    @Mapping("/live/livestreamingtype/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<LiveStreamingTypeIdParam> liveStreamingTypeIdParamList) {
        liveStreamingTypeService.delete(liveStreamingTypeIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取直播类型详情
     *
     */
    @ApiOperation("获取直播类型详情")
    @SaCheckPermission("/live/livestreamingtype/detail")
    @Get
    @Mapping("/live/livestreamingtype/detail")
    public CommonResult<LiveStreamingType> detail(LiveStreamingTypeIdParam liveStreamingTypeIdParam) {
        return CommonResult.data(liveStreamingTypeService.detail(liveStreamingTypeIdParam));
    }

    /**
     * 获取直播类型列表
     *
     */
    @ApiOperation("获取直播类型列表")
    @SaCheckPermission("/live/livestreamingtype/page")
    @Get
    @Mapping("/live/livestreamingtype/list")
    public CommonResult<List<LiveStreamingType>> list() {
        return CommonResult.data(liveStreamingTypeService.getLiveTypeList());
    }
}
