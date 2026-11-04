package vip.xiaonuo.task.api.convert;

import org.noear.solon.annotation.Component;
import org.noear.solon.core.convert.Converter;
import org.noear.solon.core.exception.ConvertException;
import vip.xiaonuo.task.api.enums.TaskCategory;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/25 17:01
 */
@Component
public class IntegralTaskCategoryStringToEnumConverter implements Converter<String, TaskCategory> {
    @Override
    public TaskCategory convert(String value) throws ConvertException {
        return TaskCategory.getByCode(value);
    }
}
