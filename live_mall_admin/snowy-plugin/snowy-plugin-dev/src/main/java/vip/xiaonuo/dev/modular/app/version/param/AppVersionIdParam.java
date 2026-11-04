package vip.xiaonuo.dev.modular.app.version.param;

import org.noear.solon.validation.annotation.NotBlank;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/27 14:44
 */
public record AppVersionIdParam(

        @NotBlank(message = "id不能为空")
        String id
) {
}
