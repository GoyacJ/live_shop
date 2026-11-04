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
package vip.xiaonuo.live.modular.marketingcampaignslog.controller;

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
import vip.xiaonuo.live.modular.marketingcampaignslog.entity.LiveMarketingCampaignsLog;
import vip.xiaonuo.live.modular.marketingcampaignslog.param.LiveMarketingCampaignsLogAddParam;
import vip.xiaonuo.live.modular.marketingcampaignslog.param.LiveMarketingCampaignsLogEditParam;
import vip.xiaonuo.live.modular.marketingcampaignslog.param.LiveMarketingCampaignsLogIdParam;
import vip.xiaonuo.live.modular.marketingcampaignslog.param.LiveMarketingCampaignsLogPageParam;
import vip.xiaonuo.live.modular.marketingcampaignslog.service.LiveMarketingCampaignsLogService;

/**
 * 营销活动记录表控制器
 *
 * @author lgq
 * @date  2025/03/13 21:51
 */
@Api(tags = "营销活动记录表控制器")
@Controller
@Valid
public class LiveMarketingCampaignsLogController {

    @Inject
    private LiveMarketingCampaignsLogService liveMarketingCampaignsLogService;

    /**
     * 获取营销活动记录表分页
     *
     * @author lgq
     * @date  2025/03/13 21:51
     */
    @ApiOperation("获取营销活动记录表分页")
    @SaCheckPermission("/live/marketingcampaignslog/page")
    @Get
    @Mapping("/live/marketingcampaignslog/page")
    public CommonResult<Page<LiveMarketingCampaignsLog>> page(LiveMarketingCampaignsLogPageParam liveMarketingCampaignsLogPageParam) {
        return CommonResult.data(liveMarketingCampaignsLogService.page(liveMarketingCampaignsLogPageParam));
    }

    /**
     * 添加营销活动记录表
     *
     * @author lgq
     * @date  2025/03/13 21:51
     */
    @ApiOperation("添加营销活动记录表")
    @CommonLog("添加营销活动记录表")
    @SaCheckPermission("/live/marketingcampaignslog/add")
    @Post
    @Mapping("/live/marketingcampaignslog/add")
    public CommonResult<String> add(LiveMarketingCampaignsLogAddParam liveMarketingCampaignsLogAddParam) {
        liveMarketingCampaignsLogService.add(liveMarketingCampaignsLogAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑营销活动记录表
     *
     * @author lgq
     * @date  2025/03/13 21:51
     */
    @ApiOperation("编辑营销活动记录表")
    @CommonLog("编辑营销活动记录表")
    @SaCheckPermission("/live/marketingcampaignslog/edit")
    @Post
    @Mapping("/live/marketingcampaignslog/edit")
    public CommonResult<String> edit(LiveMarketingCampaignsLogEditParam liveMarketingCampaignsLogEditParam) {
        liveMarketingCampaignsLogService.edit(liveMarketingCampaignsLogEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除营销活动记录表
     *
     * @author lgq
     * @date  2025/03/13 21:51
     */
    @ApiOperation("删除营销活动记录表")
    @CommonLog("删除营销活动记录表")
    @SaCheckPermission("/live/marketingcampaignslog/delete")
    @Post
    @Mapping("/live/marketingcampaignslog/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<LiveMarketingCampaignsLogIdParam> liveMarketingCampaignsLogIdParamList) {
        liveMarketingCampaignsLogService.delete(liveMarketingCampaignsLogIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取营销活动记录表详情
     *
     * @author lgq
     * @date  2025/03/13 21:51
     */
    @ApiOperation("获取营销活动记录表详情")
    @SaCheckPermission("/live/marketingcampaignslog/detail")
    @Get
    @Mapping("/live/marketingcampaignslog/detail")
    public CommonResult<LiveMarketingCampaignsLog> detail(LiveMarketingCampaignsLogIdParam liveMarketingCampaignsLogIdParam) {
        return CommonResult.data(liveMarketingCampaignsLogService.detail(liveMarketingCampaignsLogIdParam));
    }
}
