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
package vip.xiaonuo;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.annotation.*;
import vip.xiaonuo.common.annotation.CommonWrapper;
import vip.xiaonuo.core.config.CommonWrapperInterceptor;

/**
 * Solon 方式启动类
 *
 * @author xuyuxiang
 * @date 2021/12/18 16:57
 */
@Slf4j
@Controller
public class Application {

    /* 解决druid 日志报错：discard long time none received connection:xxx */
    static {
        System.setProperty("druid.mysql.usePingMethod","false");
    }

    /**
     * 主启动函数
     *
     * @author xuyuxiang noear
     * @date 2022/7/30 21:42
     */
    public static void main(String[] args) {
        Solon.start(Application.class, args, app->{
            app.context().beanInterceptorAdd(CommonWrapper.class, new CommonWrapperInterceptor());
        });
        log.info(">>> {}", Application.class.getSimpleName().toUpperCase() + " STARTING SUCCESS");
    }

    /**
     * 首页
     *
     * @author xuyuxiang
     * @date 2022/7/8 14:22
     **/
    @Get
    @Mapping("/")
    public String index() {
        return "WELCOME";
    }
}
