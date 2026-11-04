package vip.xiaonuo.shop.modular.specialtopic.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.shop.modular.specialtopic.entity.MallGoodsSpecialTopic;
import vip.xiaonuo.shop.modular.specialtopic.entity.MallGoodsSpecialTopicDetail;
import vip.xiaonuo.shop.modular.specialtopic.param.*;
import vip.xiaonuo.shop.modular.specialtopic.service.MallGoodsSpecialTopicService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/9 16:38
 */
@Slf4j
@Api(tags = "专题信息控制器")
@Controller
@Valid
@Mapping("/shop/special/topic")
public class MallGoodsSpecialTopicController {

    @Inject
    private MallGoodsSpecialTopicService mallGoodsSpecialTopicService;

    @ApiOperation("获取专题分页")
    @SaCheckPermission("/shop/special/topic/page")
    @Get
    @Mapping("/page")
    public CommonResult<Page<MallGoodsSpecialTopic>> page(MallGoodsSpecialTopicPageQueryParam param) {
        return CommonResult.data(mallGoodsSpecialTopicService.page(param));
    }

    @ApiOperation("添加专题")
    @CommonLog("添加专题")
    @SaCheckPermission("/shop/special/topic/add")
    @Post
    @Mapping("/add")
    public CommonResult<Void> add(MallGoodsSpecialTopicAddParam param) {
        mallGoodsSpecialTopicService.add(param);
        return CommonResult.ok();
    }

    @ApiOperation("编辑专题")
    @CommonLog("编辑专题")
    @SaCheckPermission("/shop/special/topic/edit")
    @Post
    @Mapping("/edit")
    public CommonResult<Void> edit(MallGoodsSpecialTopicEditParam param) {
        mallGoodsSpecialTopicService.edit(param);
        return CommonResult.ok();
    }

    @ApiOperation("删除专题")
    @CommonLog("删除专题")
    @SaCheckPermission("/shop/special/topic/delete")
    @Post
    @Mapping("/delete")
    public CommonResult<Void> delete(@NotEmpty(message = "集合不能为空")
                                     CommonValidList<MallGoodsSpecialTopicIdParam> param) {
        mallGoodsSpecialTopicService.delete(param);
        return CommonResult.ok();
    }

    @ApiOperation("绑定商品")
    @CommonLog("绑定商品")
    @SaCheckPermission("/shop/special/topic/bind")
    @Post
    @Mapping("/bind")
    public CommonResult<Void> bind(MallGoodsSpecialTopicBindParam param) {
        mallGoodsSpecialTopicService.bind(param);
        return CommonResult.ok();
    }

    @ApiOperation("删除专题绑定商品")
    @CommonLog("删除专题绑定商品")
    @SaCheckPermission("/shop/special/topic/detail/delete")
    @Post
    @Mapping("/detail/delete")
    public CommonResult<Void> detailDelete(@NotEmpty(message = "集合不能为空")
                                     CommonValidList<MallGoodsSpecialTopicIdParam> param) {
        mallGoodsSpecialTopicService.detailDelete(param);
        return CommonResult.ok();
    }

    @ApiOperation("获取专题详情商品分页")
    @SaCheckPermission("/shop/special/topic/detail/page")
    @Get
    @Mapping("/detail/page")
    public CommonResult<Page<MallGoodsSpecialTopicDetail>> detailPage(MallGoodsSpecialTopicDetailPageQueryParam param) {
        return CommonResult.data(mallGoodsSpecialTopicService.detailPage(param));
    }
}
