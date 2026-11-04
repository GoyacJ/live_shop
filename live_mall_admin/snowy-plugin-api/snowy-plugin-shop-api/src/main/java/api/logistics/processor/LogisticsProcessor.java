package api.logistics.processor;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/24 15:50
 */
public interface LogisticsProcessor {

    /**
     * 处理第三方厂商数据转换为系统内部
     * @param outerData 外部数据
     */
    void handler(Object outerData);
}
