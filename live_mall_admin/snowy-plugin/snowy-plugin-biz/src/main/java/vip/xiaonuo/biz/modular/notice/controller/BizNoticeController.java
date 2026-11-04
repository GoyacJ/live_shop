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
package vip.xiaonuo.biz.modular.notice.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.biz.modular.notice.entity.BizNotice;
import vip.xiaonuo.biz.modular.notice.param.BizNoticeAddParam;
import vip.xiaonuo.biz.modular.notice.param.BizNoticeEditParam;
import vip.xiaonuo.biz.modular.notice.param.BizNoticeIdParam;
import vip.xiaonuo.biz.modular.notice.param.BizNoticePageParam;
import vip.xiaonuo.biz.modular.notice.service.BizNoticeService;
import vip.xiaonuo.biz.modular.notice.wrapper.BizNoticeWrapper;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.annotation.CommonWrapper;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;

/**
 * 通知公告控制器
 *
 * @author yubaoshan
 * @date  2024/07/11 14:46
 */
@Api(tags = "通知公告控制器")
@Controller
@Valid
public class BizNoticeController {

    @Inject
    private BizNoticeService bizNoticeService;

    /**
     * 获取通知公告分页
     *
     * @author chengchuanyao
     * @date  2024/07/22 14:55
     */
    @ApiOperation("获取通知公告分页")
    @SaCheckPermission("/biz/notice/page")
    @Get
    @Mapping("/biz/notice/page")
    public CommonResult<Page<BizNotice>> page(BizNoticePageParam bizNoticePageParam) {
        return CommonResult.data(bizNoticeService.page(bizNoticePageParam));
    }

    /**
     * 添加通知公告
     *
     * @author chengchuanyao
     * @date  2024/07/22 14:55
     */
    @ApiOperation("添加通知公告")
    @CommonLog("添加通知公告")
    @SaCheckPermission("/biz/notice/add")
    @Post
    @Mapping("/biz/notice/add")
    public CommonResult<String> add(BizNoticeAddParam bizNoticeAddParam) {
        bizNoticeService.add(bizNoticeAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑通知公告
     *
     * @author chengchuanyao
     * @date  2024/07/22 14:55
     */
    @ApiOperation("编辑通知公告")
    @CommonLog("编辑通知公告")
    @SaCheckPermission("/biz/notice/edit")
    @Post
    @Mapping("/biz/notice/edit")
    public CommonResult<String> edit(BizNoticeEditParam bizNoticeEditParam) {
        bizNoticeService.edit(bizNoticeEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除通知公告
     *
     * @author chengchuanyao
     * @date  2024/07/22 14:55
     */
    @ApiOperation("删除通知公告")
    @CommonLog("删除通知公告")
    @SaCheckPermission("/biz/notice/delete")
    @Post
    @Mapping("/biz/notice/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<BizNoticeIdParam> bizNoticeIdParamList) {
        bizNoticeService.delete(bizNoticeIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取通知公告详情
     *
     * @author chengchuanyao
     * @date  2024/07/22 14:55
     */
    @ApiOperation("获取通知公告详情")
    @SaCheckPermission("/biz/notice/detail")
    @Mapping("/biz/notice/detail")
    @CommonWrapper(value = BizNoticeWrapper.class)
    public CommonResult<BizNotice> detail(BizNoticeIdParam bizNoticeIdParam) {
        return CommonResult.data(bizNoticeService.detail(bizNoticeIdParam));
    }

    /**
     * 禁用通知公告
     *
     * @author yubaoshan
     * @date  2024/07/11 14:46
     */
    @ApiOperation("禁用通知公告")
    @CommonLog("禁用通知公告")
    @Get
    @SaCheckPermission("/biz/notice/disableStatus")
    @Mapping("/biz/notice/disableStatus")
    public CommonResult<String> disableStatus(@Validated BizNoticeIdParam bizNoticeIdParam) {
        bizNoticeService.disableStatus(bizNoticeIdParam);
        return CommonResult.ok();
    }

    /**
     * 启用通知公告
     *
     * @author yubaoshan
     * @date  2024/07/11 14:46
     */
    @ApiOperation("启用通知公告")
    @CommonLog("启用通知公告")
    @SaCheckPermission("/biz/notice/enableStatus")
    @Post
    @Mapping("/biz/notice/enableStatus")
    public CommonResult<String> enableStatus(@Validated BizNoticeIdParam bizNoticeIdParam) {
        bizNoticeService.enableStatus(bizNoticeIdParam);
        return CommonResult.ok();
    }
}
