package vip.xiaonuo.content.modular.action.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.common.pojo.CommonEntity;

import java.time.LocalDateTime;

/**
 * <p>追剧表</p>
 *
 * @author goya
 * @since 2025/6/11 10:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table("content_shop_play_follow")
public class ContentShopPlayFollow extends CommonEntity {

    @Id
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("操作时间")
    private LocalDateTime operationTime;

    @ApiModelProperty("短剧Id")
    private String shopPlayId;

    @ApiModelProperty("是否生效")
    private SimpleStatus isActive;
}
