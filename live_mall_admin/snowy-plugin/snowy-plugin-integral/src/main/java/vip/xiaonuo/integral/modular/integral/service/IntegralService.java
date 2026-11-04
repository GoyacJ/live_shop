package vip.xiaonuo.integral.modular.integral.service;

import com.mybatisflex.core.paginate.Page;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.integral.api.dto.ChangeIntegralDTO;
import vip.xiaonuo.integral.api.dto.IntegralInfoDTO;
import vip.xiaonuo.integral.api.dto.UserIntegralChangeDTO;
import vip.xiaonuo.integral.api.dto.UserIntegralChangePageDTO;
import vip.xiaonuo.integral.api.enums.IntegralOperationSource;
import vip.xiaonuo.integral.api.enums.IntegralSource;

import java.math.BigDecimal;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/30 16:38
 */
public interface IntegralService {

    /**
     * 积分修改
     *
     * @param dto 参数
     * @return 结果
     * @link ChangeIntegralDTO 具体业务规则根据参数决定
     */
    @Valid
    boolean changeIntegral(ChangeIntegralDTO dto);

    /**
     * 清除过期积分
     *
     * @param userId           用户Id
     * @param modifyUserId     操作用户Id
     * @param integralSource   来源
     * @param integralSourceId 来源Id
     * @param operationSource  操作来源
     * @param remark           备注
     * @return 结果
     */
    boolean clearExpiredIntegral(String userId,
                                 String modifyUserId,
                                 IntegralSource integralSource,
                                 String integralSourceId,
                                 IntegralOperationSource operationSource,
                                 String remark);

    /**
     * 积分提现
     *
     * @param userId          提现用户
     * @param modifyUserId    操作人
     * @param integral        积分
     * @param operationSource 操作来源
     * @param remark          备注
     * @return 结果
     */
    boolean withdrawalPoints(String userId,
                             String modifyUserId,
                             BigDecimal integral,
                             IntegralOperationSource operationSource,
                             String remark); //Withdrawal of points

    /**
     * 获取积分
     *
     * @param userId 用户Id
     * @return 积分
     */
    BigDecimal getIntegralByUserId(String userId);

    /**
     * 获取积分信息
     *
     * @param userId 用户Id
     * @return 积分信息
     */
    IntegralInfoDTO getIntegralInfoByUserId(String userId);

    /**
     * 创建默认积分
     *
     * @param userId 用户Id
     * @return integralId
     */
    String createIntegral(String userId);

    /**
     * 创建默认积分
     *
     * @param userId         用户Id
     * @param integral       积分
     * @param frozenIntegral 冻结积分
     * @return integralId
     */
    String createIntegral(String userId, String modifyUserId, BigDecimal integral, BigDecimal frozenIntegral);

    /**
     * 删除积分信息
     *
     * @param userId 用户ID
     */
    boolean deleteIntegralByUserId(String userId);

    /**
     * 积分变动分页
     *
     * @param param 参数
     * @return 结果
     */
    Page<UserIntegralChangeDTO> userIntegralChangePage(UserIntegralChangePageDTO param);
}
