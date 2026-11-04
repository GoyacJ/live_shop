package manage.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/30 11:58
 */
@Data
public class UserStatisticsInfoDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3673668520125829113L;

    private Integer h5VisitsNo;
    private Integer appRegisterNo;
    private Integer appVisitsNo;

    public UserStatisticsInfoDTO() {
        this.h5VisitsNo = 0;
        this.appRegisterNo = 0;
        this.appVisitsNo = 0;
    }
}
