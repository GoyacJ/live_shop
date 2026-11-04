package api.logistics.common.convert;

import org.noear.solon.annotation.Component;
import org.noear.solon.core.convert.Converter;
import org.noear.solon.core.exception.ConvertException;
import api.logistics.common.enums.Shipper;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/25 17:01
 */
@Component
public class ShipperStringToEnumConverter implements Converter<String, Shipper> {
    @Override
    public Shipper convert(String value) throws ConvertException {
        return Shipper.getByCode(value);
    }
}
