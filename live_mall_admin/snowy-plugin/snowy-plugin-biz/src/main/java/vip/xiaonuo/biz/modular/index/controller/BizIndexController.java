package vip.xiaonuo.biz.modular.index.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.biz.modular.index.param.BizIndexNoticeIdParam;
import vip.xiaonuo.biz.modular.index.param.BizIndexNoticeListParam;
import vip.xiaonuo.biz.modular.index.param.BizIndexSlideshowListParam;
import vip.xiaonuo.biz.modular.index.result.BizIndexNoticeListResult;
import vip.xiaonuo.biz.modular.index.result.BizIndexSlideshowDetailResult;
import vip.xiaonuo.biz.modular.index.result.BizIndexSlideshowListResult;
import vip.xiaonuo.biz.modular.index.service.BizIndexService;
import vip.xiaonuo.common.pojo.CommonResult;

import java.util.List;

/**
 * 业务首页控制器
 *
 * @author yubaoshan
 * @date 2024/7/13 21:02
 */
@Api(tags = "业务首页控制器")
@Controller
@Valid
public class BizIndexController {

    @Inject
    private BizIndexService bizIndexService;

    /**
     * 获取轮播图列表
     *
     * @author yubaoshan
     * @date 2024/7/13 21:02
     */
    @ApiOperation("获取轮播图列表")
    @SaCheckPermission("/biz/index/slideshow/list")
    @Get
    @Mapping("/biz/index/slideshow/list")
    public CommonResult<List<BizIndexSlideshowListResult>> slideshowListByPlace(@Validated BizIndexSlideshowListParam bizIndexSlideshowListParam) {
        return CommonResult.data(bizIndexService.slideshowListByPlace(bizIndexSlideshowListParam));
    }

    /**
     * 获取通知公告列表
     *
     * @author yubaoshan
     * @date 2024/7/13 21:02
     */
    @ApiOperation("获取通知公告列表")
    @SaCheckPermission("/biz/index/notice/list")
    @Get
    @Mapping("/biz/index/notice/list")
    public CommonResult<List<BizIndexNoticeListResult>> noticeListByLimit(BizIndexNoticeListParam bizIndexNoticeListParam) {
        return CommonResult.data(bizIndexService.noticeListByLimit(bizIndexNoticeListParam));
    }

    /**
     * 获取通知公告详情
     *
     * @author yubaoshan
     * @date 2024/7/13 21:02
     */
    @ApiOperation("获取通知公告详情")
    @SaCheckPermission("/biz/index/notice/detail")
    @Get
    @Mapping("/biz/index/notice/detail")
    public CommonResult<BizIndexSlideshowDetailResult> noticeDetailById(@Validated BizIndexNoticeIdParam bizIndexNoticeIdParam) {
        return CommonResult.data(bizIndexService.noticeDetailById(bizIndexNoticeIdParam));
    }
}
