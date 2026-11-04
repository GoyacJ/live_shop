package vip.xiaonuo.shop.modular.logistics.service;

import api.logistics.dto.OrderLogisticsInfoDTO;
import com.mybatisflex.core.service.IService;
import org.noear.solon.core.handle.UploadedFile;
import vip.xiaonuo.shop.modular.logistics.entity.OrderLogistics;
import vip.xiaonuo.shop.modular.logistics.param.OrderLogisticsSettingParam;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/27 09:55
 */
public interface OrderLogisticsService extends IService<OrderLogistics> {

    /**
     * 设置物流信息
     * @param param param
     * @return 结果
     */
    boolean setting(OrderLogisticsSettingParam param);

    /**
     * 物流信息详情
     * @param orderNumber 订单编码
     * @return 结果
     */
    OrderLogisticsInfoDTO detail(String orderNumber);

    /**
     * 导入
     * @param file 发货模板
     * @return 结果
     */
    boolean importLogistics(UploadedFile file);
}
