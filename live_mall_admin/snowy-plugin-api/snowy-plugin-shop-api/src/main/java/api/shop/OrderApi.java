package api.shop;

import api.shop.dto.OrderManagePageDTO;
import api.shop.dto.OrderManagePageParamDTO;
import api.shop.dto.SettleManagePageDTO;
import api.shop.dto.SettleManagePageParamDTO;
import com.mybatisflex.core.paginate.Page;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/22 15:24
 */
public interface OrderApi {
    /**
     * 订单管理分页
     * @param param 参数
     * @return 结果
     */
    Page<OrderManagePageDTO> orderManagePage(OrderManagePageParamDTO param);

    /**
     * 结算管理分页
     * @param param 参数
     * @return 结果
     */
    Page<SettleManagePageDTO> settleManagePage(SettleManagePageParamDTO param);

    /**
     * 订单详情
     * @param orderId 订单Id
     * @return 结果
     */
    OrderManagePageDTO orderDetail(String orderId);

}
