package vip.xiaonuo.core.config;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.paginate.Page;
import org.noear.solon.core.aspect.Interceptor;
import org.noear.solon.core.aspect.Invocation;
import vip.xiaonuo.common.annotation.CommonWrapper;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonWrapperInterface;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author noear 2022/10/21 created
 */
public class CommonWrapperInterceptor implements Interceptor {
    @Override
    public Object doIntercept(Invocation inv) throws Throwable {
        Object proceedResult = inv.invoke();
        return processWrapping(inv, proceedResult);
    }

    /**
     * 具体包装过程
     *
     * @author xuyuxiang
     * @date 2022/9/15 21:27
     */
    @SuppressWarnings("all")
    private Object processWrapping(Invocation inv, Object originResult) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        Method method = inv.method().getMethod();
        CommonWrapper commonWrapper = method.getAnnotation(CommonWrapper.class);
        Class<? extends CommonWrapperInterface<?>>[] baseWrapperClasses = commonWrapper.value();
        if (ObjectUtil.isEmpty(baseWrapperClasses)) {
            return originResult;
        }
        if (!(originResult instanceof CommonResult)) {
            return originResult;
        }
        CommonResult commonResult = (CommonResult) originResult;
        Object beWrapped = commonResult.getData();
        if (ObjectUtil.isBasicType(beWrapped)) {
            throw new CommonException("被包装的值不能是基本类型");
        }
        if (beWrapped instanceof Page) {
            Page page = (Page) beWrapped;
            // 主要解决 转成map之后jackson序列化失效问题
            List<Object> maps = new ArrayList<>();
            for (Object wrappedItem : page.getRecords()) {
                String className = wrappedItem.getClass().getName();
                JSONObject entries = this.wrapPureObject(wrappedItem, baseWrapperClasses);
                Object bean = JSONUtil.toBean(entries, Class.forName(className));
                maps.add(bean);
            }
            page.setRecords(maps);
            commonResult.setData(page);
        } else if (beWrapped instanceof Collection) {
            Collection collection = (Collection) beWrapped;
            List<Map<String, Object>> maps = new ArrayList<>();
            for (Object wrappedItem : collection) {
                maps.add(this.wrapPureObject(wrappedItem, baseWrapperClasses));
            }
            commonResult.setData(maps);
        } else if (ArrayUtil.isArray(beWrapped)) {
            Object[] objects = this.objToArray(beWrapped);
            ArrayList<Map<String, Object>> maps = new ArrayList<>();
            for (Object wrappedItem : objects) {
                maps.add(this.wrapPureObject(wrappedItem, baseWrapperClasses));
            }
            commonResult.setData(maps);
        } else {
            commonResult.setData(this.wrapPureObject(beWrapped, baseWrapperClasses));
        }
        return commonResult;
    }

    /**
     * 原始对象包装JSONObject
     *
     * @author xuyuxiang
     * @date 2022/9/15 21:36
     */
    @SuppressWarnings("all")
    private JSONObject wrapPureObject(Object originModel, Class<? extends CommonWrapperInterface<?>>[] baseWrapperClasses) {
        JSONObject jsonObject = JSONUtil.parseObj(originModel);
        try {
            for (Class<? extends CommonWrapperInterface<?>> commonWrapperClass : baseWrapperClasses) {
                CommonWrapperInterface commonWrapperInterface = commonWrapperClass.newInstance();
                Map<String, Object> incrementFieldsMap = commonWrapperInterface.doWrap(originModel);
                jsonObject.putAll(incrementFieldsMap);
            }
        } catch (Exception e) {
            throw new CommonException("原始对象包装过程，字段转化异常：{}", e.getMessage());
        }
        return jsonObject;
    }

    /**
     * Object转array
     *
     * @author xuyuxiang
     * @date 2022/9/15 21:34
     */
    private Object[] objToArray(Object object) {
        int length = Array.getLength(object);
        Object[] result = new Object[length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Array.get(object, i);
        }
        return result;
    }
}
