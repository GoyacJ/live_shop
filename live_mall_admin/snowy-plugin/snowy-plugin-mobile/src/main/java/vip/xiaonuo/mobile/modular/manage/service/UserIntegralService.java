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
package vip.xiaonuo.mobile.modular.manage.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.manage.modular.userintegral.entity.UserIntegral;
import vip.xiaonuo.manage.modular.userintegral.param.*;
import vip.xiaonuo.mobile.modular.manage.result.UserIntegralResult;

import java.util.List;

/**
 * 用户积分兑换Service接口
 *
 * @author yuezisheng
 * @date  2025/02/18 11:07
 **/
public interface UserIntegralService extends IService<UserIntegral> {

    /**
     * 获取用户积分兑换分页
     *
     */
    Page<UserIntegralResult> page(UserIntegralPageParam userIntegralPageParam);

    /**
     * 添加用户积分兑换
     *
     */
    void add(UserIntegralAddParam userIntegralAddParam);

}
