package vip.xiaonuo.integral.modular.withdrawal.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.integral.modular.withdrawal.entity.IntegralWithdrawalRecord;
import vip.xiaonuo.integral.modular.withdrawal.params.IntegralWithdrawalRecordPageParam;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/28 14:04
 */
public interface IntegralWithdrawalRecordService extends IService<IntegralWithdrawalRecord> {

    /**
     * 积分提现记录管理分页
     * @param param 分页参数
     * @return 结果
     */
    Page<IntegralWithdrawalRecord> page(IntegralWithdrawalRecordPageParam param);

    /**
     * 添加积分提现记录
     * @param record 积分提现记录
     */
    boolean addIntegralWithdrawalRecord(IntegralWithdrawalRecord record);
}
