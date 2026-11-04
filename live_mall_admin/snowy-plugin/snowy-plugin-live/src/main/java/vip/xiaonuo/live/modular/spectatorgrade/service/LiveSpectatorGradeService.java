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
package vip.xiaonuo.live.modular.spectatorgrade.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import vip.xiaonuo.live.modular.spectatorgrade.entity.LiveSpectatorGrade;
import vip.xiaonuo.live.modular.spectatorgrade.param.LiveSpectatorGradeAddParam;
import vip.xiaonuo.live.modular.spectatorgrade.param.LiveSpectatorGradeEditParam;
import vip.xiaonuo.live.modular.spectatorgrade.param.LiveSpectatorGradeIdParam;
import vip.xiaonuo.live.modular.spectatorgrade.param.LiveSpectatorGradePageParam;

import java.util.List;

/**
 * 直播观众成长表Service接口
 *
 * @author GMQ
 * @date  2025/04/06 13:06
 **/
public interface LiveSpectatorGradeService extends IService<LiveSpectatorGrade> {

    /**
     * 获取直播观众成长表分页
     *
     * @author GMQ
     * @date  2025/04/06 13:06
     */
    Page<LiveSpectatorGrade> page(LiveSpectatorGradePageParam liveSpectatorGradePageParam);

    /**
     * 添加直播观众成长表
     *
     * @author GMQ
     * @date  2025/04/06 13:06
     */
    void add(LiveSpectatorGradeAddParam liveSpectatorGradeAddParam);

    /**
     * 编辑直播观众成长表
     *
     * @author GMQ
     * @date  2025/04/06 13:06
     */
    void edit(LiveSpectatorGradeEditParam liveSpectatorGradeEditParam);

    /**
     * 删除直播观众成长表
     *
     * @author GMQ
     * @date  2025/04/06 13:06
     */
    void delete(List<LiveSpectatorGradeIdParam> liveSpectatorGradeIdParamList);

    /**
     * 获取直播观众成长表详情
     *
     * @author GMQ
     * @date  2025/04/06 13:06
     */
    LiveSpectatorGrade detail(LiveSpectatorGradeIdParam liveSpectatorGradeIdParam);

    /**
     * 获取直播观众成长表详情
     *
     * @author GMQ
     * @date  2025/04/06 13:06
     **/
    LiveSpectatorGrade queryEntity(String id);
}
