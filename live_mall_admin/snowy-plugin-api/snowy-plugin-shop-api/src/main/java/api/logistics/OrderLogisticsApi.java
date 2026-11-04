package api.logistics;

import api.logistics.dto.OrderLogisticsInfoDTO;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/29 16:46
 */
public interface OrderLogisticsApi {

    /**
     * 物流信息详情
     * @param orderCode 订单编码
     * @return 结果
     */
    OrderLogisticsInfoDTO detail(String orderCode);
}
