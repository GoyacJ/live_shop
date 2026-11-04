/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.core.handler;

import cn.dev33.satoken.exception.SaTokenException;
import lombok.extern.slf4j.Slf4j;
import vip.xiaonuo.auth.core.util.AuthExceptionUtil;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.util.CommonServletUtil;

/**
 * 全局异常处理工具类，将异常转为通用结果
 *
 * @author xuyuxiang
 * @date 2021/12/18 16:44
 */
@Slf4j
public class GlobalExceptionUtil {

    /**
     * 根据错误类型获取对应的CommonResult
     *
     * @author xuyuxiang
     * @date 2021/10/11 15:52
     **/
    public static CommonResult<String> getCommonResult(Exception e) {
        CommonResult<String> commonResult;
        if (e instanceof SaTokenException) {
            // 如果是SaToken相关异常，则由AuthExceptionUtil处理
            return AuthExceptionUtil.getCommonResult(e);
        }  else if (e instanceof CommonException) {
            // 通用业务异常，直接返回给前端
            CommonException commonException = (CommonException) e;
            commonResult = CommonResult.get(commonException.getCode(), commonException.getMsg(), null);
        }  else {
            // 未知异常打印详情
            log.error(">>> 服务器未知异常，请求地址：{}，具体信息：", CommonServletUtil.getRequest().url(), e);
            // 未知异常返回服务器异常
            commonResult = CommonResult.error("服务器异常");
        }
        return commonResult;
    }
}
