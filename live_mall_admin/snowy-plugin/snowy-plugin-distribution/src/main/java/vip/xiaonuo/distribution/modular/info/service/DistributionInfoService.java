package vip.xiaonuo.distribution.modular.info.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.common.pojo.dto.CommonIdParam;
import vip.xiaonuo.common.pojo.dto.CommonPageParam;
import vip.xiaonuo.distribution.api.enums.DistributionType;
import vip.xiaonuo.distribution.modular.info.entity.DistributionInfo;
import vip.xiaonuo.distribution.modular.info.params.DistributionInfoAddParam;
import vip.xiaonuo.distribution.modular.info.params.DistributionInfoEditParam;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/30 10:56
 */
public interface DistributionInfoService extends IService<DistributionInfo> {

    /**
     * 分页
     * @param param 参数
     * @return 结果
     */
    Page<DistributionInfo> page(CommonPageParam param);

    /**
     * 详情
     * @param id id
     * @return 结果
     */
    DistributionInfo detail(String id);

    /**
     * 新增
     * @param param 参数
     */
    void add(DistributionInfoAddParam param);

    /**
     * 编辑
     * @param param 参数
     */
    void edit(DistributionInfoEditParam param);

    /**
     * 删除
     * @param param 参数
     */
    void delete(CommonValidList<CommonIdParam> param);

    /**
     * 更改状态
     * @param id id
     * @param status 状态
     */
    void changeStatus(String id, SimpleStatus status);

    /**
     * 通过推广类型获取推广信息
     * @param type 推广类型
     * @return 推广信息
     */
    DistributionInfo getDistributionInfoByType(DistributionType type);
}
