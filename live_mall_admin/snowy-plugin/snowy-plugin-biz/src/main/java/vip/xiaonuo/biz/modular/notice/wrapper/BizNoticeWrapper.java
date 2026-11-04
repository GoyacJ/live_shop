package vip.xiaonuo.biz.modular.notice.wrapper;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import vip.xiaonuo.biz.modular.notice.entity.BizNotice;
import vip.xiaonuo.common.annotation.CommonWrapperField;
import vip.xiaonuo.common.pojo.CommonWrapperInterface;
import vip.xiaonuo.sys.api.SysUserApi;

import java.util.Optional;

/**
 * 通知公告包装类
 *
 * @author chengchuanyao
 * @date 2025/1/6 16:32
 */
@Slf4j
public class BizNoticeWrapper implements CommonWrapperInterface<BizNotice> {

    private static SysUserApi userApi;

    static {
        Solon.context().getBeanAsync(SysUserApi.class, bean -> {
            userApi = bean;
        });
    }

    @Override
    public JSONObject doWrap(BizNotice bizNotice) {
        // 后期可通过反射进行处理
        JSONObject entries = new JSONObject();

        Optional.ofNullable(bizNotice.getCreateUser()).ifPresent(createUser -> {
            CommonWrapperField wrapperField = null;
            try {
                wrapperField = bizNotice.getClass().getDeclaredField("createUserName").getAnnotation(CommonWrapperField.class);
            } catch (NoSuchFieldException e) {
                setLog(createUser);
            }
            String userName = userApi.getUserNameById(createUser);
            if (null != userName){
                entries.set(wrapperField.ref(), userName);
            }
        });

        Optional.ofNullable(bizNotice.getUpdateUser()).ifPresent(updateUser -> {
            CommonWrapperField wrapperField = null;
            try {
                wrapperField = bizNotice.getClass().getDeclaredField("updateUserName").getAnnotation(CommonWrapperField.class);
            } catch (NoSuchFieldException e) {
                setLog(updateUser);
            }
            String userName = userApi.getUserNameById(updateUser);
            if (null != userName){
                entries.set(wrapperField.ref(), userName);
            }
        });

        return entries;
    }

    protected void setLog(String field) {
        log.info("字段：{}，未找到注解。", field);
    }
}
