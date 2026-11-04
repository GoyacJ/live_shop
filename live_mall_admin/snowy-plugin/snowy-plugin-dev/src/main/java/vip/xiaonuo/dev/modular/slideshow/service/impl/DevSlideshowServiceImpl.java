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
package vip.xiaonuo.dev.modular.slideshow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.solon.annotation.Component;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.dev.core.conts.DevConstants;
import vip.xiaonuo.dev.modular.slideshow.entity.DevSlideshow;
import vip.xiaonuo.dev.modular.slideshow.enums.DevSlideshowStatusEnum;
import vip.xiaonuo.dev.modular.slideshow.mapper.DevSlideshowMapper;
import vip.xiaonuo.dev.modular.slideshow.param.DevSlideshowAddParam;
import vip.xiaonuo.dev.modular.slideshow.param.DevSlideshowEditParam;
import vip.xiaonuo.dev.modular.slideshow.param.DevSlideshowIdParam;
import vip.xiaonuo.dev.modular.slideshow.param.DevSlideshowPageParam;
import vip.xiaonuo.dev.modular.slideshow.service.DevSlideshowService;

import java.util.List;

/**
 * 轮播图Service接口实现类
 *
 * @author yubaoshan
 * @date  2024/07/13 00:31
 **/
@Component
public class DevSlideshowServiceImpl extends ServiceImpl<DevSlideshowMapper, DevSlideshow> implements DevSlideshowService {

    @Override
    public Page<DevSlideshow> page(DevSlideshowPageParam devSlideshowPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(devSlideshowPageParam.getTitle())) {
            queryWrapper.like(DevSlideshow::getTitle, devSlideshowPageParam.getTitle());
        }
        if(ObjectUtil.isNotEmpty(devSlideshowPageParam.getPlace())) {
            queryWrapper.like(DevSlideshow::getPlace, devSlideshowPageParam.getPlace());
        }
        if(ObjectUtil.isNotEmpty(devSlideshowPageParam.getStatus())) {
            queryWrapper.eq(DevSlideshow::getStatus, devSlideshowPageParam.getStatus());
        }
        if(ObjectUtil.isAllNotEmpty(devSlideshowPageParam.getSortField(), devSlideshowPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(devSlideshowPageParam.getSortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(devSlideshowPageParam.getSortField()),devSlideshowPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(DevSlideshow::getSortCode);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Tran
    @Override
    public void add(DevSlideshowAddParam devSlideshowAddParam) {
        DevSlideshow devSlideshow = BeanUtil.toBean(devSlideshowAddParam, DevSlideshow.class);
        this.save(devSlideshow);
    }

    @Tran
    @Override
    public void edit(DevSlideshowEditParam devSlideshowEditParam) {
        DevSlideshow devSlideshow = this.queryEntity(devSlideshowEditParam.getId());
        BeanUtil.copyProperties(devSlideshowEditParam, devSlideshow);
        this.updateById(devSlideshow);
    }

    @Tran
    @Override
    public void delete(List<DevSlideshowIdParam> devSlideshowIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(devSlideshowIdParamList, DevSlideshowIdParam::getId));
    }

    @Override
    public DevSlideshow detail(DevSlideshowIdParam devSlideshowIdParam) {
        return this.queryEntity(devSlideshowIdParam.getId());
    }

    @Override
    public DevSlideshow queryEntity(String id) {
        DevSlideshow devSlideshow = this.getById(id);
        if(ObjectUtil.isEmpty(devSlideshow)) {
            throw new CommonException("轮播图不存在，id值为：{}", id);
        }
        return devSlideshow;
    }

    @Tran
    @Override
    public void disableStatus(DevSlideshowIdParam devSlideshowIdParam) {
        this.updateChain().eq(DevSlideshow::getId, devSlideshowIdParam.getId()).set(DevSlideshow::getStatus,
                DevSlideshowStatusEnum.DISABLE.getValue()).update();
    }

    @Tran
    @Override
    public void enableStatus(DevSlideshowIdParam devSlideshowIdParam) {
        this.updateChain().eq(DevSlideshow::getId, devSlideshowIdParam.getId()).set(DevSlideshow::getStatus,
                DevSlideshowStatusEnum.ENABLE.getValue()).update();
    }

    @Override
    public List<JSONObject> getListByPlace(String place){
        List<JSONObject> resultList = CollUtil.newArrayList();
        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.eq(DevSlideshow::getStatus,
                DevSlideshowStatusEnum.ENABLE.getValue()).orderBy(DevSlideshow::getSortCode);
        List<DevSlideshow> slideshowList = this.list(queryWrapper);
        slideshowList.forEach((item) -> {
            JSONArray slideshowPlaceArray = JSONUtil.parseArray(item.getPlace());
            slideshowPlaceArray.forEach((placeArray) -> {
                if (place.equals(placeArray)) {
                    JSONObject obj = new JSONObject();
                    obj.set("id", item.getId());
                    obj.set("title", item.getTitle());
                    // 判断这个图片是否库里真是存在，不存在则返回base64国定的，不能让前端图片裂开
                    obj.set("image", item.getImage());
                    obj.set("sortCode", item.getSortCode());
                    JSONArray array = JSONUtil.parseArray(item.getPathDetails());
                    array.forEach((detail) -> {
                        JSONObject detailObj = JSONUtil.parseObj(detail);
                        if (detailObj.get("key").equals(place)) {
                            obj.set("pathDetails", detailObj);
                        }
                    });
                    resultList.add(obj);
                }
            });
        });
        if (resultList.isEmpty()) {
            // 如果库里未配置，则补充一条静态的，避免图片为空
            JSONObject staticObj = new JSONObject();
            staticObj.set("id", IdUtil.getSnowflake().nextIdStr());
            staticObj.set("title", "静态文件");
            staticObj.set("image", DevConstants.STATIC_SLIDESHOW_IMAGE);
            staticObj.set("pathDetails", null);
            resultList.add(staticObj);
            return resultList;
        }
        return resultList;
    }
}
