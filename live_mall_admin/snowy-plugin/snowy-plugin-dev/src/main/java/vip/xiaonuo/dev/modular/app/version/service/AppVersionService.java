package vip.xiaonuo.dev.modular.app.version.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.dev.dto.LatestAppVersionDTO;
import vip.xiaonuo.dev.modular.app.version.entity.AppVersion;
import vip.xiaonuo.dev.modular.app.version.param.AppVersionAddParam;
import vip.xiaonuo.dev.modular.app.version.param.AppVersionIdParam;
import vip.xiaonuo.dev.modular.app.version.param.DevAppVersionPageParam;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/27 14:32
 */
public interface AppVersionService extends IService<AppVersion> {

    /**
     * 添加App版本
     * @param param param
     * @return 结果
     */
    boolean add(AppVersionAddParam param);

    /**
     * 编辑App版本
     * @param param param
     * @return 结果
     */
    boolean edit(AppVersionAddParam param);

    /**
     * 删除
     * @param list param
     * @return 结果
     */
    boolean delete(CommonValidList<AppVersionIdParam> list);

    /**
     * 获取详情
     * @param param param
     * @return 结果
     */
    AppVersion detail(AppVersionIdParam param);

    /**
     * 分页
     * @param param 参数
     * @return 结果
     */
    Page<AppVersion> page(DevAppVersionPageParam param);

    /**
     * 获取最新版本号
     * @return 结果
     */
    LatestAppVersionDTO getLatestAppVersion();
}
