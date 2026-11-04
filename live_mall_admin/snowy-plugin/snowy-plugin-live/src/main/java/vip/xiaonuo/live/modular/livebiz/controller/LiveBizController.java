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
package vip.xiaonuo.live.modular.livebiz.controller;

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
import vip.xiaonuo.live.modular.livebiz.entity.LiveBiz;
import vip.xiaonuo.live.modular.livebiz.param.LiveBizAddParam;
import vip.xiaonuo.live.modular.livebiz.param.LiveBizEditParam;
import vip.xiaonuo.live.modular.livebiz.param.LiveBizIdParam;
import vip.xiaonuo.live.modular.livebiz.param.LiveBizPageParam;
import vip.xiaonuo.live.modular.livebiz.service.LiveBizService;

import java.util.List;

/**
 * 直播列表控制器
 *
 * @author zsj
 * @date  2025/04/02 16:57
 */
@Api(tags = "直播列表控制器")
@Controller
@Valid
public class LiveBizController {

    @Inject
    private LiveBizService liveBizService;

    /**
     * 获取直播列表分页
     *
     * @author zsj
     * @date  2025/04/02 16:57
     */
    @ApiOperation("获取直播列表分页")
    @SaCheckPermission("/live/livebiz/page")
    @Get
    @Mapping("/live/livebiz/page")
    public CommonResult<Page<LiveBiz>> page(LiveBizPageParam liveBizPageParam) {
        return CommonResult.data(liveBizService.page(liveBizPageParam));
    }

    @ApiOperation("获取直播列表")
    @SaCheckPermission("/live/livebiz/list")
    @Get
    @Mapping("/live/livebiz/list")
    public CommonResult<List<LiveBiz>> list() {
        return CommonResult.data(liveBizService.list());
    }

    /**
     * 添加直播列表
     *
     * @author zsj
     * @date  2025/04/02 16:57
     */
    @ApiOperation("添加直播列表")
    @CommonLog("添加直播列表")
    @SaCheckPermission("/live/livebiz/add")
    @Post
    @Mapping("/live/livebiz/add")
    public CommonResult<String> add(@Validated LiveBizAddParam liveBizAddParam) {
        liveBizService.add(liveBizAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑直播列表
     *
     * @author zsj
     * @date  2025/04/02 16:57
     */
    @ApiOperation("编辑直播列表")
    @CommonLog("编辑直播列表")
    @SaCheckPermission("/live/livebiz/edit")
    @Post
    @Mapping("/live/livebiz/edit")
    public CommonResult<String> edit(LiveBizEditParam liveBizEditParam) {
        liveBizService.edit(liveBizEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除直播列表
     *
     * @author zsj
     * @date  2025/04/02 16:57
     */
    @ApiOperation("删除直播列表")
    @CommonLog("删除直播列表")
    @SaCheckPermission("/live/livebiz/delete")
    @Post
    @Mapping("/live/livebiz/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<LiveBizIdParam> liveBizIdParamList) {
        liveBizService.delete(liveBizIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取直播列表详情
     *
     * @author zsj
     * @date  2025/04/02 16:57
     */
    @ApiOperation("获取直播列表详情")
    @SaCheckPermission("/live/livebiz/detail")
    @Get
    @Mapping("/live/livebiz/detail")
    public CommonResult<LiveBiz> detail(LiveBizIdParam liveBizIdParam) {
        return CommonResult.data(liveBizService.detail(liveBizIdParam));
    }
}
