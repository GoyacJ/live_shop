package vip.xiaonuo.dev.modular.app.version.controller;

import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.UploadedFile;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.dev.api.DevConfigApi;
import vip.xiaonuo.dev.modular.app.version.entity.AppVersion;
import vip.xiaonuo.dev.modular.app.version.param.AppVersionAddParam;
import vip.xiaonuo.dev.modular.app.version.param.AppVersionIdParam;
import vip.xiaonuo.dev.modular.app.version.param.DevAppVersionPageParam;
import vip.xiaonuo.dev.modular.app.version.service.AppVersionService;
import vip.xiaonuo.dev.modular.file.entity.DevFile;
import vip.xiaonuo.dev.modular.file.enums.DevFileEngineTypeEnum;
import vip.xiaonuo.dev.modular.file.service.DevFileService;

/**
 * <p>App版本管理控制器</p>
 *
 * @author goya
 * @since 2025/4/27 14:32
 */
@Api(tags = "App版本管理控制器")
@Controller
@Valid
@Mapping("/dev/app/version")
@Slf4j
public class AppVersionController {

    @Inject
    private AppVersionService appVersionService;

    @Inject
    private DevFileService devFileService;

    @Inject
    private DevConfigApi devConfigApi;

    private static final String SNOWY_SYS_DEFAULT_FILE_ENGINE_KEY = "SNOWY_SYS_DEFAULT_FILE_ENGINE";


    /**
     * 添加App版本
     *
     * @param param param
     * @return 结果
     */
    @ApiOperation("添加版本")
    @CommonLog("添加版本")
    @Post
    @Mapping("/add")
    public CommonResult<Void> add(@Validated AppVersionAddParam param) {
        if (appVersionService.add(param)) {
            return CommonResult.ok();
        } else {
            return CommonResult.error();
        }
    }

    @ApiOperation("编辑版本")
    @CommonLog("编辑版本")
    @Post
    @Mapping("/edit")
    public CommonResult<String> edit(@Validated AppVersionAddParam param) {
        if (appVersionService.edit(param)) {
            return CommonResult.ok();
        } else {
            return CommonResult.error();
        }
    }

    @ApiOperation("删除版本")
    @CommonLog("删除版本")
    @Post
    @Mapping("/delete")
    public CommonResult<String> delete(@Validated @NotEmpty(message = "集合不能为空")
                                       CommonValidList<AppVersionIdParam> list) {
        if (appVersionService.delete(list)) {
            return CommonResult.ok();
        } else {
            return CommonResult.error();
        }
    }

    /**
     * 移动端修改用户头像
     */
    @ApiOperation("apk版本上传")
    @CommonLog("apk版本上传")
    @Post
    @Mapping("/upload")
    public CommonResult<String> uploadVersion(Context context) {

        try {
            UploadedFile version = context.file("version");
            if (version == null) {
                return CommonResult.error("请上传图片");
            }
            //上传文件信息
            String url = devFileService.uploadReturnId(DevFileEngineTypeEnum.MINIO.getValue(), version);

            DevFile byId = devFileService.getById(url);

            return CommonResult.data(byId.getDownloadPath());
        } catch (Exception e) {
            log.error("文件上传异常", e.getMessage(), e);
            return CommonResult.error("请上传图片");
        }
    }

    /**
     * 获取详情
     */
    @ApiOperation("获取详情")
    @Post
    @Mapping("/detail")
    public CommonResult<AppVersion> detail(@Validated AppVersionIdParam param) {
        return CommonResult.data(appVersionService.detail(param));
    }

    @ApiOperation("分页")
    @Get
    @Mapping("/page")
    public CommonResult<Page<AppVersion>> page(DevAppVersionPageParam param) {
        return CommonResult.data(appVersionService.page(param));
    }

}
