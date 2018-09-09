package com.wechat.util.qrCode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;

import com.google.zxing.common.BitMatrix;


/**
 * 根据信息生成二维码图片
 */
public class MatrixToImageWriter {
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;

	//生成一个BufferedImage类型的图片对象
	public static BufferedImage toBufferedImage(BitMatrix matrix, String logImgPath, boolean needCompress, int qrCodeSize) throws Exception{
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		if (StringUtils.isNotBlank(logImgPath))  
			QrCodeUtil.insertImage(image, logImgPath, needCompress, qrCodeSize);  //插入logo
	    return image;
	}

	// 将对象写成图片文件
	public static void writeToFile(BitMatrix matrix, String format, File file, String logImgPath, boolean needCompress, int qrCodeSize) throws Exception {
		BufferedImage image = toBufferedImage(matrix, logImgPath, needCompress, qrCodeSize);
		if (!ImageIO.write(image, format, file)) {
			throw new IOException("Could not write an image of format" + format + " to " + file);
		}
	}

	//将对象写成图片字节流
	public static void writeToStream(BitMatrix matrix, String format, OutputStream stream, String logImgPath, boolean needCompress, int qrCodeSize) throws Exception {
		BufferedImage image = toBufferedImage(matrix, logImgPath, needCompress, qrCodeSize);
		if (!ImageIO.write(image, format, stream)) {
			throw new IOException("Could not write an image of format " + format);
		}
	}
}
