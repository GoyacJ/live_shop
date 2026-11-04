package vip.xiaonuo.content.modular.action.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.common.pojo.CommonEntity;
import vip.xiaonuo.content.api.enums.ContentActionType;

import java.time.LocalDateTime;

/**
 * <p>用户行为表</p>
 *
 * @author goya
 * @since 2025/6/11 10:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table("content_user_action")
public class ContentUserAction extends CommonEntity {

    @Id
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("视频ID")
    private String videoId;

    @ApiModelProperty("操作类型")
    private ContentActionType contentActionType;

    @ApiModelProperty("操作时间")
    private LocalDateTime actionTime;

    @ApiModelProperty("是否生效")
    private SimpleStatus isActive;
}
