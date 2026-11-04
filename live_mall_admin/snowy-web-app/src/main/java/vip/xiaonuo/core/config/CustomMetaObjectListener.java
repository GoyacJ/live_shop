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

import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.UpdateListener;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import vip.xiaonuo.common.enums.CommonDeleteFlagEnum;
import vip.xiaonuo.common.pojo.CommonEntity;

import java.util.Date;
import java.util.function.Supplier;

/**
 * 用于监听 Entity 实体类数据被新增到数据库
 *
 * @author ChengChuanYao
 * @date 2024/6/22 14:17
 */
@Slf4j
public class CustomMetaObjectListener implements UpdateListener, InsertListener {

    /**
     * 自定义新增注入
     */
    @Override
    public void onInsert(Object entity) {
        try {
            if (ObjectUtil.isNotNull(entity) && entity instanceof CommonEntity) {
                CommonEntity commonEntity = (CommonEntity) entity;
                Date current = ObjectUtil.isNotNull(commonEntity.getCreateTime())
                        ? commonEntity.getCreateTime() : new Date();
                commonEntity.setCreateTime(current);
                // 当前已登录 且 创建人为空 则填充
                if (ObjectUtil.isNull(commonEntity.getCreateUser())) {
                    commonEntity.setCreateUser(this.getUserId());
                }
                commonEntity.setDeleteFlag(EnumUtil.toString(CommonDeleteFlagEnum.NOT_DELETE));
            }
        } catch (Exception e) {
            printException("新增注入", e);
        }
    }

    /**
     * 自定义修改注入
     */
    @Override
    public void onUpdate(Object entity) {
        try {
            if (ObjectUtil.isNotNull(entity) && entity instanceof CommonEntity) {
                CommonEntity commonEntity = (CommonEntity) entity;
                Date current = ObjectUtil.isNotNull(commonEntity.getUpdateTime())
                        ? commonEntity.getUpdateTime() : new Date();
                commonEntity.setUpdateTime(current);

                // 当前已登录 且 更新人为空 则填充
                if (ObjectUtil.isNull(commonEntity.getUpdateUser())) {
                    commonEntity.setUpdateUser(this.getUserId());
                }
            }
        } catch (Exception e) {
            printException("修改注入", e);
        }
    }

    /**
     * 打印异常信息
     */
    private void printException(String msg, Exception e) {
        log.error("{} => 自动注入异常 => {}", msg, e.getMessage());
    }

    /**
     * 获取用户id
     */
    private String getUserId() {
        Supplier<String> getter = Solon.context().getBean("user_id_bean");
        try {
            if (ObjectUtil.isNotEmpty(getter.get())) {
                return getter.get();
            } else {
                return "-1";
            }
        } catch (Exception e) {
            return "-1";
        }
    }
}
