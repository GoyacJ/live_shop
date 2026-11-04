package vip.xiaonuo.integral.modular.info.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.integral.modular.info.entity.IntegralInfo;
import vip.xiaonuo.integral.modular.info.params.IntegralInfoPageParam;

import java.math.BigDecimal;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/29 16:39
 */
public interface IntegralInfoService extends IService<IntegralInfo> {

    /**
     * 创建积分
     *
     * @param id
     * @param userId         用户ID
     * @param integral
     * @param frozenIntegral
     */
    String createIntegral(String id, String userId, BigDecimal integral, BigDecimal frozenIntegral);

    /**
     * 根据用户ID查询积分信息
     *
     * @param userId 用户ID
     * @return 积分信息
     */
    IntegralInfo getIntegralInfoByUserId(String userId);

    /**
     * 判断积分是否锁定
     *
     * @param userId 用户ID
     * @return 是否锁定 true:是 false:否
     */
    default boolean isLockIntegralStatus(String userId) {
        return isLockIntegralStatus(getIntegralInfoByUserId(userId));
    }

    /**
     * 判断积分是否锁定
     *
     * @param integralInfo 积分信息
     * @return 是否锁定 true:是 false:否
     */
    boolean isLockIntegralStatus(IntegralInfo integralInfo);

    /**
     * 增加积分
     *
     * @param userId       用户ID
     * @param integral     积分
     * @param modifyUserId 修改人Id
     */
    boolean increaseIntegral(String userId, BigDecimal integral, String modifyUserId);

    /**
     * 增加积分
     *
     * @param userId   用户ID
     * @param integral 积分
     */
    default boolean increaseIntegral(String userId, BigDecimal integral) {
        return increaseIntegral(userId, integral, userId);
    }

    /**
     * 减少积分
     *
     * @param userId   用户ID
     * @param integral 积分
     */
    default boolean decreaseIntegral(String userId, BigDecimal integral) {
        return decreaseIntegral(userId, integral, userId);
    }

    /**
     * 减少积分
     *
     * @param userId       用户ID
     * @param integral     积分
     * @param modifyUserId 修改人Id
     */
    boolean decreaseIntegral(String userId, BigDecimal integral, String modifyUserId);

    /**
     * 增加冻结积分
     *
     * @param userId         用户ID
     * @param frozenIntegral 冻结积分
     */
    default boolean increaseFrozenIntegral(String userId, BigDecimal frozenIntegral) {
        return increaseFrozenIntegral(userId, frozenIntegral, userId);
    }

    /**
     * 增加冻结积分
     *
     * @param userId         用户ID
     * @param frozenIntegral 冻结积分
     * @param modifyUserId   修改人Id
     */
    boolean increaseFrozenIntegral(String userId, BigDecimal frozenIntegral, String modifyUserId);

    /**
     * 减少冻结积分
     *
     * @param userId         用户ID
     * @param frozenIntegral 冻结积分
     */
    default boolean decreaseFrozenIntegral(String userId, BigDecimal frozenIntegral) {
        return decreaseFrozenIntegral(userId, frozenIntegral, userId);
    }

    /**
     * 减少冻结积分
     *
     * @param userId         用户ID
     * @param frozenIntegral 冻结积分
     * @param modifyUserId   修改人Id
     */
    boolean decreaseFrozenIntegral(String userId, BigDecimal frozenIntegral, String modifyUserId);

    /**
     * 解锁冻结积分
     *
     * @param userId         用户ID
     * @param frozenIntegral 冻结积分
     */
    default boolean unLockFrozenIntegral(String userId, BigDecimal frozenIntegral) {
        return unLockFrozenIntegral(userId, frozenIntegral, userId);
    }

    /**
     * 解锁冻结积分
     *
     * @param userId         用户ID
     * @param frozenIntegral 冻结积分
     * @param modifyUserId   修改人Id
     */
    boolean unLockFrozenIntegral(String userId, BigDecimal frozenIntegral, String modifyUserId);

    /**
     * 更新冻结积分
     *
     * @param userId         用户ID
     * @param frozenIntegral 冻结积分
     */
    default boolean updateFrozenIntegral(String userId, BigDecimal frozenIntegral) {
        return updateFrozenIntegral(userId, frozenIntegral, userId);
    }

    /**
     * 更新冻结积分
     *
     * @param userId         用户ID
     * @param frozenIntegral 冻结积分
     * @param modifyUserId   修改人Id
     */
    boolean updateFrozenIntegral(String userId, BigDecimal frozenIntegral, String modifyUserId);

    /**
     * 更新积分
     *
     * @param userId   用户ID
     * @param integral 积分
     */
    default boolean updateIntegral(String userId, BigDecimal integral) {
        return updateIntegral(userId, integral, userId);
    }

    /**
     * 更新积分
     *
     * @param userId       用户ID
     * @param integral     积分
     * @param modifyUserId 修改人Id
     */
    boolean updateIntegral(String userId, BigDecimal integral, String modifyUserId);

    /**
     * 更新过期积分
     *
     * @param userId         用户ID
     * @param expireIntegral 过期积分
     */
    default boolean updateExpireIntegral(String userId, BigDecimal expireIntegral) {
        return updateExpireIntegral(userId, expireIntegral, userId);
    }

    /**
     * 更新过期积分
     *
     * @param userId         用户ID
     * @param expireIntegral 过期积分
     * @param modifyUserId   修改人Id
     */
    boolean updateExpireIntegral(String userId, BigDecimal expireIntegral, String modifyUserId);

    /**
     * 清除过期积分
     *
     * @param userId 用户ID
     */
    default boolean clearExpireIntegral(String userId) {
        return clearExpireIntegral(userId, userId);
    }

    /**
     * 清除过期积分
     *
     * @param userId       用户ID
     * @param modifyUserId 修改人Id
     */
    boolean clearExpireIntegral(String userId, String modifyUserId);

    /**
     * 增加过期积分
     *
     * @param userId         用户ID
     * @param expireIntegral 积分
     */
    default boolean increaseExpireIntegral(String userId, BigDecimal expireIntegral) {
        return increaseExpireIntegral(userId, expireIntegral, userId);
    }

    /**
     * 增加过期积分
     *
     * @param userId         用户ID
     * @param expireIntegral 积分
     * @param modifyUserId   修改人Id
     */
    boolean increaseExpireIntegral(String userId, BigDecimal expireIntegral, String modifyUserId);

    /**
     * 减少过期积分
     *
     * @param userId         用户ID
     * @param expireIntegral 积分
     */
    default boolean decreaseExpireIntegral(String userId, BigDecimal expireIntegral) {
        return decreaseExpireIntegral(userId, expireIntegral, userId);
    }

    /**
     * 减少过期积分
     *
     * @param userId         用户ID
     * @param expireIntegral 积分
     * @param modifyUserId   修改人Id
     */
    boolean decreaseExpireIntegral(String userId, BigDecimal expireIntegral, String modifyUserId);

    /**
     * 更新积分状态
     *
     * @param userId 用户ID
     * @param status 状态
     */
    default boolean updateIntegralStatus(String userId, SimpleStatus status) {
        return updateIntegralStatus(userId, status, userId);
    }

    /**
     * 更新积分状态
     *
     * @param userId       用户ID
     * @param status       状态
     * @param modifyUserId 修改人Id
     */
    boolean updateIntegralStatus(String userId, SimpleStatus status, String modifyUserId);

    /**
     * 查询分页数据
     *
     * @param param 查询参数
     */
    Page<IntegralInfo> page(IntegralInfoPageParam param);

    /**
     * 查询详情
     *
     * @param id 积分ID
     */
    IntegralInfo detail(String id);

    /**
     * 删除积分信息
     *
     * @param userId 用户ID
     */
    boolean deleteIntegralInfoByUserId(String userId);
}
