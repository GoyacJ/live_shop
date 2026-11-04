package api.sales;

public interface MallSalesTypeApi {

    /**
     * 添加售后类型
     * @param orderId  订单id
     * @param  salesLabel  售后类型
     * @param  salesValue  售后描述
     * @return
     */
    int addMallSalesType(String orderId,String salesLabel,String salesValue);

    /**
     * 更新售后类型
     * @param orderId 订单id
     * @param  salesLabel  售后类型
     * @param  salesValue  售后描述
     * @return
     */
    Boolean updateMallSalesType(String orderId,String salesLabel,String salesValue);
}
