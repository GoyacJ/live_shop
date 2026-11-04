package vip.xiaonuo.manage.modular.deviceinfo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.xiaonuo.common.pojo.CommonEntity;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * <p>移动端设备信息</p>
 *
 * @author goya
 * @since 2025/4/30 10:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table("mobile_device_info")
public class MobileDeviceInfo extends CommonEntity {

    @Serial
    private static final long serialVersionUID = 1055548685432917546L;

    @Id
    @ApiModelProperty(value = "ID", position = 1)
    private String id;

    @ApiModelProperty("设备Id")
    private String clientId;

    @ApiModelProperty("品牌")
    private String brand;

    @ApiModelProperty("型号")
    private String model;

    @ApiModelProperty("系统版本")
    private String version;

    @ApiModelProperty("是否模拟器 0: 否 1: 是")
    private Integer simulator;

    @ApiModelProperty("用户Id")
    private String userId;

    @ApiModelProperty("首次访问时间")
    private LocalDateTime firstAccessTime;

    @ApiModelProperty("最后一次访问时间")
    private LocalDateTime lastAccessTime;
}
