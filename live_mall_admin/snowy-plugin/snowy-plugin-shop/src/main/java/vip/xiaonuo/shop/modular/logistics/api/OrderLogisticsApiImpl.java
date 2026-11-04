package vip.xiaonuo.shop.modular.logistics.api;

import api.logistics.OrderLogisticsApi;
import api.logistics.dto.OrderLogisticsInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.shop.modular.logistics.service.OrderLogisticsService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/29 16:55
 */
@Slf4j
@Component
public class OrderLogisticsApiImpl implements OrderLogisticsApi {

    @Inject
    private OrderLogisticsService orderLogisticsService;

    @Override
    public OrderLogisticsInfoDTO detail(String orderCode) {
        return orderLogisticsService.detail(orderCode);
    }
}
