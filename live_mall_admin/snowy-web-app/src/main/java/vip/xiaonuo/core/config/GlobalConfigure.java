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
package vip.xiaonuo.core.config;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.FlexGlobalConfig;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Init;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.Action;
import org.noear.solon.core.handle.Context;
import org.noear.solon.web.staticfiles.StaticMappings;
import org.noear.solon.web.staticfiles.repository.ClassPathStaticRepository;
import vip.xiaonuo.common.annotation.CommonNoRepeat;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.listener.CommonDataChangeEventCenter;
import vip.xiaonuo.common.listener.CommonDataChangeListener;
import vip.xiaonuo.common.pojo.CommonEntity;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.util.CommonTimeFormatUtil;
import vip.xiaonuo.dev.modular.log.entity.DevLog;
import vip.xiaonuo.sys.modular.index.result.SysIndexMessageDetailResult;
import vip.xiaonuo.sys.modular.index.result.SysIndexOpLogListResult;
import vip.xiaonuo.sys.modular.index.result.SysIndexVisLogListResult;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Snowy配置
 *
 * @author xuyuxiang
 * @date 2021/10/9 14:24
 **/
@Configuration
public class GlobalConfigure {

    private static final String COMMON_REPEAT_SUBMIT_CACHE_KEY = "common-repeatSubmit:";

    @Inject
    private CommonCacheOperator commonCacheOperator;

    /**
     * 静态资源映射
     *
     * @author xuyuxiang
     * @date 2022/7/25 15:16
     **/
    @Bean
    public void addResourceHandlers() {
        StaticMappings.add("/doc.html", new ClassPathStaticRepository("/META-INF/resources/"));
        StaticMappings.add("/webjars/", new ClassPathStaticRepository("/META-INF/resources/webjars/"));
    }

    /**
     * 添加节流防抖拦截器
     *
     * @author xuyuxiang
     * @date 2022/6/20 15:18
     **/
    @Bean
    public void addInterceptors() {
        Solon.app().routerInterceptor((ctx, mainHandler, chain) -> {
            Action action = ctx.action();
            if (action != null) {
                Method method = action.method().getMethod();
                CommonNoRepeat annotation = method.getAnnotation(CommonNoRepeat.class);
                if (ObjectUtil.isNotEmpty(annotation)) {
                    JSONObject repeatSubmitJsonObject = this.isRepeatSubmit(ctx, annotation);
                    if (repeatSubmitJsonObject.getBool("repeat")) {
                        ctx.charset(CharsetUtil.UTF_8);
                        ctx.contentType(ContentType.JSON.toString());
                        ctx.outputAsJson(JSONUtil.toJsonStr(CommonResult.error("请求过于频繁，请" + repeatSubmitJsonObject.getStr("time") + "后再试")));
                        return;
                    }
                }
            }
            chain.doIntercept(ctx, mainHandler);
        });
    }

    protected JSONObject isRepeatSubmit(Context ctx, CommonNoRepeat annotation) {
        JSONObject jsonObject = JSONUtil.createObj();
        jsonObject.set("repeatParam", JSONUtil.toJsonStr(ctx.paramMap()));
        jsonObject.set("repeatTime", DateUtil.current());
        String url = ctx.url();

        // 获取该接口缓存的限流数据
        Object cacheObj = commonCacheOperator.get(COMMON_REPEAT_SUBMIT_CACHE_KEY + url);
        if (ObjectUtil.isNotEmpty(cacheObj)) {
            JSONObject cacheJsonObject = JSONUtil.parseObj(cacheObj);
            if (cacheJsonObject.containsKey(url)) {
                JSONObject existRepeatJsonObject = cacheJsonObject.getJSONObject(url);
                // 如果与上次参数一致，且时间间隔小于要求的限流时长，则判定为重复提交
                if (jsonObject.getStr("repeatParam").equals(existRepeatJsonObject.getStr("repeatParam"))) {
                    long interval = jsonObject.getLong("repeatTime") - existRepeatJsonObject.getLong("repeatTime");
                    if (interval < annotation.interval()) {
                        long secondsParam = (annotation.interval() - interval) / 1000;
                        if (secondsParam == 0) {
                            return JSONUtil.createObj().set("repeat", false);
                        } else {
                            return JSONUtil.createObj().set("repeat", true).set("time", CommonTimeFormatUtil.formatSeconds(secondsParam));
                        }
                    }
                }
            }
        }
        // 缓存最新的该接口的限流数据，为防止缓存的数据过多，缓存时效为1小时
        commonCacheOperator.put(COMMON_REPEAT_SUBMIT_CACHE_KEY + url, JSONUtil.createObj().set(url, jsonObject), 60 * 60);
        return JSONUtil.createObj().set("repeat", false);
    }

    /**
     * 注册数据变化事件中心 事件发布器
     *
     * @author xuyuxiang
     * @date 2023/3/3 14:27
     **/
    @Bean
    public void registerListenerList(List<CommonDataChangeListener> dataChangeListenerList) {
        CommonDataChangeEventCenter.registerListenerList(dataChangeListenerList);
    }

    /**
     * 全局配置flex监听
     *
     * @author ChengChuanYao
     * @date 2024/6/22 15:33
     */
    @Init
    public void registerListenerByFlex() {
        CustomMetaObjectListener customMetaObjectListener = new CustomMetaObjectListener();
        FlexGlobalConfig config = FlexGlobalConfig.getDefaultConfig();

        config.setDeletedValueOfLogicDelete("DELETED");
        config.setNormalValueOfLogicDelete("NOT_DELETE");

        config.registerInsertListener(customMetaObjectListener, CommonEntity.class, SysIndexVisLogListResult.class, SysIndexMessageDetailResult.class, SysIndexOpLogListResult.class, DevLog.class);
        config.registerUpdateListener(customMetaObjectListener, CommonEntity.class, SysIndexVisLogListResult.class, SysIndexMessageDetailResult.class, SysIndexOpLogListResult.class, DevLog.class
        );
    }

}
