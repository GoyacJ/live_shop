package api.shop.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/13 09:12
 */
@Data
public class SpecialTopicImageResDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -7691883620733548094L;

    private String specialTopicId;

    private String specialTopicName;

    @ApiModelProperty("是否轮播 0是1否")
    private Integer carousel;

    private String img;
}
