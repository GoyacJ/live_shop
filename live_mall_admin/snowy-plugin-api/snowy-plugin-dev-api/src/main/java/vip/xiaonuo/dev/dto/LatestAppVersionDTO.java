package vip.xiaonuo.dev.dto;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/27 16:07
 */
public record LatestAppVersionDTO(

        AppVersionInfo android,
        AppVersionInfo ios

) {

    public record AppVersionInfo(
            String appType,
            String appVersion,
            String versionCode,
            String appUrl,
            String appDesc
    ) {
    }
}
