package vip.xiaonuo.content.modular.action.service;

import com.mybatisflex.core.service.IService;
import vip.xiaonuo.content.modular.action.entity.ContentWatchProgress;
import vip.xiaonuo.content.modular.action.entity.ContentWatchRecord;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 18:19
 */
public interface ContentWatchRecordService extends IService<ContentWatchRecord> {

    /**
     * 添加观看记录
     *
     * @param contentWatchProgress 观看记录
     */
    void add(ContentWatchProgress contentWatchProgress);
}
