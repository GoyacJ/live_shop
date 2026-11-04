package vip.xiaonuo.integral.api;

import com.mybatisflex.core.paginate.Page;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.integral.api.dto.ChangeIntegralDTO;
import vip.xiaonuo.integral.api.dto.IntegralInfoDTO;
import vip.xiaonuo.integral.api.dto.UserIntegralChangeDTO;
import vip.xiaonuo.integral.api.dto.UserIntegralChangePageDTO;
import vip.xiaonuo.integral.api.enums.IntegralOperationSource;
import vip.xiaonuo.integral.api.enums.IntegralSource;
import vip.xiaonuo.integral.modular.integral.service.IntegralService;

import java.math.BigDecimal;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/28 19:18
 */
@Tran
@Slf4j
@Component
public class IntegralApiImpl implements IntegralApi {

    @Inject
    private IntegralService integralService;

    @Override
    public boolean changeIntegral(ChangeIntegralDTO dto) {
        return integralService.changeIntegral(dto);
    }

    @Override
    public String createIntegral(String userId) {
        return integralService.createIntegral(userId);
    }

    @Override
    public String createIntegral(String userId, String modifyUserId, BigDecimal integral, BigDecimal frozenIntegral) {
        return integralService.createIntegral(userId, modifyUserId, integral, frozenIntegral);
    }

    @Override
    public boolean clearExpiredIntegral(String userId, String modifyUserId, IntegralSource integralSource, String integralSourceId, IntegralOperationSource operationSource, String remark) {
        return integralService.clearExpiredIntegral(userId, modifyUserId, integralSource, integralSourceId, operationSource, remark);
    }

    @Override
    public boolean withdrawalPoints(String userId, String modifyUserId, BigDecimal integral, IntegralOperationSource operationSource, String remark) {
        return integralService.withdrawalPoints(userId, modifyUserId, integral, operationSource, remark);
    }

    @Override
    public BigDecimal getIntegralByUserId(String userId) {
        return integralService.getIntegralByUserId(userId);
    }

    @Override
    public IntegralInfoDTO getIntegralInfoByUserId(String userId) {
        return integralService.getIntegralInfoByUserId(userId);
    }

    @Override
    public boolean deleteIntegralByUserId(String userId) {
        return integralService.deleteIntegralByUserId(userId);
    }

    @Override
    public Page<UserIntegralChangeDTO> userIntegralChangePage(UserIntegralChangePageDTO param) {
        return integralService.userIntegralChangePage(param);
    }
}
