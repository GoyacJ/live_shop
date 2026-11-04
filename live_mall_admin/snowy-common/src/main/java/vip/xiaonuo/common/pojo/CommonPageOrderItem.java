package vip.xiaonuo.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 排序元素载体
 *
 * @author ChengChuanYao
 * @date 2024/06/22 11:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonPageOrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 需要进行排序的字段
     */
    private String Column;
    /**
     * 是否正序排列，默认 true
     */
    private boolean asc = true;

    public static CommonPageOrderItem asc(String Column) {
        return build(Column, true);
    }

    public static CommonPageOrderItem desc(String Column) {
        return build(Column, false);
    }

    public static List<CommonPageOrderItem> ascs(String... Columns) {
        return Arrays.stream(Columns).map(CommonPageOrderItem::asc).collect(Collectors.toList());
    }

    public static List<CommonPageOrderItem> descs(String... Columns) {
        return Arrays.stream(Columns).map(CommonPageOrderItem::desc).collect(Collectors.toList());
    }

    private static CommonPageOrderItem build(String Column, boolean asc) {
        return new CommonPageOrderItem(Column, asc);
    }
}