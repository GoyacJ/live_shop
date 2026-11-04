package vip.xiaonuo.dev.modular.app.version.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.xiaonuo.common.pojo.CommonEntity;
import vip.xiaonuo.dev.core.enums.AppType;

import java.io.Serial;

/**
 * <p>App版本管理</p>
 *
 * @author goya
 * @since 2025/4/27 09:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table("dev_app_version")
public class AppVersion extends CommonEntity {

    @Serial
    private static final long serialVersionUID = 3364369565213068776L;

    /**
     * ID
     */
    @Id
    @ApiModelProperty(value = "ID", position = 1)
    private String id;

    @ApiModelProperty(value = "版本名", position = 2)
    private String versionName;

    @ApiModelProperty(value = "版本号", position = 2)
    private String versionCode;

    @ApiModelProperty(value = "应用类型", position = 3)
    private AppType appType;

    @ApiModelProperty(value = "下载地址", position = 4)
    private String appUrl;

    @ApiModelProperty(value = "版本描述", position = 5)
    private String appDesc;

    @ApiModelProperty(value = "是否强制更新", position = 6)
    private Boolean forceUpdate;

    /** 排序码 */
    @ApiModelProperty(value = "排序码", position = 7)
    private Integer sortCode;


}
