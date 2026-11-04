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
package vip.xiaonuo.manage.modular.feedback.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.CollectionUtil;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import org.noear.solon.annotation.Component;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.manage.modular.feedback.entity.BizFeedback;
import vip.xiaonuo.manage.modular.feedback.mapper.BizFeedbackMapper;
import vip.xiaonuo.manage.modular.feedback.param.*;
import vip.xiaonuo.manage.modular.feedback.service.BizFeedBackService;
import vip.xiaonuo.manage.modular.userinfo.entity.UserInfo;
import vip.xiaonuo.manage.modular.userinfo.param.UserInfoIdParam;

import java.util.List;

/**
 * 反馈意见Service接口实现类
 *
 */
@Component
public class BizFeedbackServiceImpl extends ServiceImpl<BizFeedbackMapper, BizFeedback> implements BizFeedBackService {

    @Override
    public Page<BizFeedback> page(BizFeedbackPageParam bizFeedbackPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(bizFeedbackPageParam.getMobile())) {
            queryWrapper.like(BizFeedback::getMobile, bizFeedbackPageParam.getMobile());
        }
        if(ObjectUtil.isNotEmpty(bizFeedbackPageParam.getStartCreateTime()) && ObjectUtil.isNotEmpty(bizFeedbackPageParam.getEndCreateTime())) {
            queryWrapper.between(BizFeedback::getCreateTime, bizFeedbackPageParam.getStartCreateTime(), bizFeedbackPageParam.getEndCreateTime());
        }
        queryWrapper.orderBy(BizFeedback::getCreateTime,false);
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public void edit(BizFeedbackEditParam bizFeedbackEditParam) {
        BizFeedback bizFeedback = this.getById(bizFeedbackEditParam.getId());
        if(ObjectUtil.isEmpty(bizFeedback)) {
            throw new CommonException("反馈意见不存在，id值为：{}", bizFeedbackEditParam.getId());
        }
        BeanUtil.copyProperties(bizFeedbackEditParam, bizFeedback);
        this.updateById(bizFeedback);
    }

    @Override
    public void delete(List<BizFeedbackIdParam> bizFeedbackIdParamList) {
        this.removeByIds(CollStreamUtil.toList(bizFeedbackIdParamList, BizFeedbackIdParam::getId));
    }

    @Override
    public BizFeedback detail(BizFeedbackIdParam BizFeedbackIdParam) {
        BizFeedback bizFeedback = this.getById(BizFeedbackIdParam.getId());
        if(ObjectUtil.isEmpty(bizFeedback)) {
            throw new CommonException("反馈意见不存在，id值为：{}", BizFeedbackIdParam.getId());
        }
        return bizFeedback;
    }

    @Override
    public void add(BizFeedbackAddParam bizFeedbackAddParam) {
        BizFeedback bizFeedback = BeanUtil.toBean(bizFeedbackAddParam, BizFeedback.class);
        this.save(bizFeedback);
    }

    @Override
    public List<BizFeedback> detailByMobile(BizFeedbackMobileParam bizFeedbackMobileParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(ObjectUtil.isNotEmpty(bizFeedbackMobileParam.getMobile())) {
            queryWrapper.eq("mobile", bizFeedbackMobileParam.getMobile());
        }
        List<BizFeedback> list = this.list(queryWrapper);
        return list;
    }
}
