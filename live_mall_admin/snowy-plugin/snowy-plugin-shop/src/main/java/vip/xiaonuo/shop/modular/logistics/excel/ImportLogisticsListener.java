package vip.xiaonuo.shop.modular.logistics.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.shop.modular.logistics.dto.ImportLogisticsDTO;
import vip.xiaonuo.shop.modular.logistics.param.OrderLogisticsSettingParam;
import vip.xiaonuo.shop.modular.logistics.service.OrderLogisticsService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/6 17:06
 */
@Slf4j
@Component
public class ImportLogisticsListener extends AnalysisEventListener<ImportLogisticsDTO> {

    @Inject
    private OrderLogisticsService orderLogisticsService;

    @Override
    public void invoke(ImportLogisticsDTO data, AnalysisContext analysisContext) {
        try {
            orderLogisticsService.setting(new OrderLogisticsSettingParam(
                    data.getShipper(),
                    data.getLogisticCode(),
                    data.getCustomerName(),
                    data.getOrderCode()
            ));
        } catch (Exception e) {
            log.error("批量订单物流数据上传异常,异常数据:{}", data);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.warn("批量订单物流数据解析完成!");
    }
}
