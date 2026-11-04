/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.live.modular.livebiz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.live.api.live.LiveApi;
import vip.xiaonuo.live.api.openim.OpenImApi;
import vip.xiaonuo.live.modular.anchor.entity.Anchor;
import vip.xiaonuo.live.modular.anchor.mapper.AnchorMapper;
import vip.xiaonuo.live.modular.livebiz.entity.LiveBiz;
import vip.xiaonuo.live.modular.livebiz.mapper.LiveBizMapper;
import vip.xiaonuo.live.modular.livebiz.param.LiveBizAddParam;
import vip.xiaonuo.live.modular.livebiz.param.LiveBizEditParam;
import vip.xiaonuo.live.modular.livebiz.param.LiveBizIdParam;
import vip.xiaonuo.live.modular.livebiz.param.LiveBizPageParam;
import vip.xiaonuo.live.modular.livebiz.service.LiveBizService;
import vip.xiaonuo.live.modular.streamingtype.entity.LiveStreamingType;
import vip.xiaonuo.live.modular.streamingtype.service.LiveStreamingTypeService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 直播列表Service接口实现类
 *
 * @author zsj
 * @date  2025/04/02 16:57
 **/
@Slf4j
@Component
public class LiveBizServiceImpl extends ServiceImpl<LiveBizMapper, LiveBiz> implements LiveBizService {

    @Inject
    private AnchorMapper anchorMapper;

    @Inject
    private LiveStreamingTypeService liveStreamingTypeService;

    @Inject
    private LiveApi liveApi;

    @Inject
    private OpenImApi openImApi;

    @Override
    public Page<LiveBiz> page(LiveBizPageParam liveBizPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (ObjectUtil.isNotEmpty(liveBizPageParam.getLiveRoomState())) {
            queryWrapper.eq(LiveBiz::getLiveRoomState, liveBizPageParam.getLiveRoomState());
        }

        // 关键字查询
        if (ObjectUtil.isNotEmpty(liveBizPageParam.getSearchKey())) {
            queryWrapper.and(
                    wrap -> {
                        wrap.where(
                                q -> {
                                    q.or(LiveBiz::getMainTitle).like(liveBizPageParam.getSearchKey());
                                    q.or(LiveBiz::getSubTitle).like(liveBizPageParam.getSearchKey());
                                });
                    });
        }

        if(ObjectUtil.isAllNotEmpty(liveBizPageParam.getSortField(), liveBizPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(liveBizPageParam.getSortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(liveBizPageParam.getSortField()),liveBizPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(LiveBiz::getCreateTime, false);
        }
        Page<LiveBiz> page = this.page(CommonPageRequest.defaultPage(), queryWrapper);
        if (ObjectUtil.isNotEmpty(page.getRecords())) {
            List<Anchor> anchors = anchorMapper.selectListByIds(page.getRecords().stream().map(LiveBiz::getAnchorId).collect(Collectors.toList()));
            Map<Integer, String> anchorsMap = CollStreamUtil.toMap(anchors, Anchor::getId, Anchor::getRealName);

            List<LiveStreamingType> liveTypeList = liveStreamingTypeService.getLiveTypeList();
            Map<String, String> liveTypeMap = CollStreamUtil.toMap(liveTypeList, LiveStreamingType::getId, LiveStreamingType::getTitle);
            page.getRecords().forEach(liveBiz -> {
                // 转换主播姓名
                liveBiz.setAnchorName(anchorsMap.get(liveBiz.getAnchorId()));
                // 转换直播类型
                liveBiz.setLiveStateName(liveTypeMap.get(liveBiz.getLiveState()));
            });
        }
        return page;
    }

    @Tran
    @Override
    public void add(LiveBizAddParam liveBizAddParam) {
        if (liveBizAddParam.getLiveBeginTime().after(liveBizAddParam.getLiveOverTime())) {
            throw new CommonException("开始时间必须小于结束时间");
        }
        LiveBiz liveBiz = BeanUtil.toBean(liveBizAddParam, LiveBiz.class);
        Map<String, String> liveAddress = liveApi.getLiveAddress();
        // 直播场次id设置
        int liveBroadcasts = (int) (Math.random() * 1000000);
        liveBiz.setLiveBroadcasts(liveBroadcasts);
        liveBiz.setIngestUrl(liveAddress.get("push"));
        liveBiz.setLiveUrl(liveAddress.get("pull"));
        liveBiz.setLiveRoomState("1"); // 待开播
        this.save(liveBiz);
        // 1.获取token 2.创建用户 3.创建群组
        Map<String, String> group = openImApi.createGroup(liveBiz.getId(), liveBiz.getMainTitle());
        log.info("创建群组：{}", group);
        if (group.isEmpty() || !"200".equals(group.get("code"))) {
            // 创建群组失败
//            throw new CommonException("直播间新建失败，群组未成功初始化");
        }
    }

    @Tran
    @Override
    public void edit(LiveBizEditParam liveBizEditParam) {
        if (liveBizEditParam.getLiveBeginTime().after(liveBizEditParam.getLiveOverTime())) {
            throw new CommonException("开始时间必须小于结束时间");
        }
        LiveBiz liveBiz = this.queryEntity(liveBizEditParam.getId());
        BeanUtil.copyProperties(liveBizEditParam, liveBiz);
        this.updateById(liveBiz);
    }

    @Tran
    @Override
    public void delete(List<LiveBizIdParam> liveBizIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(liveBizIdParamList, LiveBizIdParam::getId));
    }

    @Override
    public LiveBiz detail(LiveBizIdParam liveBizIdParam) {
        return this.queryEntity(liveBizIdParam.getId());
    }

    @Override
    public LiveBiz queryEntity(String id) {
        LiveBiz liveBiz = this.getById(id);
        if(ObjectUtil.isEmpty(liveBiz)) {
            throw new CommonException("直播列表不存在，id值为：{}", id);
        }
        return liveBiz;
    }
}
