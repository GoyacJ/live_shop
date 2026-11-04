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
package vip.xiaonuo.gen.modular.basic.controller;

import cn.hutool.core.lang.tree.Tree;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.Context;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.gen.modular.basic.entity.GenBasic;
import vip.xiaonuo.gen.modular.basic.param.*;
import vip.xiaonuo.gen.modular.basic.result.*;
import vip.xiaonuo.gen.modular.basic.service.GenBasicService;

import java.io.IOException;
import java.util.List;

/**
 * 代码生成基础控制器
 *
 * @author yubaoshan
 * @date 2022/10/25 22:33
 **/
@Api(tags = "代码生成基础控制器")
@Controller
@Valid
public class GenBasicController {

    @Inject
    private GenBasicService genBasicService;

    /**
     * 获取代码生成基础分页
     *
     * @author yubaoshan
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取代码生成基础分页")
    @Get
    @Mapping("/gen/basic/page")
    public CommonResult<Page<GenBasic>> page(GenBasicPageParam genBasicPageParam) {
        return CommonResult.data(genBasicService.page(genBasicPageParam));
    }

    /**
     * 添加代码生成基础
     *
     * @author yubaoshan
     * @date 2022/4/24 20:47
     */
    @ApiOperation("添加代码生成基础")
    @CommonLog("添加代码生成基础")
    @Post
    @Mapping("/gen/basic/add")
    public CommonResult<GenBasic> add(GenBasicAddParam genBasicAddParam) {
        return CommonResult.data(genBasicService.add(genBasicAddParam));
    }

    /**
     * 编辑代码生成基础
     *
     * @author yubaoshan
     * @date 2022/4/24 20:47
     */
    @ApiOperation("编辑代码生成基础")
    @CommonLog("编辑代码生成基础")
    @Post
    @Mapping("/gen/basic/edit")
    public CommonResult<GenBasic> edit(GenBasicEditParam genBasicEditParam) {
        return CommonResult.data(genBasicService.edit(genBasicEditParam));
    }

    /**
     * 删除代码生成基础
     *
     * @author yubaoshan
     * @date 2022/4/24 20:00
     */
    @ApiOperation("删除代码生成基础")
    @CommonLog("删除代码生成基础")
    @Post
    @Mapping("/gen/basic/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                              @Body CommonValidList<GenBasicIdParam> genBasicIdParamList) {
        genBasicService.delete(genBasicIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取代码生成基础详情
     *
     * @author yubaoshan
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取代码生成基础详情")
    @Get
    @Mapping("/gen/basic/detail")
    public CommonResult<GenBasic> detail(GenBasicIdParam genBasicIdParam) {
        return CommonResult.data(genBasicService.detail(genBasicIdParam));
    }

    /**
     * 获取所有表信息
     *
     * @author yubaoshan
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取所有表信息")
    @Get
    @Mapping("/gen/basic/tables")
    public CommonResult<List<GenBasicTableResult>> dbsTable() {
        return CommonResult.data(genBasicService.tables());
    }

    /**
     * 获取表内所有字段信息
     *
     * @author yubaoshan
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取表内所有字段信息")
    @Get
    @Mapping("/gen/basic/tableColumns")
    public CommonResult<List<GenBasicTableColumnResult>> tableColumns(GenBasicTableColumnParam genBasicTableColumnParam) {
        return CommonResult.data(genBasicService.tableColumns(genBasicTableColumnParam));
    }

    /**
     * 执行代码生成
     *
     * @author xuyuxiang
     * @date 2022/6/21 15:44
     **/
    @ApiOperation("执行代码生成（压缩包）")
    @CommonLog("执行代码生成（压缩包）")
    @Get
    @Mapping(value = "/gen/basic/execGenZip")
    public void execGenZip(GenBasicIdParam genBasicIdParam, Context response) throws IOException {
        genBasicService.execGenZip(genBasicIdParam, response);
    }

    /**
     * 执行代码生成
     *
     * @author yubaoshan
     * @date 2022/10/31 02:17
     **/
    @ApiOperation("执行代码生成（项目内）")
    @CommonLog("执行代码生成（项目内）")
    @Post
    @Mapping(value = "/gen/basic/execGenPro")
    public CommonResult<String> execGenPro(GenBasicIdParam genBasicIdParam, Context response) throws IOException {
        genBasicService.execGenPro(genBasicIdParam, response);
        return CommonResult.ok();
    }

    /**
     * 预览代码生成
     *
     * @author xuyuxiang
     * @date 2022/6/21 15:44
     **/
    @ApiOperation("预览代码生成")
    @CommonLog("预览代码生成")
    @Get
    @Mapping(value = "/gen/basic/previewGen")
    public CommonResult<GenBasicPreviewResult> previewGen(GenBasicIdParam genBasicIdParam) {
        return CommonResult.data(genBasicService.previewGen(genBasicIdParam));
    }

    /**
     * 获取所有移动端模块
     *
     * @author 每天一点
     * @date 2023/7/15 22:36
     */
    @ApiOperation("获取所有移动端模块")
    @Get
    @Mapping("/gen/basic/mobileModuleSelector")
    public CommonResult<List<GenBasicMobileModuleSelectorResult>> mobileModuleSelector() {
        return CommonResult.data(genBasicService.mobileModuleSelector());
    }

    /**
     * 获取所有模块
     *
     * @author yubaoshan
     * @date 2024/9/6 01:24
     */
    @ApiOperation("获取所有模块")
    @Get
    @Mapping("/gen/basic/moduleSelector")
    public CommonResult<List<GenBasicModuleSelectorResult>> moduleSelector() {
        return CommonResult.data(genBasicService.moduleSelector());
    }

    /**
     * 代码生成获取所有菜单树包括未授权的
     *
     * @author yubaoshan
     * @date 2024/9/6 01:24
     **/
    @ApiOperation("代码生成获取所有菜单树包括未授权的")
    @Get
    @Mapping("/gen/basic/menuTreeSelector")
    public CommonResult<List<Tree<String>>> menuTreeSelector(@Validated GenBasicSelectorMenuParam genBasicSelectorMenuParam) {
        return CommonResult.data(genBasicService.menuTreeSelector(genBasicSelectorMenuParam));
    }
}

