package api.logistics.common.convert;

import org.noear.solon.annotation.Component;
import org.noear.solon.core.convert.Converter;
import org.noear.solon.core.exception.ConvertException;
import api.logistics.common.enums.KdniaoLogisticsStateEx;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/25 17:01
 */
@Component
public class KdniaoLogisticsStateExIntegerToEnumConverter implements Converter<Integer, KdniaoLogisticsStateEx> {
    @Override
    public KdniaoLogisticsStateEx convert(Integer value) throws ConvertException {
        return KdniaoLogisticsStateEx.getByCode(value);
    }
}
