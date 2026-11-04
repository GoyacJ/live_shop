package vip.xiaonuo.common.converter;

import org.noear.solon.annotation.Component;
import org.noear.solon.core.convert.Converter;
import org.noear.solon.core.exception.ConvertException;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/13 15:23
 */
@Component
public class CommonSortOrderStringToEnumConverter  implements Converter<String, CommonSortOrderEnum> {
    @Override
    public CommonSortOrderEnum convert(String value) throws ConvertException {
        return CommonSortOrderEnum.getByValue(value);
    }
}
