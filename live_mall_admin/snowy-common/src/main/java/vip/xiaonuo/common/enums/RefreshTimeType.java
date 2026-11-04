package vip.xiaonuo.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;

/**
 * <p>刷新时间类型</p>
 *
 * @author goya
 * @since 2025/5/28 13:45
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RefreshTimeType {

    DAY(1, "每日"),
    WEEK(2, "每周"),
    MONTH(3, "每月"),
    YEAR(4, "每年"),
    NEVER(5, "永不"),

    ;

    @EnumValue
    @JsonValue
    private final Integer code;

    private final String name;

    /**
     * 根据 code 获取枚举实例
     *
     * @param code 枚举中的 code 字段值
     * @return 匹配的枚举实例，若无匹配则返回 null
     */
    @JsonCreator
    public static RefreshTimeType getByCode(Integer code) {
        for (RefreshTimeType type : RefreshTimeType.values()) {
            if (Objects.equals(type.getCode(), code)) {
                return type;
            }
        }
        return null;
    }

    public static RefreshTimeType getByName(String name) {
        for (RefreshTimeType type : RefreshTimeType.values()) {
            if (type.getName().contains(name)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 计算限制过期时间
     *
     * @return 秒
     */
    public long toCalculateTimeOfIntegralLimit() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime targetTime;
        switch (this) {
            case DAY:
                // 次日0点
                targetTime = now.toLocalDate().plusDays(1).atStartOfDay();
                break;
            case WEEK:
                // 本周周日 0 点（周一为一周开始）
                int daysUntilSunday = DayOfWeek.SUNDAY.getValue() - now.getDayOfWeek().getValue();
                if (daysUntilSunday < 0) {
                    daysUntilSunday += 7;
                }
                targetTime = now.toLocalDate().plusDays(daysUntilSunday).atStartOfDay();
                break;
            case MONTH:
                // 本月最后一天的次日0点
                LocalDate lastDayOfMonth = now.toLocalDate().with(TemporalAdjusters.lastDayOfMonth());
                // 次月1号0点
                targetTime = lastDayOfMonth.plusDays(1).atStartOfDay();
                break;
            case YEAR:
                // 本年最后一天的次日0点
                LocalDate lastDayOfYear = now.toLocalDate().with(TemporalAdjusters.lastDayOfYear());
                // 次年1月1日0点
                targetTime = lastDayOfYear.plusDays(1).atStartOfDay();
                break;
            case NEVER:
                // 次年1月1日0点
                targetTime = LocalDateTime.now().plusYears(99);
                break;
            default:
                throw new IllegalArgumentException("不支持的刷新时间类型: " + this);
        }

        Duration duration = Duration.between(now, targetTime);
        return duration.getSeconds();
    }

    /**
     * 获取开始时间
     *
     * @return 开始时间
     */
    public LocalDateTime getStartTime() {
        LocalDate today = LocalDate.now();
        return switch (this) {
            case DAY -> today.atStartOfDay();
            case WEEK -> today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
            case MONTH -> today.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
            case YEAR -> today.with(TemporalAdjusters.firstDayOfYear()).atStartOfDay();
            default -> throw new IllegalArgumentException("Unsupported RefreshTimeType: " + this);
        };
    }

    /**
     * 获取结束时间
     *
     * @return 结束时间
     */
    public LocalDateTime getStopTime() {
        LocalDate today = LocalDate.now();
        return switch (this) {
            case DAY -> today.atTime(LocalTime.MAX);
            case WEEK -> today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(LocalTime.MAX);
            case MONTH -> today.with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX);
            case YEAR -> today.with(TemporalAdjusters.lastDayOfYear()).atTime(LocalTime.MAX);
            default -> throw new IllegalArgumentException("Unsupported RefreshTimeType: " + this);
        };
    }

    public static void main(String[] args) {
        long calculateTimeOfIntegralLimit = RefreshTimeType.YEAR.toCalculateTimeOfIntegralLimit();
        System.out.println(calculateTimeOfIntegralLimit / 60 / 60 / 24);
    }
}
