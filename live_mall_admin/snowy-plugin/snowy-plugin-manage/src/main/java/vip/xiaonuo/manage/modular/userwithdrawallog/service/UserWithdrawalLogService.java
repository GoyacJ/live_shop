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
package vip.xiaonuo.manage.modular.userwithdrawallog.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.manage.modular.userwithdrawallog.entity.UserWithdrawalLog;
import vip.xiaonuo.manage.modular.userwithdrawallog.param.UserWithdrawalLogAddParam;
import vip.xiaonuo.manage.modular.userwithdrawallog.param.UserWithdrawalLogEditParam;
import vip.xiaonuo.manage.modular.userwithdrawallog.param.UserWithdrawalLogIdParam;
import vip.xiaonuo.manage.modular.userwithdrawallog.param.UserWithdrawalLogPageParam;

import java.util.List;

/**
 * 用户提现记录Service接口
 *
 * @author yuezisheng
 * @date  2025/02/18 20:31
 **/
public interface UserWithdrawalLogService extends IService<UserWithdrawalLog> {

    /**
     * 获取用户提现记录分页
     *
     * @author yuezisheng
     * @date  2025/02/18 20:31
     */
    Page<UserWithdrawalLog> page(UserWithdrawalLogPageParam userWithdrawalLogPageParam);

    /**
     * 添加用户提现记录
     *
     * @author yuezisheng
     * @date  2025/02/18 20:31
     */
    void add(UserWithdrawalLogAddParam userWithdrawalLogAddParam);

    /**
     * 编辑用户提现记录
     *
     * @author yuezisheng
     * @date  2025/02/18 20:31
     */
    void edit(UserWithdrawalLogEditParam userWithdrawalLogEditParam);

    /**
     * 删除用户提现记录
     *
     * @author yuezisheng
     * @date  2025/02/18 20:31
     */
    void delete(List<UserWithdrawalLogIdParam> userWithdrawalLogIdParamList);

    /**
     * 获取用户提现记录详情
     *
     * @author yuezisheng
     * @date  2025/02/18 20:31
     */
    UserWithdrawalLog detail(UserWithdrawalLogIdParam userWithdrawalLogIdParam);

    /**
     * 获取用户提现记录详情
     *
     * @author yuezisheng
     * @date  2025/02/18 20:31
     **/
    UserWithdrawalLog queryEntity(String id);
}
