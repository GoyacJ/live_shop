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
package vip.xiaonuo.live.modular.spectatorwatching.controller;

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
import vip.xiaonuo.live.modular.spectatorwatching.entity.LiveSpectatorWatching;
import vip.xiaonuo.live.modular.spectatorwatching.param.LiveSpectatorWatchingAddParam;
import vip.xiaonuo.live.modular.spectatorwatching.param.LiveSpectatorWatchingEditParam;
import vip.xiaonuo.live.modular.spectatorwatching.param.LiveSpectatorWatchingIdParam;
import vip.xiaonuo.live.modular.spectatorwatching.param.LiveSpectatorWatchingPageParam;
import vip.xiaonuo.live.modular.spectatorwatching.service.LiveSpectatorWatchingService;

/**
 * 直播观众观看时间表控制器
 *
 * @author GMQ
 * @date  2025/04/06 13:10
 */
@Api(tags = "直播观众观看时间表控制器")
@Controller
@Valid
public class LiveSpectatorWatchingController {

    @Inject
    private LiveSpectatorWatchingService liveSpectatorWatchingService;

    /**
     * 获取直播观众观看时间表分页
     *
     * @author GMQ
     * @date  2025/04/06 13:10
     */
    @ApiOperation("获取直播观众观看时间表分页")
    @SaCheckPermission("/live/spectatorwatching/page")
    @Get
    @Mapping("/live/spectatorwatching/page")
    public CommonResult<Page<LiveSpectatorWatching>> page(LiveSpectatorWatchingPageParam liveSpectatorWatchingPageParam) {
        return CommonResult.data(liveSpectatorWatchingService.page(liveSpectatorWatchingPageParam));
    }

    /**
     * 添加直播观众观看时间表
     *
     * @author GMQ
     * @date  2025/04/06 13:10
     */
    @ApiOperation("添加直播观众观看时间表")
    @CommonLog("添加直播观众观看时间表")
    @SaCheckPermission("/live/spectatorwatching/add")
    @Post
    @Mapping("/live/spectatorwatching/add")
    public CommonResult<String> add(LiveSpectatorWatchingAddParam liveSpectatorWatchingAddParam) {
        liveSpectatorWatchingService.add(liveSpectatorWatchingAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑直播观众观看时间表
     *
     * @author GMQ
     * @date  2025/04/06 13:10
     */
    @ApiOperation("编辑直播观众观看时间表")
    @CommonLog("编辑直播观众观看时间表")
    @SaCheckPermission("/live/spectatorwatching/edit")
    @Post
    @Mapping("/live/spectatorwatching/edit")
    public CommonResult<String> edit(LiveSpectatorWatchingEditParam liveSpectatorWatchingEditParam) {
        liveSpectatorWatchingService.edit(liveSpectatorWatchingEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除直播观众观看时间表
     *
     * @author GMQ
     * @date  2025/04/06 13:10
     */
    @ApiOperation("删除直播观众观看时间表")
    @CommonLog("删除直播观众观看时间表")
    @SaCheckPermission("/live/spectatorwatching/delete")
    @Post
    @Mapping("/live/spectatorwatching/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<LiveSpectatorWatchingIdParam> liveSpectatorWatchingIdParamList) {
        liveSpectatorWatchingService.delete(liveSpectatorWatchingIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取直播观众观看时间表详情
     *
     * @author GMQ
     * @date  2025/04/06 13:10
     */
    @ApiOperation("获取直播观众观看时间表详情")
    @SaCheckPermission("/live/spectatorwatching/detail")
    @Get
    @Mapping("/live/spectatorwatching/detail")
    public CommonResult<LiveSpectatorWatching> detail(LiveSpectatorWatchingIdParam liveSpectatorWatchingIdParam) {
        return CommonResult.data(liveSpectatorWatchingService.detail(liveSpectatorWatchingIdParam));
    }
}
