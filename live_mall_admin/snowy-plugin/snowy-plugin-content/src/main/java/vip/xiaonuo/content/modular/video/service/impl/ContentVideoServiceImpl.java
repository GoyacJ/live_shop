package vip.xiaonuo.content.modular.video.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.content.api.dto.ContentStatsDTO;
import vip.xiaonuo.content.api.dto.VideoByActionPageDTO;
import vip.xiaonuo.content.api.enums.ContentType;
import vip.xiaonuo.content.api.enums.VideoType;
import vip.xiaonuo.content.modular.action.service.ContentStatCounterService;
import vip.xiaonuo.content.modular.video.dto.ContentVideoDTO;
import vip.xiaonuo.content.modular.video.entity.ContentVideo;
import vip.xiaonuo.content.modular.video.mapper.ContentVideoMapper;
import vip.xiaonuo.content.modular.video.param.*;
import vip.xiaonuo.content.modular.video.service.ContentVideoService;

import java.util.Objects;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/13 13:30
 */
@Slf4j
@Component
public class ContentVideoServiceImpl extends ServiceImpl<ContentVideoMapper, ContentVideo> implements ContentVideoService {


    @Inject
    private ContentStatCounterService contentStatCounterService;

    @Override
    public Page<ContentVideoDTO> page(ContentVideoPageParam param) {
        QueryWrapper queryWrapper = new QueryWrapper();

        if (StringUtils.isNotEmpty(param.title())) {
            queryWrapper.like(ContentVideo::getTitle, param.title());
        }

        if (StringUtils.isNotEmpty(param.category())) {
            queryWrapper.eq(ContentVideo::getCategory, param.category());
        }

        if (Objects.nonNull(param.status())) {
            queryWrapper.eq(ContentVideo::getStatus, param.status());
        }

        if (ObjectUtil.isNotEmpty(param.startCreateTime()) && ObjectUtil.isNotEmpty(param.endCreateTime())) {
            queryWrapper.between(ContentVideo::getCreateTime, param.startCreateTime(), param.endCreateTime());
        }

        // 关键词查询
        if (ObjectUtil.isNotEmpty(param.searchKey())) {
            // sql注入处理
            queryWrapper.and(wq -> {
                wq.where(wq1 -> {
                    wq1.or(ContentVideo::getTitle).like(param.searchKey());
                });
            });
        }

        if (ObjectUtil.isAllNotEmpty(param.sortField(), param.sortOrder())) {
            CommonSortOrderEnum.validate(param.sortOrder());
            queryWrapper.orderBy(CharSequenceUtil.toUnderlineCase(param.sortField()), param.sortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(ContentVideo::getCreateTime, false);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper)
                .map(video -> {
                    ContentVideoDTO dto = BeanUtil.copyProperties(video, ContentVideoDTO.class);
                    ContentStatsDTO contentStats = contentStatCounterService.getContentStats(video.getId(), ContentType.VIDEO);
                    dto.setContentStats(contentStats);
                    return dto;
                });
    }

    @Override
    public ContentVideo detail(String id) {
        return this.getById(id);
    }

    @Override
    public void add(ContentVideoAddParam param) {
        this.save(param.toBean());
    }

    @Override
    public void edit(ContentVideoEditParam param) {
        this.updateById(param.toBean());
    }

    @Override
    public void delete(CommonValidList<ContentVideoIdParam> param) {
        this.removeByIds(param.getList().stream().map(ContentVideoIdParam::id).toList());
    }

    @Override
    public void changeStatus(ContentVideoChangeStatusParam param) {

        if (param.status() == 1) {
            ContentVideo contentVideo = this.getById(param.id());
            String category = contentVideo.getCategory();
            if (StringUtils.isEmpty(category)) {
                throw new CommonException("视频发布失败,视频分类不能为空");
            }
        }

        UpdateChain.of(ContentVideo.class)
                .eq(ContentVideo::getId, param.id())
                .set(ContentVideo::getStatus, param.status())
                .update();
    }

    @Override
    public void batchAdd(CommonValidList<ContentVideoAddParam> params) {
        this.saveBatch(params.getList().stream().map(ContentVideoAddParam::toBean).toList());
    }

    @Override
    public Page<ContentVideoDTO> videoByActionPage(VideoByActionPageDTO dto) {
        QueryWrapper queryWrapper = new QueryWrapper();

        if (StringUtils.isNotEmpty(dto.title())) {
            queryWrapper.like(ContentVideo::getTitle, dto.title());
        }

        queryWrapper.eq(ContentVideo::getStatus, 1);
//        queryWrapper.orderBy(ContentVideo::getSort, false);
        queryWrapper.ne(ContentVideo::getCategory, VideoType.SHORT_PLAY.getCode());
        return this.page(CommonPageRequest.defaultPage(), queryWrapper)
                .map(video -> {
                    ContentVideoDTO res = BeanUtil.copyProperties(video, ContentVideoDTO.class);
                    ContentStatsDTO contentStats = contentStatCounterService.getContentStats(video.getId(), ContentType.VIDEO);
                    res.setContentStats(contentStats);
                    return res;
                });
    }

    @Override
    public ContentVideoDTO getVideoByAction(String videoId) {
        ContentVideo contentVideo = this.getById(videoId);
        ContentVideoDTO res = BeanUtil.copyProperties(contentVideo, ContentVideoDTO.class);
        ContentStatsDTO contentStats = contentStatCounterService.getContentStats(videoId, ContentType.VIDEO);
        res.setContentStats(contentStats);
        return res;
    }
}
