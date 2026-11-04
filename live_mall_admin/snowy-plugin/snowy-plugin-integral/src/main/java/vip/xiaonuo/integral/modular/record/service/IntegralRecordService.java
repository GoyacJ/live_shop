package vip.xiaonuo.integral.modular.record.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.integral.api.dto.UserIntegralChangeDTO;
import vip.xiaonuo.integral.api.dto.UserIntegralChangePageDTO;
import vip.xiaonuo.integral.modular.record.entity.IntegralRecord;
import vip.xiaonuo.integral.modular.record.params.IntegralRecordPageParam;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/28 14:04
 */
public interface IntegralRecordService extends IService<IntegralRecord> {

    /**
     * 积分记录管理分页
     * @param param 分页参数
     * @return 结果
     */
    Page<IntegralRecord> page(IntegralRecordPageParam param);

    /**
     * 添加积分记录
     * @param integralLog 积分记录
     */
    boolean addIntegralRecord(IntegralRecord integralLog);

    /**
     * 积分过期
     */
    void expireIntegral();

    /**
     * 删除积分记录
     * @param userId 用户id
     * @return 结果
     */
    boolean deleteIntegralRecordByUserId(String userId);

    /**
     * 积分记录详情
     * @param id 积分记录id
     * @return 结果
     */
    IntegralRecord detail(String id);

    /**
     * 用户积分变动分页
     * @param param 参数
     * @return 结果
     */
    Page<UserIntegralChangeDTO> userIntegralChangePage(UserIntegralChangePageDTO param);
}
