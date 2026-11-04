package vip.xiaonuo.shop.modular.mallorder.api;

import api.shop.OrderApi;
import api.shop.dto.OrderManagePageDTO;
import api.shop.dto.OrderManagePageParamDTO;
import api.shop.dto.SettleManagePageDTO;
import api.shop.dto.SettleManagePageParamDTO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import manage.UserApi;
import manage.dto.UserInfoDTO;
import org.jetbrains.annotations.NotNull;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.enums.OrderStatusEnum;
import vip.xiaonuo.common.enums.OrderTypeEnum;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.shop.modular.mallorder.entity.MallOrder;
import vip.xiaonuo.shop.modular.mallorder.service.MallOrderService;

import java.time.ZoneId;
import java.util.Objects;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/22 15:25
 */
@Slf4j
@Component
public class OrderApiImpl implements OrderApi {

    @Inject
    private MallOrderService mallOrderService;
    @Inject
    private UserApi userApi;

    @Override
    public Page<OrderManagePageDTO> orderManagePage(OrderManagePageParamDTO param) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (ObjectUtil.isNotEmpty(param.userName())) {
            queryWrapper.like(MallOrder::getNickname, param.userName());
        }
        if (ObjectUtil.isNotEmpty(param.phone())) {
            queryWrapper.like(MallOrder::getMobile, param.phone());
        }
//        if (ObjectUtil.isNotEmpty(param.invitationCode())) {
//            queryWrapper.like(MallOrder::getRcode, param.invitationCode());
//        }
        if (ObjectUtil.isNotEmpty(param.startCreateTime()) && ObjectUtil.isNotEmpty(param.endCreateTime())) {
            queryWrapper.between(MallOrder::getCreateTime, param.startCreateTime(), param.endCreateTime());
        }
        queryWrapper.and(wq -> {
            wq.ne(MallOrder::getPid, "").isNotNull(MallOrder::getPid);
        });
        if (ObjectUtil.isAllNotEmpty(param.sortField(), param.sortOrder())) {
            CommonSortOrderEnum.validate(param.sortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(param.sortField()), param.sortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(MallOrder::getCreateTime, false);
        }
        return this.mallOrderService.page(CommonPageRequest.defaultPage(), queryWrapper)
                .map(this::convert);
    }

    @Override
    public Page<SettleManagePageDTO> settleManagePage(SettleManagePageParamDTO param) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (ObjectUtil.isNotEmpty(param.userName())) {
            queryWrapper.like(MallOrder::getNickname, param.userName());
        }
        if (ObjectUtil.isNotEmpty(param.phone())) {
            queryWrapper.like(MallOrder::getMobile, param.phone());
        }
//        if (ObjectUtil.isNotEmpty(param.invitationCode())) {
//            queryWrapper.like(MallOrder::getRcode, param.invitationCode());
//        }
        if (ObjectUtil.isNotEmpty(param.startCreateTime()) && ObjectUtil.isNotEmpty(param.endCreateTime())) {
            queryWrapper.between(MallOrder::getSettleTime, param.startCreateTime(), param.endCreateTime());
        }
        queryWrapper.and(wq -> {
            wq.ne(MallOrder::getPid, "").isNotNull(MallOrder::getPid);
        });
        if (ObjectUtil.isAllNotEmpty(param.sortField(), param.sortOrder())) {
            CommonSortOrderEnum.validate(param.sortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(param.sortField()), param.sortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(MallOrder::getCreateTime, false);
        }
        return this.mallOrderService.page(CommonPageRequest.defaultPage(), queryWrapper)
                .map(page -> {
                    SettleManagePageDTO dto = BeanUtil.toBean(page, SettleManagePageDTO.class);
                    UserInfoDTO user = this.userApi.getUserById(page.getCreateUser());
                    if (Objects.nonNull(user)) {
                        dto.setInvitationCode(user.getRcode());
                        dto.setUuid(user.getId());
                        dto.setAccount(user.getMobile());
                        dto.setOrderAccount(user.getMobile());
                        dto.setOrderPhone(user.getMobile());
                    }
                    dto.setParentId(page.getPid());
                    dto.setOrderNumber(page.getOrderNumber());
                    dto.setPhone(page.getMobile());
                    dto.setOrderTime(page.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                    dto.setOrderAmount(page.getOrderFee());
                    dto.setSettleStatus(page.getSettlementStatus());
                    dto.setCommission("");
                    dto.setSettleTime(page.getSettleTime());
                    dto.setUserName(page.getNickname());
                    dto.setCreateTime(dto.getOrderTime());
                    return dto;
                });

    }

    @Override
    public OrderManagePageDTO orderDetail(String orderId) {
        MallOrder mallOrder = this.mallOrderService.getById(orderId);
        if (Objects.nonNull(mallOrder)) {
            return convert(mallOrder);
        }
        return null;
    }

    @NotNull
    private OrderManagePageDTO convert(MallOrder mallOrder) {
        OrderManagePageDTO dto = BeanUtil.toBean(mallOrder, OrderManagePageDTO.class);
        dto.setOrderTime(mallOrder.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        dto.setOrderNumber(mallOrder.getOrderNumber());
        OrderTypeEnum orderTypeEnum = OrderTypeEnum.getByType(mallOrder.getOrderType());
        dto.setOrderName(orderTypeEnum.getDescription());
        dto.setAccountId(mallOrder.getCreateUser());
        dto.setOrderAmount(mallOrder.getOrderFee());
        dto.setOrderStatus(OrderStatusEnum.fromStatus(Integer.parseInt(mallOrder.getStatus())));
        dto.setParentId(mallOrder.getPid());
        UserInfoDTO parentUser = this.userApi.getUserById(mallOrder.getPid());
        if (Objects.nonNull(parentUser)) {
            dto.setParentAccount(parentUser.getMobile());
        }
        dto.setPhone(mallOrder.getMobile());
        dto.setUserName(mallOrder.getNickname());
        UserInfoDTO user = this.userApi.getUserById(mallOrder.getCreateUser());
        if (Objects.nonNull(user)) {
            dto.setInvitationCode(user.getRcode());
            dto.setOrderAccount(user.getMobile());
        }
        dto.setCreateTime(dto.getOrderTime());
        dto.setSettleTime(mallOrder.getSettleTime());
        dto.setSettlementStatus(mallOrder.getSettlementStatus());
        return dto;
    }
}
