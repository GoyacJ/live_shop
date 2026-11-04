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
package vip.xiaonuo.common.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.URLUtil;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.core.handle.Context;

import java.io.File;
import java.io.IOException;

/**
 * 文件下载工具类，使用本类前，对参数校验的异常使用CommonResponseUtil.renderError()方法进行渲染
 *
 * @author xuyuxiang
 * @date 2020/8/5 21:45
 */
@Slf4j
public class CommonDownloadUtil {

    /**
     * 下载文件
     *
     * @param file 要下载的文件
     * @param ctx  请求上下文
     * @author xuyuxiang
     * @date 2020/8/5 21:46
     */
    public static void download(File file, Context ctx) {
        download(file.getName(), FileUtil.readBytes(file), ctx);
    }

    /**
     * 下载文件
     *
     * @author xuyuxiang
     * @date 2022/7/31 10:57
     */
    public static void download(String fileName, byte[] fileBytes, Context ctx) {
        try {
            ctx.headerSet("Content-Disposition", "attachment;filename=" + URLUtil.encode(fileName));
            ctx.headerSet("Content-Length", "" + fileBytes.length);
            ctx.headerSet("Access-Control-Allow-Origin", "*");
            ctx.headerSet("Access-Control-Expose-Headers", "Content-Disposition");
            ctx.contentType("application/octet-stream;charset=UTF-8");
            IoUtil.write(ctx.outputStream(), true, fileBytes);
        } catch (IOException e) {
            log.error(">>> 文件下载异常：", e);
        }
    }
}
