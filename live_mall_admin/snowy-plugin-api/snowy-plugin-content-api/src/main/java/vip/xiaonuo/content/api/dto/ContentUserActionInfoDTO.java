package vip.xiaonuo.content.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 13:53
 */
@Data
@Builder
public class ContentUserActionInfoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是否点赞")
    private boolean like;

    @ApiModelProperty(value = "是否收藏")
    private boolean collect;

    @ApiModelProperty(value = "是否评论")
    private boolean comment;

    @ApiModelProperty(value = "是否分享")
    private boolean share;

    @ApiModelProperty(value = "是否举报")
    private boolean report;
}
