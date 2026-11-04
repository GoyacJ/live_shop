package vip.xiaonuo.dev.modular.app.version.service.impl;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.dev.core.enums.AppType;
import vip.xiaonuo.dev.dto.LatestAppVersionDTO;
import vip.xiaonuo.dev.modular.app.version.entity.AppVersion;
import vip.xiaonuo.dev.modular.app.version.mapper.mapping.AppVersionMapper;
import vip.xiaonuo.dev.modular.app.version.param.AppVersionAddParam;
import vip.xiaonuo.dev.modular.app.version.param.AppVersionIdParam;
import vip.xiaonuo.dev.modular.app.version.param.DevAppVersionPageParam;
import vip.xiaonuo.dev.modular.app.version.service.AppVersionService;
import vip.xiaonuo.dev.modular.dict.entity.DevDict;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/27 14:32
 */
@Component
@Slf4j
public class AppVersionServiceImpl extends ServiceImpl<AppVersionMapper, AppVersion> implements AppVersionService {

    @Override
    public boolean add(AppVersionAddParam param) {
        return this.save(param.toAppVersion());
    }

    @Override
    public boolean edit(AppVersionAddParam param) {
        return UpdateChain.of(AppVersion.class)
                .set(AppVersion::getVersionName, param.versionName())
                .set(AppVersion::getVersionCode, param.versionCode())
                .set(AppVersion::getAppType, param.appType())
                .set(AppVersion::getAppUrl, param.appUrl())
                .set(AppVersion::getAppDesc, param.appDesc())
                .set(AppVersion::getForceUpdate, param.forceUpdate())
                .set(AppVersion::getSortCode, param.sortCode())
                .where(AppVersion::getId).eq(param.id())
                .update();
    }

    @Override
    public boolean delete(CommonValidList<AppVersionIdParam> list) {
        return this.removeByIds(CollStreamUtil.toList(list, AppVersionIdParam::id));
    }

    @Override
    public AppVersion detail(AppVersionIdParam param) {
        return this.getById(param.id());
    }

    @Override
    public Page<AppVersion> page(DevAppVersionPageParam param) {

        QueryWrapper queryWrapper = new QueryWrapper();
        if (ObjectUtil.isAllNotEmpty(param.sortField(), param.sortOrder())) {
            CommonSortOrderEnum.validate(param.sortOrder());
            queryWrapper.orderBy(CharSequenceUtil.toUnderlineCase(param.sortField()), param.sortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(DevDict::getSortCode);
        }

        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public LatestAppVersionDTO getLatestAppVersion() {

        QueryWrapper query = new QueryWrapper();

        // 先按 appType 排序（分组时方便）
        query.orderBy(AppVersion::getAppType, true);

        // 再按 sortCode 降序（取最新）
        query.orderBy(AppVersion::getSortCode, false);
        List<AppVersion> allVersions = this.list(query);

        // 分组，只保留每种appType下 sortCode 最大的第一条
        Map<AppType, AppVersion> latestVersionMap = new LinkedHashMap<>();
        for (AppVersion version : allVersions) {
            latestVersionMap.putIfAbsent(version.getAppType(), version);
        }

        LatestAppVersionDTO.AppVersionInfo ios = null;
        LatestAppVersionDTO.AppVersionInfo android = null;

        // 组装返回结果
        for (Map.Entry<AppType, AppVersion> entry : latestVersionMap.entrySet()) {
            AppType key = entry.getKey();
            switch (key) {
                case iOS -> {
                    AppVersion version = entry.getValue();
                    ios = new LatestAppVersionDTO.AppVersionInfo(
                            entry.getKey().name(),
                            version.getVersionName(),
                            version.getVersionCode(),
                            version.getAppUrl(),
                            version.getAppDesc()
                    );
                }
                case Android -> {
                    AppVersion version = entry.getValue();
                    android = new LatestAppVersionDTO.AppVersionInfo(
                            entry.getKey().name(),
                            version.getVersionName(),
                            version.getVersionCode(),
                            version.getAppUrl(),
                            version.getAppDesc()
                    );
                }
            }
        }
        return new LatestAppVersionDTO(android, ios);
    }
}
