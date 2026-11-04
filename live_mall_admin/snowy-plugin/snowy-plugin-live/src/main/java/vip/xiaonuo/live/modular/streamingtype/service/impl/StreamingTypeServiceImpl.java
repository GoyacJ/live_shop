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
package vip.xiaonuo.live.modular.streamingtype.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.row.AbstractRowHeightStyleStrategy;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.apache.poi.ss.usermodel.*;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.excel.CommonExcelCustomMergeStrategy;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.common.util.CommonDownloadUtil;
import vip.xiaonuo.common.util.CommonResponseUtil;
import vip.xiaonuo.live.modular.streamingtype.entity.LiveStreamingType;
import vip.xiaonuo.live.modular.streamingtype.enums.LiveTypeStatusEnum;
import vip.xiaonuo.live.modular.streamingtype.mapper.LiveStreamingTypeMapper;
import vip.xiaonuo.live.modular.streamingtype.param.LiveStreamingTypeAddParam;
import vip.xiaonuo.live.modular.streamingtype.param.LiveStreamingTypeEditParam;
import vip.xiaonuo.live.modular.streamingtype.param.LiveStreamingTypeIdParam;
import vip.xiaonuo.live.modular.streamingtype.param.LiveStreamingTypePageParam;
import vip.xiaonuo.live.modular.streamingtype.service.LiveStreamingTypeService;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 直播类型Service接口实现类
 *
 **/
@Component
public class StreamingTypeServiceImpl extends ServiceImpl<LiveStreamingTypeMapper, LiveStreamingType> implements LiveStreamingTypeService {

    @Override
    public Page<LiveStreamingType> page(LiveStreamingTypePageParam liveStreamingTypePageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(liveStreamingTypePageParam.getTitle())) {
            queryWrapper.like(LiveStreamingType::getTitle, liveStreamingTypePageParam.getTitle());
        }
        if(ObjectUtil.isNotEmpty(liveStreamingTypePageParam.getStartCreateTime()) && ObjectUtil.isNotEmpty(liveStreamingTypePageParam.getEndCreateTime())) {
            queryWrapper.between(LiveStreamingType::getCreateTime, liveStreamingTypePageParam.getStartCreateTime(), liveStreamingTypePageParam.getEndCreateTime());
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public void add(LiveStreamingTypeAddParam liveStreamingTypeAddParam) {
        if(ObjectUtil.isEmpty(liveStreamingTypeAddParam)) {
            throw new CommonException("直播类型信息不能为空");
        }
        try {
            LiveStreamingType liveStreamingType = BeanUtil.toBean(liveStreamingTypeAddParam, LiveStreamingType.class);
            this.save(liveStreamingType);
        } catch (Exception e) {
            throw new CommonException("直播类型信息添加失败");
        }
    }

    @Override
    public void edit(LiveStreamingTypeEditParam liveStreamingTypeEditParam) {
        LiveStreamingType liveStreamingType = this.getById(liveStreamingTypeEditParam.getId());
        if(ObjectUtil.isEmpty(liveStreamingType)) {
            throw new CommonException("直播类型不存在，id值为：{}", liveStreamingTypeEditParam.getId());
        }
        try {
            BeanUtil.copyProperties(liveStreamingTypeEditParam, liveStreamingType);
            this.updateById(liveStreamingType);
        } catch (Exception e) {
            throw new CommonException("商品信息修改失败");
        }
    }

    @Override
    public void delete(List<LiveStreamingTypeIdParam> liveStreamingTypeIdParamList) {
        try {
            // 执行删除
            this.removeByIds(CollStreamUtil.toList(liveStreamingTypeIdParamList, LiveStreamingTypeIdParam::getId));
        } catch (Exception e) {
            throw new CommonException("直播类型信息删除失败");
        }
    }

    @Override
    public LiveStreamingType detail(LiveStreamingTypeIdParam liveStreamingTypeIdParam) {
        LiveStreamingType liveStreamingType = this.getById(liveStreamingTypeIdParam.getId());
        if(ObjectUtil.isEmpty(liveStreamingType)) {
            throw new CommonException("直播类型不存在，id值为：{}", liveStreamingTypeIdParam.getId());
        }
        return liveStreamingType;
    }

    @Override
    public List<LiveStreamingType> getLiveTypeList() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(LiveStreamingType::getStatus,
                LiveTypeStatusEnum.ENABLE.getValue()).orderBy(LiveStreamingType::getCreateTime,false);
        return this.list(queryWrapper);
    }
}
