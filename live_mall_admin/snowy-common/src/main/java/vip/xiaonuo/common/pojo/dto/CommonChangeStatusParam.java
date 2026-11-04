package vip.xiaonuo.common.pojo.dto;

import vip.xiaonuo.common.enums.SimpleStatus;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/30 16:36
 */
public record CommonChangeStatusParam(String id, SimpleStatus status) {
}
