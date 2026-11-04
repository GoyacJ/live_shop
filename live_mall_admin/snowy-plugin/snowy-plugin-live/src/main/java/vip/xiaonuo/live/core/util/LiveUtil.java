package vip.xiaonuo.live.core.util;

import org.apache.commons.codec.digest.DigestUtils;
import vip.xiaonuo.live.core.config.LiveConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LiveUtil {
    /**
     * 生成推流地址
     * @return 推流地址
     */
    public static Map<String, String> createUrl(LiveConfig liveConfig) {
        validateParams(liveConfig.getLiveAppName(), liveConfig.getStreamName());
        long timestamp = System.currentTimeMillis() / 1000 + liveConfig.getLiveIdentUrlValidTime();
        String rand = generateRandom();
        // String authKey = generateAuthKey(liveConfig.getLiveAppName(), liveConfig.getStreamName(), timestamp, rand, liveConfig.getLivePushIdentKey());
        String authKey = generateAuthKey(liveConfig.getLiveAppName(), rand, timestamp, rand, liveConfig.getLivePushIdentKey());
        // String pullAuthKey = generateAuthKey(liveConfig.getLiveAppName(), liveConfig.getStreamName(), timestamp, rand, liveConfig.getLivePullIdentKey());
        String pullAuthKey = generateAuthKey(liveConfig.getLiveAppName(), rand, timestamp, rand, liveConfig.getLivePullIdentKey());

        Map<String, String> url = new HashMap<>();
        url.put("push", String.format("rtmp://%s/%s/%s?auth_key=%d-%s-0-%s",
                liveConfig.getLivePushDomain(),
                liveConfig.getLiveAppName(),
                // liveConfig.getStreamName(),
                rand,
                timestamp,
                rand,
                authKey));
        url.put("pull", String.format("rtmp://%s/%s/%s?auth_key=%d-%s-0-%s",
                liveConfig.getLivePullDomain(),
                liveConfig.getLiveAppName(),
                // liveConfig.getStreamName(),
                rand,
                timestamp,
                rand,
                pullAuthKey));

        return url;
    }

    /**
     * 生成拉流地址
     * @return 拉流地址
     */
    public static String createPullUrl(LiveConfig liveConfig) {
        validateParams(liveConfig.getLiveAppName(), liveConfig.getStreamName());
        validateProtocol(liveConfig.getProtocol());
        long timestamp = System.currentTimeMillis() / 1000 + liveConfig.getLiveIdentUrlValidTime();
        String rand = "0";  // generateRandom();
        String authKey = generateAuthKey(liveConfig.getLiveAppName(), liveConfig.getStreamName(), timestamp, rand,liveConfig.getLivePullIdentKey());
        String extension = getProtocolExtension(liveConfig.getProtocol());
        String baseUrl = buildBasePullUrl(liveConfig.getProtocol(),liveConfig.getLivePullDomain());
        return String.format("rtmp://%s/%s/%s?auth_key=%d-%s-0-%s",
                liveConfig.getLivePullDomain(),
                liveConfig.getLiveAppName(),
                liveConfig.getStreamName(),
                timestamp,
                rand,
                authKey);
    }

    // 生成鉴权Key
    private static String generateAuthKey(String appName, String streamName, long timestamp, String rand,String secret) {
        String path = String.format("/%s/%s", appName, streamName);
        //String secret = LiveConfig.getLivePushIdentKey();
        String raw = path + "-" + timestamp + "-" + rand + "-0-" + secret;
        return DigestUtils.md5Hex(raw);
    }

    // 验证协议有效性
    private static void validateProtocol(String protocol) {
        if (!protocol.equals("rtmp") &&
                !protocol.equals("flv") &&
                !protocol.equals("m3u8")) {
            throw new IllegalArgumentException("Unsupported protocol: " + protocol);
        }
    }

    // 获取协议扩展名
    private static String getProtocolExtension(String protocol) {
        switch (protocol) {
            case "rtmp": return "rtmp";
            case "flv": return "flv";
            case "m3u8": return "m3u8";
            default: throw new IllegalArgumentException("Invalid protocol");
        }
    }

    // 构建拉流基础URL
    private static String buildBasePullUrl(String protocol,String domain) {
        //String domain = config.getLivePullDomain();
        if (protocol.equals("rtmp")) {
            return "rtmp://" + domain;
        } else {
            return "http://" + domain;
        }
    }

    // 生成10位随机字符串
    private static String generateRandom() {
        return UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 10);
    }

    // 参数校验
    private static void validateParams(String appName, String streamName) {
        if (isEmpty(appName) || isEmpty(streamName)) {
            throw new IllegalArgumentException("appName/streamName cannot be empty");
        }
    }

    private static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
