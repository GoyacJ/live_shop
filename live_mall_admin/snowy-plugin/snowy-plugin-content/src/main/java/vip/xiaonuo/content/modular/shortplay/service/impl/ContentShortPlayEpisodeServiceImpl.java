package vip.xiaonuo.content.modular.shortplay.service.impl;

import api.shop.MallGoodsApi;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.content.api.dto.ShortPlayEpisodeByActionPageDTO;
import vip.xiaonuo.content.modular.action.service.ContentStatCounterService;
import vip.xiaonuo.content.modular.shortplay.entity.ContentShortPlayEpisode;
import vip.xiaonuo.content.modular.shortplay.mapper.ContentShortPlayEpisodeMapper;
import vip.xiaonuo.content.modular.shortplay.param.ContentShortPlayEpisodeAddParam;
import vip.xiaonuo.content.modular.shortplay.param.ContentShortPlayEpisodeEditParam;
import vip.xiaonuo.content.modular.shortplay.param.ContentShortPlayEpisodeIdParam;
import vip.xiaonuo.content.modular.shortplay.param.ContentShortPlayEpisodePageParam;
import vip.xiaonuo.content.modular.shortplay.service.ContentShortPlayEpisodeService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/14 13:42
 */
@Slf4j
@Component
public class ContentShortPlayEpisodeServiceImpl extends ServiceImpl<ContentShortPlayEpisodeMapper, ContentShortPlayEpisode> implements ContentShortPlayEpisodeService {

    @Inject
    private ContentStatCounterService contentShortPlayRecordService;

    @Inject
    private MallGoodsApi mallGoodsApi;

    @Override
    public Page<ContentShortPlayEpisode> page(ContentShortPlayEpisodePageParam param) {

        QueryWrapper queryWrapper = new QueryWrapper();

        if (StringUtils.isNotEmpty(param.title())) {
            queryWrapper.like(ContentShortPlayEpisode::getTitle, param.title());
        }

        queryWrapper.eq(ContentShortPlayEpisode::getShortPlayId, param.shortPlayId());

        if (ObjectUtil.isNotEmpty(param.startCreateTime()) && ObjectUtil.isNotEmpty(param.endCreateTime())) {
            queryWrapper.between(ContentShortPlayEpisode::getCreateTime, param.startCreateTime(), param.endCreateTime());
        }

        // 关键词查询
        if (ObjectUtil.isNotEmpty(param.searchKey())) {
            // sql注入处理
            queryWrapper.and(wq -> {
                wq.where(wq1 -> {
                    wq1.or(ContentShortPlayEpisode::getTitle).like(param.searchKey());
                });
            });
        }

        if (ObjectUtil.isAllNotEmpty(param.sortField(), param.sortOrder())) {
            CommonSortOrderEnum.validate(param.sortOrder());
            queryWrapper.orderBy(CharSequenceUtil.toUnderlineCase(param.sortField()), param.sortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(ContentShortPlayEpisode::getCreateTime, false);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);

    }

    @Override
    public void add(ContentShortPlayEpisodeAddParam param) {
        this.save(param.toBean());
    }

    @Override
    public void edit(ContentShortPlayEpisodeEditParam param) {
        this.updateById(param.toBean());
    }

    @Override
    public void delete(CommonValidList<ContentShortPlayEpisodeIdParam> param) {
        this.removeByIds(param.getList().stream().map(ContentShortPlayEpisodeIdParam::id).toList());
    }

    @Override
    public void batchAdd(CommonValidList<ContentShortPlayEpisodeAddParam> params) {
        this.saveBatch(params.getList().stream().map(ContentShortPlayEpisodeAddParam::toBean).toList());
    }

    @Override
    public Page<ContentShortPlayEpisode> shortPlayEpisodeByActionPage(ShortPlayEpisodeByActionPageDTO dto) {
        QueryWrapper queryWrapper = new QueryWrapper();

        if (StringUtils.isNotEmpty(dto.title())) {
            queryWrapper.like(ContentShortPlayEpisode::getTitle, dto.title());
        }

        queryWrapper.eq(ContentShortPlayEpisode::getShortPlayId, dto.shortPlayId());
        queryWrapper.orderBy(ContentShortPlayEpisode::getEpisodeNumber);
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);

    }
}
