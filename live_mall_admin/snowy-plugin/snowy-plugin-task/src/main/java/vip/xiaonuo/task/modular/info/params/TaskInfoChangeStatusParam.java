package vip.xiaonuo.task.modular.info.params;

import vip.xiaonuo.common.enums.SimpleStatus;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/28 15:26
 */
public record TaskInfoChangeStatusParam(
        String id,
        SimpleStatus status
) {
}
