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
package vip.xiaonuo.live.modular.marketingcampaigns.controller;

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
import vip.xiaonuo.live.modular.marketingcampaigns.entity.MarketingCampaigns;
import vip.xiaonuo.live.modular.marketingcampaigns.param.MarketingCampaignsAddParam;
import vip.xiaonuo.live.modular.marketingcampaigns.param.MarketingCampaignsEditParam;
import vip.xiaonuo.live.modular.marketingcampaigns.param.MarketingCampaignsIdParam;
import vip.xiaonuo.live.modular.marketingcampaigns.param.MarketingCampaignsPageParam;
import vip.xiaonuo.live.modular.marketingcampaigns.service.MarketingCampaignsService;

/**
 * 营销活动表控制器
 *
 * @author lgq
 * @date  2025/03/04 14:15
 */
@Api(tags = "营销活动表控制器")
@Controller
@Valid
public class MarketingCampaignsController {

    @Inject
    private MarketingCampaignsService marketingCampaignsService;

    /**
     * 获取营销活动表分页
     *
     * @author lgq
     * @date  2025/03/04 14:15
     */
    @ApiOperation("获取营销活动表分页")
    @SaCheckPermission("/live/marketingcampaigns/page")
    @Get
    @Mapping("/live/marketingcampaigns/page")
    public CommonResult<Page<MarketingCampaigns>> page(MarketingCampaignsPageParam marketingCampaignsPageParam) {
        return CommonResult.data(marketingCampaignsService.page(marketingCampaignsPageParam));
    }

    /**
     * 添加营销活动表
     *
     * @author lgq
     * @date  2025/03/04 14:15
     */
    @ApiOperation("添加营销活动表")
    @CommonLog("添加营销活动表")
    @SaCheckPermission("/live/marketingcampaigns/add")
    @Post
    @Mapping("/live/marketingcampaigns/add")
    public CommonResult<String> add(MarketingCampaignsAddParam marketingCampaignsAddParam) {
        marketingCampaignsService.add(marketingCampaignsAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑营销活动表
     *
     * @author lgq
     * @date  2025/03/04 14:15
     */
    @ApiOperation("编辑营销活动表")
    @CommonLog("编辑营销活动表")
    @SaCheckPermission("/live/marketingcampaigns/edit")
    @Post
    @Mapping("/live/marketingcampaigns/edit")
    public CommonResult<String> edit(MarketingCampaignsEditParam marketingCampaignsEditParam) {
        marketingCampaignsService.edit(marketingCampaignsEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除营销活动表
     *
     * @author lgq
     * @date  2025/03/04 14:15
     */
    @ApiOperation("删除营销活动表")
    @CommonLog("删除营销活动表")
    @SaCheckPermission("/live/marketingcampaigns/delete")
    @Post
    @Mapping("/live/marketingcampaigns/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<MarketingCampaignsIdParam> marketingCampaignsIdParamList) {
        marketingCampaignsService.delete(marketingCampaignsIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取营销活动表详情
     *
     * @author lgq
     * @date  2025/03/04 14:15
     */
    @ApiOperation("获取营销活动表详情")
    @SaCheckPermission("/live/marketingcampaigns/detail")
    @Get
    @Mapping("/live/marketingcampaigns/detail")
    public CommonResult<MarketingCampaigns> detail(MarketingCampaignsIdParam marketingCampaignsIdParam) {
        return CommonResult.data(marketingCampaignsService.detail(marketingCampaignsIdParam));
    }
}
