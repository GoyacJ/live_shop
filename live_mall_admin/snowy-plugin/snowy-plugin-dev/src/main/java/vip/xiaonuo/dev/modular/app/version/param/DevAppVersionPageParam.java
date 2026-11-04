package vip.xiaonuo.dev.modular.app.version.param;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/27 14:57
 */
public record DevAppVersionPageParam(
        Integer current,
        Integer size,
        String sortField,
        String sortOrder
) {
}
