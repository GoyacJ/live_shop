package vip.xiaonuo.dev.dto;

import com.mybatisflex.annotation.Id;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/12 11:34
 */
@Data
public class DevConfigDTO implements Serializable {

    /** id */
    @ApiModelProperty(value = "id", position = 1)
    @Id
    private String id;

    /** 配置键 */
    @ApiModelProperty(value = "配置键", position = 2)
    private String configKey;

    /** 配置值 */
    @ApiModelProperty(value = "配置值", position = 3)
    private String configValue;

    /** 分类 */
    @ApiModelProperty(value = "分类", position = 4)
    private String category;

    /** 备注 */
    @ApiModelProperty(value = "备注", position = 5)
    private String remark;

    /** 排序码 */
    @ApiModelProperty(value = "排序码", position = 6)
    private Integer sortCode;

    /** 扩展信息 */
    @ApiModelProperty(value = "扩展信息", position = 7)
    private String extJson;
}
