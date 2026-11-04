package vip.xiaonuo.mobile.modular.index.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/12 11:28
 */
@Data
public class PrivacyAgreementVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户协议")
    private String userAgreement;

    @ApiModelProperty("隐私政策")
    private String privacyPolicy;
}
