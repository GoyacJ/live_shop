package vip.xiaonuo.mobile.modular.integral.service.impl;

import com.google.zxing.WriterException;
import com.mybatisflex.core.paginate.Page;
import lombok.extern.slf4j.Slf4j;
import manage.UserApi;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.UploadedFile;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.util.QRCodeGeneratorUtil;
import vip.xiaonuo.dev.api.DevConfigApi;
import vip.xiaonuo.dev.api.DevFileApi;
import vip.xiaonuo.integral.api.IntegralApi;
import vip.xiaonuo.integral.api.dto.UserIntegralChangePageDTO;
import vip.xiaonuo.manage.modular.userbalancelog.param.UserIntegralChangePageParam;
import vip.xiaonuo.mobile.modular.integral.dto.SharingLinksDTO;
import vip.xiaonuo.mobile.modular.integral.service.IntegralService;
import vip.xiaonuo.mobile.modular.manage.result.UserIntegralChangeVO;
import vip.xiaonuo.task.api.TaskApi;
import vip.xiaonuo.task.api.enums.TaskBusinessCategory;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/28 19:04
 */
@Slf4j
@Component
public class IntegralServiceImpl implements IntegralService {

    @Inject
    private UserApi userApi;
    @Inject
    private IntegralApi integralApi;

    @Inject
    private TaskApi taskApi;

    @Inject
    private DevConfigApi devConfigApi;

    @Inject("devFileApi")
    private DevFileApi devFileApi;

    @Inject("${appQr.width}")
    private int codeWidth;
    @Inject("${appQr.height}")
    private int codeHeight;
    @Inject("${liveQr.liveCodeContent}")
    private String live_codeContent;

    @Override
    public String sharingLinks(SharingLinksDTO dto) {
        String sharingLinks;
        TaskBusinessCategory taskCategory = TaskBusinessCategory.getByCode(dto.category());

        if (TaskBusinessCategory.LIVE_SHARE.equals(taskCategory)) {
            String liveLogo = devConfigApi.getValueByKey("LIVE_POSTER");

            UploadedFile file = null;
            if (!liveLogo.isEmpty()) {
                try {
                    InputStream inputStream = QRCodeGeneratorUtil.generateQRCodePoster(liveLogo, live_codeContent, codeWidth, codeHeight);
                    file = new UploadedFile("image/png", inputStream, "live.png");
                } catch (IOException | WriterException e) {
                    throw new RuntimeException(e);
                }
            }
            sharingLinks = devFileApi.storageFileWithReturnUrlMinio(file);

        } else {

            sharingLinks = userApi.sharingLinks(dto.userId());
        }

        // 积分任务
        taskApi.task(dto.userId(), dto.businessId(), taskCategory);

        return sharingLinks;
    }

    @Override
    public Page<UserIntegralChangeVO> page(UserIntegralChangePageParam param) {
        return integralApi.userIntegralChangePage(new UserIntegralChangePageDTO(
                param.getIntegralChangeType(),
                param.getIntegralSource(),
                param.getIntegralCategory(),
                param.getUserId(),
                CommonSortOrderEnum.getByValue(param.getSortOrder()),
                param.getCurrent(),
                param.getSize()
        )).map(res -> {
            UserIntegralChangeVO vo = new UserIntegralChangeVO();
            vo.setUserId(res.getUserId());
            vo.setIntegralId(res.getIntegralId());
            vo.setIntegralRecordId(res.getIntegralRecordId());
            vo.setIntegralSource(res.getIntegralSource().getName());
            vo.setIntegralSourceId(res.getIntegralSourceId());
            vo.setChangeType(res.getChangeType());
            vo.setIntegralCategory(res.getIntegralCategory().getName());
            vo.setOperationTime(res.getOperationTime());
            vo.setIntegral(res.getIntegral());
            vo.setOperationSource(res.getOperationSource().getName());
            vo.setOperationUserId(res.getOperationUserId());
            vo.setIntegralBalance(res.getIntegralBalance());
            return vo;
        });
    }
}
