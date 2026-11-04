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
package vip.xiaonuo.dev.modular.slideshow.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
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
import vip.xiaonuo.dev.modular.slideshow.entity.DevSlideshow;
import vip.xiaonuo.dev.modular.slideshow.param.DevSlideshowAddParam;
import vip.xiaonuo.dev.modular.slideshow.param.DevSlideshowEditParam;
import vip.xiaonuo.dev.modular.slideshow.param.DevSlideshowIdParam;
import vip.xiaonuo.dev.modular.slideshow.param.DevSlideshowPageParam;
import vip.xiaonuo.dev.modular.slideshow.service.DevSlideshowService;

/**
 * 轮播图控制器
 *
 * @author yubaoshan
 * @date  2024/07/13 00:31
 */
@Api(tags = "轮播图控制器")
@Controller
@Valid
public class DevSlideshowController {

    @Inject
    private DevSlideshowService devSlideshowService;

    /**
     * 获取轮播图分页
     *
     * @author yubaoshan
     * @date  2024/07/13 00:31
     */
    @ApiOperation("获取轮播图分页")
    @SaCheckPermission("/dev/slideshow/page")
    @Get
    @Mapping("/dev/slideshow/page")
    public CommonResult<Page<DevSlideshow>> page(DevSlideshowPageParam devSlideshowPageParam) {
        return CommonResult.data(devSlideshowService.page(devSlideshowPageParam));
    }

    /**
     * 添加轮播图
     *
     * @author yubaoshan
     * @date  2024/07/13 00:31
     */
    @ApiOperation("添加轮播图")
    @CommonLog("添加轮播图")
    @SaCheckPermission("/dev/slideshow/add")
    @Post
    @Mapping("/dev/slideshow/add")
    public CommonResult<String> add(DevSlideshowAddParam devSlideshowAddParam) {
        devSlideshowService.add(devSlideshowAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑轮播图
     *
     * @author yubaoshan
     * @date  2024/07/13 00:31
     */
    @ApiOperation("编辑轮播图")
    @CommonLog("编辑轮播图")
    @SaCheckPermission("/dev/slideshow/edit")
    @Post
    @Mapping("/dev/slideshow/edit")
    public CommonResult<String> edit(DevSlideshowEditParam devSlideshowEditParam) {
        devSlideshowService.edit(devSlideshowEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除轮播图
     *
     * @author yubaoshan
     * @date  2024/07/13 00:31
     */
    @ApiOperation("删除轮播图")
    @CommonLog("删除轮播图")
    @SaCheckPermission("/dev/slideshow/delete")
    @Post
    @Mapping("/dev/slideshow/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<DevSlideshowIdParam> devSlideshowIdParamList) {
        devSlideshowService.delete(devSlideshowIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取轮播图详情
     *
     * @author yubaoshan
     * @date  2024/07/13 00:31
     */
    @ApiOperation("获取轮播图详情")
    @SaCheckPermission("/dev/slideshow/detail")
    @Get
    @Mapping("/dev/slideshow/detail")
    public CommonResult<DevSlideshow> detail(DevSlideshowIdParam devSlideshowIdParam) {
        return CommonResult.data(devSlideshowService.detail(devSlideshowIdParam));
    }

    /**
     * 禁用轮播图
     *
     * @author yubaoshan
     * @date  2024/07/13 00:31
     */
    @ApiOperation("禁用轮播图")
    @CommonLog("禁用轮播图")
    @Post
    @Mapping("/dev/slideshow/disableStatus")
    public CommonResult<String> disableStatus(@Validated DevSlideshowIdParam devSlideshowIdParam) {
        devSlideshowService.disableStatus(devSlideshowIdParam);
        return CommonResult.ok();
    }

    /**
     * 启用轮播图
     *
     * @author yubaoshan
     * @date  2024/07/13 00:31
     */
    @ApiOperation("启用轮播图")
    @CommonLog("启用轮播图")
    @Post
    @Mapping("/dev/slideshow/enableStatus")
    public CommonResult<String> enableStatus(@Validated DevSlideshowIdParam devSlideshowIdParam) {
        devSlideshowService.enableStatus(devSlideshowIdParam);
        return CommonResult.ok();
    }
}
