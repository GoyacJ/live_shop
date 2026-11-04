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
package vip.xiaonuo.live.modular.focusrelationships.controller;

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
import vip.xiaonuo.live.modular.focusrelationships.entity.LiveFocusRelationships;
import vip.xiaonuo.live.modular.focusrelationships.param.LiveFocusRelationshipsAddParam;
import vip.xiaonuo.live.modular.focusrelationships.param.LiveFocusRelationshipsEditParam;
import vip.xiaonuo.live.modular.focusrelationships.param.LiveFocusRelationshipsIdParam;
import vip.xiaonuo.live.modular.focusrelationships.param.LiveFocusRelationshipsPageParam;
import vip.xiaonuo.live.modular.focusrelationships.service.LiveFocusRelationshipsService;

/**
 * 关注关系表控制器
 *
 * @author lgq
 * @date  2025/03/04 14:07
 */
@Api(tags = "关注关系表控制器")
@Controller
@Valid
public class LiveFocusRelationshipsController {

    @Inject
    private LiveFocusRelationshipsService liveFocusRelationshipsService;

    /**
     * 获取关注关系表分页
     *
     * @author lgq
     * @date  2025/03/04 14:07
     */
    @ApiOperation("获取关注关系表分页")
    @SaCheckPermission("/live/focusrelationships/page")
    @Get
    @Mapping("/live/focusrelationships/page")
    public CommonResult<Page<LiveFocusRelationships>> page(LiveFocusRelationshipsPageParam liveFocusRelationshipsPageParam) {
        return CommonResult.data(liveFocusRelationshipsService.page(liveFocusRelationshipsPageParam));
    }

    /**
     * 添加关注关系表
     *
     * @author lgq
     * @date  2025/03/04 14:07
     */
    @ApiOperation("添加关注关系表")
    @CommonLog("添加关注关系表")
    @SaCheckPermission("/live/focusrelationships/add")
    @Post
    @Mapping("/live/focusrelationships/add")
    public CommonResult<String> add(LiveFocusRelationshipsAddParam liveFocusRelationshipsAddParam) {
        liveFocusRelationshipsService.add(liveFocusRelationshipsAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑关注关系表
     *
     * @author lgq
     * @date  2025/03/04 14:07
     */
    @ApiOperation("编辑关注关系表")
    @CommonLog("编辑关注关系表")
    @SaCheckPermission("/live/focusrelationships/edit")
    @Post
    @Mapping("/live/focusrelationships/edit")
    public CommonResult<String> edit(LiveFocusRelationshipsEditParam liveFocusRelationshipsEditParam) {
        liveFocusRelationshipsService.edit(liveFocusRelationshipsEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除关注关系表
     *
     * @author lgq
     * @date  2025/03/04 14:07
     */
    @ApiOperation("删除关注关系表")
    @CommonLog("删除关注关系表")
    @SaCheckPermission("/live/focusrelationships/delete")
    @Post
    @Mapping("/live/focusrelationships/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<LiveFocusRelationshipsIdParam> liveFocusRelationshipsIdParamList) {
        liveFocusRelationshipsService.delete(liveFocusRelationshipsIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取关注关系表详情
     *
     * @author lgq
     * @date  2025/03/04 14:07
     */
    @ApiOperation("获取关注关系表详情")
    @SaCheckPermission("/live/focusrelationships/detail")
    @Get
    @Mapping("/live/focusrelationships/detail")
    public CommonResult<LiveFocusRelationships> detail(LiveFocusRelationshipsIdParam liveFocusRelationshipsIdParam) {
        return CommonResult.data(liveFocusRelationshipsService.detail(liveFocusRelationshipsIdParam));
    }
}
