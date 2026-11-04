package vip.xiaonuo.dev.modular.app.version.param;

import io.swagger.annotations.ApiModelProperty;
import org.noear.solon.validation.annotation.NotBlank;
import org.noear.solon.validation.annotation.NotNull;
import vip.xiaonuo.dev.core.enums.AppType;
import vip.xiaonuo.dev.modular.app.version.entity.AppVersion;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/27 14:38
 */
public record AppVersionAddParam(

        @ApiModelProperty(value = "id", required = true, position = 0)
        String id,

        @ApiModelProperty(value = "版本名", required = true, position = 1)
        @NotBlank(message = "版本名不能为空")
        String versionName,

        @ApiModelProperty(value = "版本号", required = true, position = 1)
        @NotBlank(message = "版本号不能为空")
        String versionCode,

        @ApiModelProperty(value = "应用类型", required = true, position = 2)
        @NotNull(message = "应用类型不能为空")
        AppType appType,

        @ApiModelProperty(value = "下载地址", required = true, position = 3)
        @NotBlank(message = "下载地址不能为空")
        String appUrl,

        @ApiModelProperty(value = "版本描述", required = true, position = 4)
        String appDesc,

        @ApiModelProperty(value = "是否强制更新", required = true, position = 5)
        boolean forceUpdate,

        @ApiModelProperty(value = "排序码", required = true, position = 6)
        Integer sortCode
) {

    public AppVersion toAppVersion() {
        AppVersion version = new AppVersion();
        version.setId(id);
        version.setVersionName(versionName);
        version.setVersionCode(versionCode);
        version.setAppType(appType);
        version.setAppUrl(appUrl);
        version.setAppDesc(appDesc);
        version.setForceUpdate(forceUpdate);
        version.setSortCode(sortCode);
        return version;
    }
}
