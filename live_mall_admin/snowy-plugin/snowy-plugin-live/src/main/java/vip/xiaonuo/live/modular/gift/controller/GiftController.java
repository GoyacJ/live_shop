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
package vip.xiaonuo.live.modular.gift.controller;

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
import vip.xiaonuo.live.modular.gift.entity.Gift;
import vip.xiaonuo.live.modular.gift.param.GiftAddParam;
import vip.xiaonuo.live.modular.gift.param.GiftEditParam;
import vip.xiaonuo.live.modular.gift.param.GiftIdParam;
import vip.xiaonuo.live.modular.gift.param.GiftPageParam;
import vip.xiaonuo.live.modular.gift.service.GiftService;

/**
 * 礼物表控制器
 *
 * @author lgq
 * @date  2025/03/04 14:13
 */
@Api(tags = "礼物表控制器")
@Controller
@Valid
public class GiftController {

    @Inject
    private GiftService giftService;

    /**
     * 获取礼物表分页
     *
     * @author lgq
     * @date  2025/03/04 14:13
     */
    @ApiOperation("获取礼物表分页")
    @SaCheckPermission("/live/gift/page")
    @Get
    @Mapping("/live/gift/page")
    public CommonResult<Page<Gift>> page(GiftPageParam giftPageParam) {
        return CommonResult.data(giftService.page(giftPageParam));
    }

    /**
     * 添加礼物表
     *
     * @author lgq
     * @date  2025/03/04 14:13
     */
    @ApiOperation("添加礼物表")
    @CommonLog("添加礼物表")
    @SaCheckPermission("/live/gift/add")
    @Post
    @Mapping("/live/gift/add")
    public CommonResult<String> add(GiftAddParam giftAddParam) {
        giftService.add(giftAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑礼物表
     *
     * @author lgq
     * @date  2025/03/04 14:13
     */
    @ApiOperation("编辑礼物表")
    @CommonLog("编辑礼物表")
    @SaCheckPermission("/live/gift/edit")
    @Post
    @Mapping("/live/gift/edit")
    public CommonResult<String> edit(GiftEditParam giftEditParam) {
        giftService.edit(giftEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除礼物表
     *
     * @author lgq
     * @date  2025/03/04 14:13
     */
    @ApiOperation("删除礼物表")
    @CommonLog("删除礼物表")
    @SaCheckPermission("/live/gift/delete")
    @Post
    @Mapping("/live/gift/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<GiftIdParam> giftIdParamList) {
        giftService.delete(giftIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取礼物表详情
     *
     * @author lgq
     * @date  2025/03/04 14:13
     */
    @ApiOperation("获取礼物表详情")
    @SaCheckPermission("/live/gift/detail")
    @Get
    @Mapping("/live/gift/detail")
    public CommonResult<Gift> detail(GiftIdParam giftIdParam) {
        return CommonResult.data(giftService.detail(giftIdParam));
    }
}
