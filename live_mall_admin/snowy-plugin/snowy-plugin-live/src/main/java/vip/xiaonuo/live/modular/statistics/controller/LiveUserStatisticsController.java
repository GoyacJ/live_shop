package vip.xiaonuo.live.modular.statistics.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.live.modular.statistics.dto.LiveUserStatisticsDTO;
import vip.xiaonuo.live.modular.statistics.param.LiveUserStatisticsParam;
import vip.xiaonuo.live.modular.statistics.service.LiveUserStatisticsService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/30 09:40
 */
@Slf4j
@Api(tags = "直播用户统计控制器")
@Controller
@Valid
@Mapping("/live/statistics")
public class LiveUserStatisticsController {

    @Inject
    private LiveUserStatisticsService liveUserStatisticsService;

    /**
     * 获取直播用户信息统计
     */
    @ApiOperation("获取直播用户信息统计")
    @SaCheckPermission("/live/statistics/info")
    @Get
    @Mapping("/info")
    public CommonResult<LiveUserStatisticsDTO> info(LiveUserStatisticsParam param) {
        return CommonResult.data(liveUserStatisticsService.info(param));
    }

}
