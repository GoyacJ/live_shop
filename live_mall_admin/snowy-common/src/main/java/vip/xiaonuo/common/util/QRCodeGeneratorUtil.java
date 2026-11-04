package vip.xiaonuo.common.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Slf4j
public class QRCodeGeneratorUtil {

    public static InputStream generateQRCodePoster(String posterBase64, String qrContent, int qrCodeWidth, int qrCodeHeight) throws IOException, WriterException {
        // 解码海报的Base64字符串
        byte[] posterBytes = getBase64(posterBase64);
        BufferedImage posterImage = ImageIO.read(new ByteArrayInputStream(posterBytes));
        // 生成二维码
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, qrCodeWidth, qrCodeHeight);
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        // 将二维码绘制到海报的右下角
        Graphics2D graphics = posterImage.createGraphics();
        int x = 325;
        int y = 690;
        graphics.drawImage(qrImage, x, y, null);
        graphics.dispose();
        // 将结果转换为InputStream
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(posterImage, "png", os);
        return new ByteArrayInputStream(os.toByteArray());
    }

    public static InputStream generateQRCodePoster(String posterBase64, String qrContent, double qrCodeWidth, double qrCodeHeight, double x, double y) throws IOException, WriterException {
        // 解码海报的Base64字符串
        byte[] posterBytes = getBase64(posterBase64);
        BufferedImage posterImage = ImageIO.read(new ByteArrayInputStream(posterBytes));

        // 读取底图
        int posterWidth = posterImage.getWidth();
        int posterHeight = posterImage.getHeight();

        // 计算二维码实际像素
        int qrcodeX = (int) (x * posterWidth);
        int qrcodeY = (int) (y * posterHeight);
        int qrcodeW = (int) (qrCodeWidth * posterWidth);
        int qrcodeH = (int) (qrCodeHeight * posterHeight);

        // 生成二维码
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, qrcodeW, qrcodeH);
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        // 将二维码绘制到海报的右下角
        Graphics2D graphics = posterImage.createGraphics();
        graphics.drawImage(qrImage, qrcodeX, qrcodeY, null);
        graphics.dispose();
        // 将结果转换为InputStream
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(posterImage, "png", os);
        return new ByteArrayInputStream(os.toByteArray());
    }

    private static byte[] getBase64(String imageBase64) {
        if (imageBase64.contains("data:")) {
            int start = imageBase64.indexOf(",");
            imageBase64 = imageBase64.substring(start + 1);
        }
        final Base64.Decoder decoder = Base64.getDecoder();
        imageBase64 = imageBase64.replaceAll("\r|\n", "");
        imageBase64 = imageBase64.trim();
        return decoder.decode(imageBase64);
    }
}