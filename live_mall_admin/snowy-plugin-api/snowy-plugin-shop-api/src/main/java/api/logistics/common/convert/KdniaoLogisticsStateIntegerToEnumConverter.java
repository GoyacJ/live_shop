package api.logistics.common.convert;

import org.noear.solon.annotation.Component;
import org.noear.solon.core.convert.Converter;
import org.noear.solon.core.exception.ConvertException;
import api.logistics.common.enums.KdniaoLogisticsState;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/25 17:01
 */
@Component
public class KdniaoLogisticsStateIntegerToEnumConverter implements Converter<Integer, KdniaoLogisticsState> {
    @Override
    public KdniaoLogisticsState convert(Integer value) throws ConvertException {
        return KdniaoLogisticsState.getByCode(value);
    }
}
