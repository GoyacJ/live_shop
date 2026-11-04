package vip.xiaonuo.content.modular.action.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import vip.xiaonuo.content.modular.action.entity.ContentWatchProgress;
import vip.xiaonuo.content.modular.action.entity.ContentWatchRecord;
import vip.xiaonuo.content.modular.action.mapper.ContentWatchRecordMapper;
import vip.xiaonuo.content.modular.action.service.ContentWatchRecordService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 18:19
 */
@Slf4j
@Component
public class ContentWatchRecordServiceImpl extends ServiceImpl<ContentWatchRecordMapper, ContentWatchRecord> implements ContentWatchRecordService {

    @Override
    public void add(ContentWatchProgress contentWatchProgress) {
        ContentWatchRecord bean = BeanUtil.toBean(contentWatchProgress, ContentWatchRecord.class);
        bean.setId(null);
        this.save(bean);
    }
}
