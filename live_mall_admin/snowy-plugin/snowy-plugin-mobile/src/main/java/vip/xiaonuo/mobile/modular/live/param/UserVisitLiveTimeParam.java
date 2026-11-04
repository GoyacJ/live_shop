package vip.xiaonuo.mobile.modular.live.param;

import org.noear.solon.validation.annotation.NotBlank;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/30 17:35
 */
public record UserVisitLiveTimeParam(
        @NotBlank(message = "用户Id不能为空")
        String userId,

        @NotBlank(message = "直播Id")
        String liveId,

        long visitTime
) {
}
