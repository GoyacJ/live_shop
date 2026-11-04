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
package vip.xiaonuo.common.enums;

import cn.hutool.core.util.IdUtil;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 订单类型字典与简称映射枚举
 *
 * @author yuezisheng
 * @date  2025/02/18 20:31
 **/
@Getter
public enum OrderTypeMapperEnum {

    /**
     * 订单类型，1商城订单，2直播间订单，3积分兑换订单，4直播间福袋
     */
    /** 1商城订单 */
    SC("1","SC","商城订单"),
    ZBJ("2","ZBJ","直播间订单"),
    JF("3","JF","积分兑换订单"),
    FD("4","FD","直播间福袋");


    private final String key;
    private final String abbr;
    private final String desc;

    OrderTypeMapperEnum(String key, String abbr, String desc) {
        this.key = key;
        this.abbr = abbr;
        this.desc = desc;
    }

    /**
     * 创建订单号
     * 本地单体实现方式
     * @param orderType 业务类型
     * @return 订单号
     */
    public static String createOrderNumber(OrderTypeMapperEnum orderType){
        // 获取标识
        String abbr = orderType.getAbbr();
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String nanoId = IdUtil.nanoId(6);
        return abbr + now + nanoId;
    }

    /**
     * 根据 key 获取枚举实例
     *
     * @param key 枚举中的 key 字段值
     * @return 匹配的枚举实例，若无匹配则返回 null
     */
    public static OrderTypeMapperEnum getByKey(String key) {
        for (OrderTypeMapperEnum type : OrderTypeMapperEnum.values()) {
            if (type.getKey().equals(key)) {
                return type;
            }
        }
        return null;
    }

}
