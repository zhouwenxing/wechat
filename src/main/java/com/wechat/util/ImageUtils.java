package com.wechat.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.junit.Test;

public class ImageUtils {
	/**
     * 
     * @Title: 构造图片
     * @Description: 生成水印并返回java.awt.image.BufferedImage
     * @param file - 源文件(图片)
     * @param waterFile - 水印文件(图片)
     * @param x - 距离右下角的X偏移量
     * @param y - 距离右下角的Y偏移量
     * @param alpha - 透明度, 选择值从0.0~1.0: 完全透明~完全不透明
     * @return BufferedImage
     * @throws IOException
     */
    public static BufferedImage watermark(File file, File waterFile, int x, int y, float alpha) throws IOException {
        // 获取底图
        BufferedImage buffImg = ImageIO.read(file);
        // 获取层图
        BufferedImage waterImg = ImageIO.read(waterFile);
        // 创建Graphics2D对象，用在底图对象上绘图
        Graphics2D g2d = buffImg.createGraphics();
        int waterImgWidth = waterImg.getWidth();// 获取层图的宽度
        int waterImgHeight = waterImg.getHeight();// 获取层图的高度
        // 在图形和图像中实现混合和透明效果
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
        // 绘制
        g2d.drawImage(waterImg, x, y, waterImgWidth, waterImgHeight, null);
        g2d.dispose();// 释放图形上下文使用的系统资源
        return buffImg;
    }

    /**
     * 输出水印图片
     * @param buffImg - 图像加水印之后的BufferedImage对象
     * @param savePath - 图像加水印之后的保存路径
     */
    public void generateWaterFile(BufferedImage buffImg, String savePath) {
        int temp = savePath.lastIndexOf(".") + 1;
        try {
            ImageIO.write(buffImg, savePath.substring(temp), new File(savePath));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    
    /** 
     * 采用指定宽度、高度或压缩比例 的方式对图片进行压缩 
     * @param imgsrc 源图片地址 
     * @param imgdist 目标图片地址 
     * @param widthdist 压缩后图片宽度（当rate==null时，必传） 
     * @param heightdist 压缩后图片高度（当rate==null时，必传） 
     * @param rate 压缩比例  
     */  
//    public static void reduceImg(String imgsrc, String imgdist, int widthdist,  
//            int heightdist, Float rate) {  
//        try {  
//            File srcfile = new File(imgsrc);  
//            // 检查文件是否存在  
//            if (!srcfile.exists()) {  
//                return;  
//            }  
//            // 如果rate不为空说明是按比例压缩  
//            if (rate != null && rate > 0) {  
//                // 获取文件高度和宽度  
//                int[] results = getImgWidth(srcfile);  
//                if (results == null || results[0] == 0 || results[1] == 0) {  
//                    return;  
//                } else {  
//                    widthdist = (int) (results[0] * rate);  
//                    heightdist = (int) (results[1] * rate);  
//                }  
//            }  
//            // 开始读取文件并进行压缩  
//            Image src = javax.imageio.ImageIO.read(srcfile);  
//            BufferedImage tag = new BufferedImage((int) widthdist,  
//                    (int) heightdist, BufferedImage.TYPE_INT_RGB);  
//  
//            tag.getGraphics().drawImage(  
//                    src.getScaledInstance(widthdist, heightdist,  
//                            Image.SCALE_SMOOTH), 0, 0, null);  
//  
//            FileOutputStream out = new FileOutputStream(imgdist);  
//            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);  
//            encoder.encode(tag);  
//            out.close();  
//  
//        } catch (IOException ex) {  
//            ex.printStackTrace();  
//        }  
//    }
    
    /** 
     * 获取图片宽度 
     * @param file - 图片文件 
     * @return 宽度 
     */  
    public static int[] getImgWidth(File file) {  
        InputStream is = null;  
        BufferedImage src = null;  
        int result[] = { 0, 0 };  
        try {  
            is = new FileInputStream(file);  
            src = javax.imageio.ImageIO.read(is);  
            result[0] = src.getWidth(null); // 得到源图宽  
            result[1] = src.getHeight(null); // 得到源图高  
            is.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return result;  
    }  

   @Test
    public void test() throws IOException {
        String sourceFilePath = "D:/test/changdu.png";
        String waterFilePath = "D:/test/code1.png";
        String sourceFilePath1 = "D:/test/code.png";
        ImageHelper.scaleImageWithParams(sourceFilePath1, waterFilePath, 190, 190, false, "png");
        String saveFilePath = "D:/test/new.png";
        ImageUtils newImageUtils = new ImageUtils();
        // 构建叠加层
        BufferedImage buffImg = ImageUtils.watermark(new File(sourceFilePath), new File(waterFilePath), 280, 950, 1.0f);
        // 输出水印图片
        newImageUtils.generateWaterFile(buffImg, saveFilePath);
    }

}
