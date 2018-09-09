package com.wechat.util.qrCode;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QrCodeUtil {
	private static Logger logger = LoggerFactory.getLogger(QrCodeUtil.class);
	
	/**
	 * 生成一个BufferedImage类型的图片对象
	 * @param contents 内容
	 * @param qrCodeSize 二维码大小
	 * @param logImgPath log地址
	 * @param needCompress 是否压缩log
	 * @return
	 * @throws Exception
	 */
	public static BufferedImage toBufferedImage(String contents, int qrCodeSize, String logImgPath, boolean needCompress) throws Exception{
		BitMatrix bitMatrix = getBitMatrix(contents, qrCodeSize, qrCodeSize);
		return MatrixToImageWriter.toBufferedImage(bitMatrix, logImgPath, needCompress, qrCodeSize);
	}
	
	/**
	 * 将对象写成图片文件
	 * @param contents 内容
	 * @param format 格式：如 "png"
	 * @param file 
	 * @param qrCodeSize 二维码大小
	 * @param logImgPath log地址
	 * @param needCompress 是否压缩log
	 * @throws Exception
	 */
	public static void writeToFile(String contents, String format, File file,int qrCodeSize, String logImgPath, boolean needCompress) throws Exception{
		BitMatrix bitMatrix = getBitMatrix(contents, qrCodeSize, qrCodeSize);
		MatrixToImageWriter.writeToFile(bitMatrix, format, file, logImgPath, needCompress, qrCodeSize);
	}
	
	/**
	 * 将对象写成图片字节流
	 * @param contents 内容
	 * @param format 如：png
	 * @param stream 输出流
	 * @param qrCodeSize 二维码大小
	 * @param logImgPath log路径
	 * @param needCompress 是否压缩log
	 * @throws Exception
	 */
	public static void writeToStream(String contents, String format, OutputStream stream, int qrCodeSize, String logImgPath, boolean needCompress) throws Exception {
		BitMatrix bitMatrix = getBitMatrix(contents, qrCodeSize, qrCodeSize);
		MatrixToImageWriter.writeToStream(bitMatrix, format, stream, logImgPath, needCompress, qrCodeSize);
	}
	
	public static Result getResult(File file) throws Exception{
		MultiFormatReader formatReader = new MultiFormatReader();
		BufferedImage image = ImageIO.read(file);;
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		Binarizer binarizer = new HybridBinarizer(source);
		BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
		Map<DecodeHintType,String> hints = new HashMap<DecodeHintType,String>();
		hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
		return formatReader.decode(binaryBitmap,hints);
	}
	
	private static BitMatrix getBitMatrix(String contents, int width, int height) throws WriterException{
		 MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		 Map<EncodeHintType,Object> hints = new HashMap<EncodeHintType,Object>();
		//二维码的格式信息
	     hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  
	     hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");  
	     hints.put(EncodeHintType.MARGIN, 1);  
		 //生成二维码图片的对象
		 BitMatrix bitMatrix = multiFormatWriter.encode(contents, BarcodeFormat.QR_CODE, width, height,hints);
		 return bitMatrix;
	}

	/** 
     * 插入LOGO 
     *  
     * @param source 
     *            二维码图片 
     * @param imgPath 
     *            LOGO图片地址 
     * @param needCompress 
     *            是否压缩 
     * @param qrCodeSize
     * 			二维码大小
     * @throws Exception 
     */  
	static void insertImage(BufferedImage source, String imgPath,  
            boolean needCompress, int qrCodeSize) throws Exception {  
        File file = new File(imgPath); 
        if (!file.exists()) {  
            logger.error("QrCodeUtil-该logo文件不存在！{}", imgPath);
            return;  
        }  
        Image src = ImageIO.read(file);  
        int width = src.getWidth(null);  
        int height = src.getHeight(null); 
        // LOGO宽度  
	    int WIDTH = qrCodeSize/5;  
	    // LOGO高度  
	    int HEIGHT = qrCodeSize/5; 
	    //弧度
	    int arc = 20;
        if (needCompress) { // 压缩LOGO  
            width = WIDTH;  
            height = HEIGHT;  
            Image image = src.getScaledInstance(width, height,  
                    Image.SCALE_SMOOTH);  
            BufferedImage tag = new BufferedImage(width, height,  
                    BufferedImage.TYPE_INT_RGB);  
            Graphics g = tag.getGraphics();  
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图  
            g.dispose();  
            src = image;  
        }  
        // 插入LOGO  
        Graphics2D graph = source.createGraphics();  
        Map<Key, Object> mapH = new HashMap<Key, Object>();
        mapH.put(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);  //抗锯齿 （抗锯齿总开关）   
        mapH.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graph.setRenderingHints(mapH);
        int x = (qrCodeSize - width) / 2;  
        int y = (qrCodeSize - height) / 2;  
        graph.drawImage(src, x, y, width, height, null);  
        Shape shape = new RoundRectangle2D.Double(x, y, width, height, arc, arc);
        graph.setStroke(new BasicStroke(5f));
        graph.draw(shape);
        Shape shape1 = new RoundRectangle2D.Double(x-2, y-2, width+4, height+4, arc, arc);
        graph.setStroke(new BasicStroke(1f));
        graph.setColor(Color.GRAY);
        graph.draw(shape1);  
        graph.dispose();  
    }  
	
}
