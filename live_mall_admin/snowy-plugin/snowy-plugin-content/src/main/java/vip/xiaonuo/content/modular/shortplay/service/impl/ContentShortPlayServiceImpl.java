package vip.xiaonuo.content.modular.shortplay.service.impl;

import api.shop.MallGoodsApi;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.IterUtil;
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
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.content.api.dto.ContentStatsDTO;
import vip.xiaonuo.content.api.dto.ShortPlayByActionPageDTO;
import vip.xiaonuo.content.api.enums.ContentType;
import vip.xiaonuo.content.modular.action.service.ContentStatCounterService;
import vip.xiaonuo.content.modular.shortplay.entity.ContentShortPlay;
import vip.xiaonuo.content.modular.shortplay.entity.ContentShortPlayEpisode;
import vip.xiaonuo.content.modular.shortplay.mapper.ContentShortPlayMapper;
import vip.xiaonuo.content.modular.shortplay.param.*;
import vip.xiaonuo.content.modular.shortplay.service.ContentShortPlayEpisodeService;
import vip.xiaonuo.content.modular.shortplay.service.ContentShortPlayService;
import vip.xiaonuo.content.modular.video.dto.ContentShortPlayDTO;
import vip.xiaonuo.content.modular.video.service.ContentVideoService;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/13 13:33
 */
@Slf4j
@Component
public class ContentShortPlayServiceImpl extends ServiceImpl<ContentShortPlayMapper, ContentShortPlay> implements ContentShortPlayService {

    @Inject
    private ContentShortPlayEpisodeService contentShortPlayEpisodeService;

    @Inject
    private ContentStatCounterService contentShortPlayRecordService;

    @Inject
    private ContentVideoService contentVideoService;

    @Inject
    private MallGoodsApi mallGoodsApi;

    @Inject
    private ContentStatCounterService contentStatCounterService;

    @Override
    public Page<ContentShortPlayDTO> page(ContentShortPlayPageParam param) {
        QueryWrapper queryWrapper = new QueryWrapper();

        if (StringUtils.isNotEmpty(param.title())) {
            queryWrapper.like(ContentShortPlay::getTitle, param.title());
        }

        if (StringUtils.isNotEmpty(param.category())) {
            queryWrapper.eq(ContentShortPlay::getCategory, param.category());
        }

        if (Objects.nonNull(param.status())) {
            queryWrapper.eq(ContentShortPlay::getStatus, param.status());
        }

        if (ObjectUtil.isNotEmpty(param.startCreateTime()) && ObjectUtil.isNotEmpty(param.endCreateTime())) {
            queryWrapper.between(ContentShortPlay::getCreateTime, param.startCreateTime(), param.endCreateTime());
        }

        // 关键词查询
        if (ObjectUtil.isNotEmpty(param.searchKey())) {
            // sql注入处理
            queryWrapper.and(wq -> {
                wq.where(wq1 -> {
                    wq1.or(ContentShortPlay::getTitle).like(param.searchKey());
                });
            });
        }

        if (ObjectUtil.isAllNotEmpty(param.sortField(), param.sortOrder())) {
            CommonSortOrderEnum.validate(param.sortOrder());
            queryWrapper.orderBy(CharSequenceUtil.toUnderlineCase(param.sortField()), param.sortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(ContentShortPlay::getCreateTime, false);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper)
                .map(shortPlay -> {
                    ContentShortPlayDTO dto = BeanUtil.copyProperties(shortPlay, ContentShortPlayDTO.class);
                    ContentStatsDTO contentStats = contentStatCounterService.getContentStats(shortPlay.getId(), ContentType.SHORT_PLAY);
                    dto.setContentStats(contentStats);
                    return dto;
                });
    }

    @Override
    public ContentShortPlay detail(String id) {
        return this.getById(id);
    }

    @Override
    public void add(ContentShortPlayAddParam param) {
        this.save(param.toBean());
    }

    @Override
    public void edit(ContentShortPlayEditParam param) {
        this.updateById(param.toBean());
    }

    @Override
    public void delete(CommonValidList<ContentShortPlayIdParam> param) {
        this.removeByIds(param.getList().stream().map(ContentShortPlayIdParam::id).toList());

        this.contentShortPlayEpisodeService.remove(new QueryWrapper().in(ContentShortPlayEpisode::getShortPlayId, param.getList().stream().map(ContentShortPlayIdParam::id).collect(Collectors.toList())));
    }

    @Override
    public void changeStatus(ContentShortPlayChangeStatusParam param) {
        UpdateChain.of(ContentShortPlay.class)
                .eq(ContentShortPlay::getId, param.id())
                .set(ContentShortPlay::getStatus, param.status())
                .update();
    }

    @Override
    public Page<ContentShortPlayEpisode> episodePage(ContentShortPlayEpisodePageParam param) {
        return this.contentShortPlayEpisodeService.page(param);
    }

    @Override
    public ContentShortPlayEpisode episodeDetail(String id) {
        return this.contentShortPlayEpisodeService.getById(id);
    }

    @Override
    @Tran
    public void episodeAdd(ContentShortPlayEpisodeAddParam param) {
        this.contentShortPlayEpisodeService.add(param);
        ContentShortPlay shortPlay = this.getById(param.shortPlayId());
        Integer episodeCount = shortPlay.getEpisodeCount();
        if (Objects.isNull(episodeCount)) {
            episodeCount = 1;
        } else {
            episodeCount += 1;
        }
        shortPlay.setEpisodeCount(episodeCount);
        this.updateById(shortPlay);
    }

    @Override
    public void episodeEdit(ContentShortPlayEpisodeEditParam param) {
        this.contentShortPlayEpisodeService.edit(param);
    }

    @Override
    @Tran
    public void episodeDelete(CommonValidList<ContentShortPlayEpisodeIdParam> param) {
        if (IterUtil.isNotEmpty(param.iterator())) {
            ContentShortPlayEpisode episode = this.contentShortPlayEpisodeService.getById(param.get(0).id());
            ContentShortPlay shortPlay = this.getById(episode.getShortPlayId());
            Integer episodeCount = shortPlay.getEpisodeCount();
            if (Objects.nonNull(episodeCount) && episodeCount >= param.getList().size()) {
                episodeCount = episodeCount - param.getList().size();
                shortPlay.setEpisodeCount(episodeCount);
                this.updateById(shortPlay);
            }
        }
        this.contentShortPlayEpisodeService.delete(param);
    }

    @Override
    public void episodeBatchAdd(CommonValidList<ContentShortPlayEpisodeAddParam> params) {
        if (IterUtil.isNotEmpty(params)) {
            this.contentShortPlayEpisodeService.batchAdd(params);
            ContentShortPlay shortPlay = this.getById(params.get(0).shortPlayId());
            Integer episodeCount = shortPlay.getEpisodeCount();
            if (Objects.isNull(episodeCount)) {
                episodeCount = 1;
            } else {
                episodeCount += params.size();
            }
            shortPlay.setEpisodeCount(episodeCount);
            this.updateById(shortPlay);
        }
    }

    @Override
    public Page<ContentShortPlay> shortPlayIndexByActionPage(ShortPlayByActionPageDTO dto) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotEmpty(dto.title())) {
            queryWrapper.like(ContentShortPlay::getTitle, dto.title());
        }

        queryWrapper.eq(ContentShortPlay::getStatus, 1);
        queryWrapper.orderBy(ContentShortPlay::getRecommendLevel, false);
        queryWrapper.orderBy(ContentShortPlay::getSort, false);
        queryWrapper.orderBy(ContentShortPlay::getCreateTime, false);
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public Page<ContentShortPlayDTO> shortPlayByActionPage(ShortPlayByActionPageDTO dto) {
        QueryWrapper queryWrapper = new QueryWrapper();

        if (StringUtils.isNotEmpty(dto.title())) {
            queryWrapper.like(ContentShortPlay::getTitle, dto.title());
        }

        queryWrapper.eq(ContentShortPlay::getStatus, 1);
        queryWrapper.orderBy(ContentShortPlay::getRecommendLevel, false);
        queryWrapper.orderBy(ContentShortPlay::getSort, false);
        queryWrapper.orderBy(ContentShortPlay::getCreateTime, false);
        return this.page(CommonPageRequest.defaultPage(), queryWrapper)
                .map(shortPlay -> {
                    ContentShortPlayDTO res = BeanUtil.copyProperties(shortPlay, ContentShortPlayDTO.class);
                    ContentStatsDTO contentStats = contentStatCounterService.getContentStats(shortPlay.getId(), ContentType.SHORT_PLAY);
                    res.setContentStats(contentStats);
                    return res;
                });
    }
}
