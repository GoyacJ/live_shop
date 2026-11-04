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
package vip.xiaonuo.live.modular.gift.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.solon.annotation.Component;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.live.modular.gift.entity.Gift;
import vip.xiaonuo.live.modular.gift.mapper.GiftMapper;
import vip.xiaonuo.live.modular.gift.param.GiftAddParam;
import vip.xiaonuo.live.modular.gift.param.GiftEditParam;
import vip.xiaonuo.live.modular.gift.param.GiftIdParam;
import vip.xiaonuo.live.modular.gift.param.GiftPageParam;
import vip.xiaonuo.live.modular.gift.service.GiftService;

import java.util.List;

/**
 * 礼物表Service接口实现类
 *
 * @author lgq
 * @date  2025/03/04 14:13
 **/
@Component
public class GiftServiceImpl extends ServiceImpl<GiftMapper, Gift> implements GiftService {

    @Override
    public Page<Gift> page(GiftPageParam giftPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isAllNotEmpty(giftPageParam.getSortField(), giftPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(giftPageParam.getSortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(giftPageParam.getSortField()),giftPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(Gift::getId);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Tran
    @Override
    public void add(GiftAddParam giftAddParam) {
        Gift gift = BeanUtil.toBean(giftAddParam, Gift.class);
        this.save(gift);
    }

    @Tran
    @Override
    public void edit(GiftEditParam giftEditParam) {
        Gift gift = this.queryEntity(giftEditParam.getId());
        BeanUtil.copyProperties(giftEditParam, gift);
        this.updateById(gift);
    }

    @Tran
    @Override
    public void delete(List<GiftIdParam> giftIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(giftIdParamList, GiftIdParam::getId));
    }

    @Override
    public Gift detail(GiftIdParam giftIdParam) {
        return this.queryEntity(giftIdParam.getId());
    }

    @Override
    public Gift queryEntity(String id) {
        Gift gift = this.getById(id);
        if(ObjectUtil.isEmpty(gift)) {
            throw new CommonException("礼物表不存在，id值为：{}", id);
        }
        return gift;
    }
}
