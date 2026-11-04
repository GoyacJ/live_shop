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
package vip.xiaonuo.live.modular.spectatorgrade.controller;

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
import vip.xiaonuo.live.modular.spectatorgrade.entity.LiveSpectatorGrade;
import vip.xiaonuo.live.modular.spectatorgrade.param.LiveSpectatorGradeAddParam;
import vip.xiaonuo.live.modular.spectatorgrade.param.LiveSpectatorGradeEditParam;
import vip.xiaonuo.live.modular.spectatorgrade.param.LiveSpectatorGradeIdParam;
import vip.xiaonuo.live.modular.spectatorgrade.param.LiveSpectatorGradePageParam;
import vip.xiaonuo.live.modular.spectatorgrade.service.LiveSpectatorGradeService;

/**
 * 直播观众成长表控制器
 *
 * @author GMQ
 * @date  2025/04/06 13:06
 */
@Api(tags = "直播观众成长表控制器")
@Controller
@Valid
public class LiveSpectatorGradeController {

    @Inject
    private LiveSpectatorGradeService liveSpectatorGradeService;

    /**
     * 获取直播观众成长表分页
     *
     * @author GMQ
     * @date  2025/04/06 13:06
     */
    @ApiOperation("获取直播观众成长表分页")
    @SaCheckPermission("/live/spectatorgrade/page")
    @Get
    @Mapping("/live/spectatorgrade/page")
    public CommonResult<Page<LiveSpectatorGrade>> page(LiveSpectatorGradePageParam liveSpectatorGradePageParam) {
        return CommonResult.data(liveSpectatorGradeService.page(liveSpectatorGradePageParam));
    }

    /**
     * 添加直播观众成长表
     *
     * @author GMQ
     * @date  2025/04/06 13:06
     */
    @ApiOperation("添加直播观众成长表")
    @CommonLog("添加直播观众成长表")
    @SaCheckPermission("/live/spectatorgrade/add")
    @Post
    @Mapping("/live/spectatorgrade/add")
    public CommonResult<String> add(LiveSpectatorGradeAddParam liveSpectatorGradeAddParam) {
        liveSpectatorGradeService.add(liveSpectatorGradeAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑直播观众成长表
     *
     * @author GMQ
     * @date  2025/04/06 13:06
     */
    @ApiOperation("编辑直播观众成长表")
    @CommonLog("编辑直播观众成长表")
    @SaCheckPermission("/live/spectatorgrade/edit")
    @Post
    @Mapping("/live/spectatorgrade/edit")
    public CommonResult<String> edit(LiveSpectatorGradeEditParam liveSpectatorGradeEditParam) {
        liveSpectatorGradeService.edit(liveSpectatorGradeEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除直播观众成长表
     *
     * @author GMQ
     * @date  2025/04/06 13:06
     */
    @ApiOperation("删除直播观众成长表")
    @CommonLog("删除直播观众成长表")
    @SaCheckPermission("/live/spectatorgrade/delete")
    @Post
    @Mapping("/live/spectatorgrade/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<LiveSpectatorGradeIdParam> liveSpectatorGradeIdParamList) {
        liveSpectatorGradeService.delete(liveSpectatorGradeIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取直播观众成长表详情
     *
     * @author GMQ
     * @date  2025/04/06 13:06
     */
    @ApiOperation("获取直播观众成长表详情")
    @SaCheckPermission("/live/spectatorgrade/detail")
    @Get
    @Mapping("/live/spectatorgrade/detail")
    public CommonResult<LiveSpectatorGrade> detail(LiveSpectatorGradeIdParam liveSpectatorGradeIdParam) {
        return CommonResult.data(liveSpectatorGradeService.detail(liveSpectatorGradeIdParam));
    }
}
