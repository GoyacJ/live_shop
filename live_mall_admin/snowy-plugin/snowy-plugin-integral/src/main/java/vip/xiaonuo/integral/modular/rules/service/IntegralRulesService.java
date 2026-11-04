package vip.xiaonuo.integral.modular.rules.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.common.enums.RefreshTimeType;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.integral.modular.rules.entity.IntegralRules;
import vip.xiaonuo.integral.modular.rules.params.IntegralRulesAddParam;
import vip.xiaonuo.integral.modular.rules.params.IntegralRulesEditParam;
import vip.xiaonuo.integral.modular.rules.params.IntegralRulesIdParam;
import vip.xiaonuo.integral.modular.rules.params.IntegralRulesPageParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/28 13:27
 */
public interface IntegralRulesService extends IService<IntegralRules> {

    /**
     * 分页
     *
     * @param param 参数
     * @return 数据
     */
    Page<IntegralRules> page(IntegralRulesPageParam param);

    /**
     * 新增
     *
     * @param param 参数
     */
    void add(IntegralRulesAddParam param);

    /**
     * 编辑
     *
     * @param param 参数
     */
    void edit(IntegralRulesEditParam param);

    /**
     * 详情
     *
     * @param id id
     * @return 数据
     */
    IntegralRules detail(String id);

    /**
     * 删除
     *
     * @param param 参数
     */
    void delete(CommonValidList<IntegralRulesIdParam> param);

    /**
     * 修改状态
     *
     * @param id     id
     * @param status 状态
     */
    void changeStatus(String id, SimpleStatus status);

    /**
     * 根据用户ID获取积分规则
     *
     * @param userId 用户ID
     * @return 积分规则
     */
    Map<RefreshTimeType,IntegralRules> getIntegralRulesByUserId(String userId);

    /**
     * 根据用户ID获取积分提现规则
     *
     * @param userId 用户ID
     * @return 积分规则
     */
    Map<RefreshTimeType,IntegralRules> getWithdrawalIntegralRulesByUserId(String userId);

    /**
     * 检查增加积分是否符合规则
     *
     * @param userId   用户ID
     * @param integral 积分
     * @return 是否符合规则
     */
    boolean checkIncreaseRules(String userId, BigDecimal integral);

    /**
     * 检查提现是否符合规则
     *
     * @param userId   用户ID
     * @param integral 积分
     * @return 是否符合规则
     */
    boolean checkWithdrawalRules(String userId, BigDecimal integral);

    /**
     * 获取积分兑现比例
     *
     * @param userId 用户ID
     * @return 积分兑现比例
     */
    BigDecimal getPointsToCashRatio(String userId);

    /**
     * 获取有效时间
     *
     * @param userId 用户ID
     * @return 有效时间
     */
    LocalDateTime getValidityTime(String userId);
}
