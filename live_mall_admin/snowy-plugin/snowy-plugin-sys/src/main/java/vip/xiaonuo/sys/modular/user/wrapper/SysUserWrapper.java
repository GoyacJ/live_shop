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
package vip.xiaonuo.sys.modular.user.wrapper;

import cn.hutool.json.JSONObject;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import vip.xiaonuo.common.annotation.CommonWrapperField;
import vip.xiaonuo.common.pojo.CommonWrapperInterface;
import vip.xiaonuo.sys.modular.org.entity.SysOrg;
import vip.xiaonuo.sys.modular.org.service.SysOrgService;
import vip.xiaonuo.sys.modular.position.entity.SysPosition;
import vip.xiaonuo.sys.modular.position.service.SysPositionService;
import vip.xiaonuo.sys.modular.user.entity.SysUser;
import vip.xiaonuo.sys.modular.user.service.SysUserService;

import java.util.Optional;

/**
 * 用户管理的包装类
 *
 * @author ChengChuanYao
 * @date 2024/7/12 14:17
 */
@Slf4j
public class SysUserWrapper implements CommonWrapperInterface<SysUser> {

    private static SysOrgService orgService;

    private static SysPositionService positionService;

    private static SysUserService userService;

    static {
        //异步订阅方式，根据bean type获取Bean（已存在或产生时，会通知回调；否则，一直不回调）
        Solon.context().getBeanAsync(SysOrgService.class, bean -> {
            orgService = bean;
        });
        Solon.context().getBeanAsync(SysPositionService.class, bean -> {
            positionService = bean;
        });
        Solon.context().getBeanAsync(SysUserService.class, bean -> {
            userService = bean;
        });
    }

    @Override
    public JSONObject doWrap(SysUser sysUser) {
        // 后期可通过反射进行处理
        JSONObject entries = new JSONObject();
        // 组织id 翻译
        Optional.ofNullable(sysUser.getOrgId()).ifPresent(orgId -> {
            CommonWrapperField wrapperField = null;
            try {
                wrapperField = sysUser.getClass().getDeclaredField("orgId").getAnnotation(CommonWrapperField.class);
            } catch (NoSuchFieldException e) {
                setLog(orgId);
            }
            QueryWrapper queryWrapper = QueryWrapper.create().select("name").eq("id", orgId);
            SysOrg sysOrg = orgService.getMapper().selectOneByQuery(queryWrapper);
            if (null != sysOrg) {
                entries.set(wrapperField.ref(), sysOrg.getName());
            }
        });
        // 职位id
        Optional.ofNullable(sysUser.getPositionId()).ifPresent(positionId -> {
            CommonWrapperField wrapperField = null;
            try {
                wrapperField = sysUser.getClass().getDeclaredField("positionId").getAnnotation(CommonWrapperField.class);
            } catch (NoSuchFieldException e) {
                setLog(positionId);
            }
            QueryWrapper eq = QueryWrapper.create().select("name").eq("id", positionId);
            SysPosition sysPosition = positionService.getMapper().selectOneByQuery(eq);
            if (null != sysPosition) {
                entries.set(wrapperField.ref(), sysPosition.getName());
            }
        });

        // 主管id
        Optional.ofNullable(sysUser.getDirectorId()).ifPresent(directorId -> {
            CommonWrapperField wrapperField = null;
            try {
                wrapperField = sysUser.getClass().getDeclaredField("directorId").getAnnotation(CommonWrapperField.class);
            } catch (NoSuchFieldException e) {
                setLog(directorId);
            }
            QueryWrapper queryWrapper = QueryWrapper.create().select("name").eq("id", directorId);
            SysUser sysUserDirector = userService.getMapper().selectOneByQuery(queryWrapper);
            if (null != sysUserDirector) {
                entries.set(wrapperField.ref(), sysUserDirector.getName());
            }
        });
        // 性别
        Optional.ofNullable(sysUser.getGender()).ifPresent(gender -> {
            CommonWrapperField wrapperField = null;
            try {
                wrapperField = sysUser.getClass().getDeclaredField("gender").getAnnotation(CommonWrapperField.class);
            } catch (NoSuchFieldException e) {
                setLog(gender);
            }
            entries.set(wrapperField.ref(), gender);

        });
        return entries;
    }

    protected void setLog(String field) {
        log.info("字段：{}，未找到注解。", field);
    }

}
