package vip.xiaonuo.common.util;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {

    /**
     * 私有构造函数，防止工具类被实例化。
     */
    private ResponseUtil() {
    }

    /**
     * 生成成功响应的Map对象。
     * @param data 成功响应的数据
     * @return 包含成功状态码、消息和数据的Map对象
     */
    public static Map<String, String> successResponse(Object data) {
        Map<String, String> response = new HashMap<>();
        response.put("code", "200");
        response.put("message", "成功");
        if (data != null) {
            response.put("data", data.toString());
        }
        return response;
    }

    /**
     * 生成错误响应的Map对象。
     * @param code 错误状态码
     * @param message 错误消息
     * @param data 错误响应的数据
     * @return 包含错误状态码、消息和数据的Map对象
     */
    public static Map<String, String> errorResponse(Integer code, String message, Object data) {
        Map<String, String> response = new HashMap<>();
        response.put("code", String.valueOf(code));
        response.put("message", message);
        if (data != null) {
            response.put("data", data.toString());
        }
        return response;
    }

    /**
     * 返回成功响应
     * @return 成功响应字符串
     */
    public static String success() {
        return "SUCCESS";
    }

    /**
     * 返回成功响应
     * @return 成功响应字符串
     */
    public static String success(String message) {
        return "SUCCESS : " + message ;
    }

    /**
     * 返回失败响应
     * @param message 成功原因
     * @return 成功响应字符串
     */
    public static String error(String message) {
        return message;
    }

    /**
     * 返回失败响应
     * @param message 失败原因
     * @return 失败响应字符串
     */
    public static String failure(Integer Code,String message) {
        return "FAILURE: code = " + Code + ", message = " + message;
    }
}
