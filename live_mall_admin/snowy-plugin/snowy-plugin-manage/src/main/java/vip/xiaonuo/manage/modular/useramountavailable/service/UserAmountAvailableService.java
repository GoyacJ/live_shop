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
package vip.xiaonuo.manage.modular.useramountavailable.service;


import com.mybatisflex.core.paginate.Page;
import vip.xiaonuo.manage.modular.useramountavailable.entity.UserAmountAvailable;
import vip.xiaonuo.manage.modular.useramountavailable.param.UserAmountAvailableAddParam;
import vip.xiaonuo.manage.modular.useramountavailable.param.UserAmountAvailableEditParam;
import vip.xiaonuo.manage.modular.useramountavailable.param.UserAmountAvailableIdParam;
import vip.xiaonuo.manage.modular.useramountavailable.param.UserAmountAvailablePageParam;

import java.util.List;

/**
 * 可选金额Service接口
 *
 */
public interface UserAmountAvailableService {

    /**
     * 获取可选金额列表分页
     *
     */
    Page<UserAmountAvailable> page(UserAmountAvailablePageParam userAmountAvailablePageParam);

    /**
     * 编辑可选金额列表记录
     *
     */
    void edit(UserAmountAvailableEditParam userAmountAvailableEditParam);

    /**
     * 删除可选金额记录
     *
     */
    void delete(List<UserAmountAvailableIdParam> userAmountAvailableIdParamList);

    /**
     * 获取可选金额详情
     *
     */
    UserAmountAvailable detail(UserAmountAvailableIdParam userAmountAvailableIdParam);

    /**
     * 添加可选金额记录
     *
     */
    void add(UserAmountAvailableAddParam userAmountAvailableAddParam);

}
