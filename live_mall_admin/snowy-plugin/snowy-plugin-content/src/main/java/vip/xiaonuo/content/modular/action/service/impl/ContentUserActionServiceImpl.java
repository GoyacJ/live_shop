package vip.xiaonuo.content.modular.action.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.content.api.dto.ContentUserActionDTO;
import vip.xiaonuo.content.api.dto.ContentUserActionInfoDTO;
import vip.xiaonuo.content.api.enums.ContentActionType;
import vip.xiaonuo.content.modular.action.entity.ContentUserAction;
import vip.xiaonuo.content.modular.action.mapper.ContentUserActionMapper;
import vip.xiaonuo.content.modular.action.service.ContentStatCounterService;
import vip.xiaonuo.content.modular.action.service.ContentUserActionService;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 11:21
 */
@Tran
@Slf4j
@Component
public class ContentUserActionServiceImpl extends ServiceImpl<ContentUserActionMapper, ContentUserAction> implements ContentUserActionService {

    @Inject
    private ContentStatCounterService contentStatCounterService;

    @Override
    public ContentUserActionInfoDTO getUserActionInfo(String userId, String videoId) {
        Set<ContentActionType> actionTypes = this.list(new QueryWrapper()
                        .eq(ContentUserAction::getUserId, userId)
                        .eq(ContentUserAction::getVideoId, videoId)
                        .eq(ContentUserAction::getIsActive, SimpleStatus.ENABLE)
                ).stream()
                .map(ContentUserAction::getContentActionType)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return ContentUserActionInfoDTO.builder()
                .like(actionTypes.contains(ContentActionType.LIKE))
                .collect(actionTypes.contains(ContentActionType.COLLECT))
                .comment(actionTypes.contains(ContentActionType.COMMENT))
                .share(actionTypes.contains(ContentActionType.SHARE))
                .build();
    }

    @Override
    public void action(ContentUserActionDTO dto) {
        ContentUserAction contentUserAction = this.getOne(new QueryWrapper()
                .eq(ContentUserAction::getUserId, dto.userId())
                .eq(ContentUserAction::getVideoId, dto.videoId())
                .eq(ContentUserAction::getContentActionType, dto.contentActionType().getCode()));
        if (Objects.nonNull(contentUserAction)) {
            contentUserAction.setIsActive(dto.isActive());
            this.updateById(contentUserAction);
        }else{
            ContentUserAction userAction = new ContentUserAction();
            userAction.setUserId(dto.userId());
            userAction.setVideoId(dto.videoId());
            userAction.setContentActionType(dto.contentActionType());
            userAction.setActionTime(LocalDateTime.now());
            userAction.setIsActive(dto.isActive());
            this.save(userAction);
        }

        contentStatCounterService.addVideoStats(dto.videoId(), dto.contentActionType(),dto.isActive());
    }
}
