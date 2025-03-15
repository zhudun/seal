package org.kang.seal;

import org.kang.config.SealCircle;
import org.kang.config.SealConfiguration;
import org.kang.config.SealFont;
import org.kang.util.SealUtil;
import org.kang.seal.util.EllipseStampUtil;

import java.awt.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws Exception {
        // 1. 使用原来的方法生成印章
        // generateOriginalSeal();
        
        // 2. 使用新的椭圆印章算法生成印章
        generateEllipseSeal();
    }
    
    /**
     * 使用原来的方法生成印章
     */
    private static void generateOriginalSeal() throws Exception {
        /**
         * 印章配置文件
         */
        SealConfiguration configuration = new SealConfiguration();

        /**
         * 主文字
         */
        SealFont mainFont = new SealFont();
        mainFont.setBold(true);
        // 使用与中心文字相同的字体
        mainFont.setFontFamily("宋体");
        // 增加边距，使文字更清晰
        mainFont.setMarginSize(20);
        // 修改为图片中的环绕文字
        mainFont.setFontText("中信百信银行股份有限公司");
        // 增加字体大小，使其更清晰
        mainFont.setFontSize(28);
        // 增加字间距，使文字分布更均匀，更清晰
        mainFont.setFontSpace(14.0);

        /**
         * 副文字（底部数字）
         */
        SealFont viceFont = new SealFont();
        viceFont.setBold(true);
        viceFont.setFontFamily("宋体");
        // 增加边距，使数字更清晰
        viceFont.setMarginSize(10);
        // 添加底部数字序列
        viceFont.setFontText("5001080489655");
        // 增加字体大小，使数字更清晰
        viceFont.setFontSize(18);
        // 增加字间距，使数字分布更均匀，更清晰
        viceFont.setFontSpace(10.0);

        /**
         * 中心文字
         */
        SealFont centerFont = new SealFont();
        centerFont.setBold(true);
        centerFont.setFontFamily("宋体");
        // 使用中心文字
        centerFont.setFontText("贷款专用章");
        centerFont.setFontSize(35);

        /**
         * 添加主文字
         */
        configuration.setMainFont(mainFont);
        /**
         * 添加副文字
         */
        configuration.setViceFont(viceFont);
        /**
         * 添加中心文字
         */
        configuration.setCenterFont(centerFont);

        /**
         * 图片大小 - 增加图片大小以提高清晰度
         */
        configuration.setImageSize(400);
        /**
         * 背景颜色
         */
        configuration.setBackgroudColor(Color.RED);
        /**
         * 边线粗细、半径
         * 调整椭圆形状，使文字不变形但保持环绕效果
         * 增加高度值，减少宽高差异，减轻文字变形
         * 增加整体尺寸以提高清晰度
         */
        configuration.setBorderCircle(new SealCircle(3, 180, 160));
        /**
         * 内边线粗细、半径
         */
        configuration.setBorderInnerCircle(new SealCircle(2, 175, 155));
        /**
         * 内环线粗细、半径 - 移除内环线，使其更接近图片样式
         */
        // configuration.setInnerCircle(new SealCircle(2, 85, 45));

        //1.生成公章
        try {
            // 修改保存路径为您的本地路径
            SealUtil.buildAndStoreSeal(configuration, "C:\\Users\\23101\\Pictures\\公章_原始算法.png");
            System.out.println("原始算法印章生成成功，请查看：C:\\Users\\23101\\Pictures\\公章_原始算法.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 使用新的椭圆印章算法生成印章
     */
    private static void generateEllipseSeal() throws Exception {
        try {
            // 生成标准椭圆印章
            generateStandardEllipseSeal();
            
            // 生成与图片相似的印章
            generateSimilarToImageSeal();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 生成标准椭圆印章
     */
    private static void generateStandardEllipseSeal() throws IOException {
        // 公司名称
        String corpName = "中信百信银行股份有限公司";
        
        // 信用代码/税号
        String creditCode = "5001080489655";
        
        // 印章文字
        String stampText = "贷款专用章";
        
        // 印章编号
        String stampNo = "1";
        
        // 缩放比例 - 增大比例使印章更清晰
        float scale = 6.0f;
        
        // 输出路径
        String outputPath = "C:\\Users\\23101\\Pictures\\公章_椭圆标准.png";
        
        // 生成印章
        EllipseStampUtil.drawStamp(corpName, creditCode, stampText, stampNo, scale, outputPath);
        
        System.out.println("标准椭圆印章生成成功，请查看：" + outputPath);
    }
    
    /**
     * 生成与图片相似的印章
     */
    private static void generateSimilarToImageSeal() throws IOException {
        // 公司名称
        String corpName = "吉林省消费金融有限公司";
        
        // 信用代码/税号 - 使用数字序列
        String creditCode = "5001080489655";
        
        // 印章文字
        String stampText = "贷款专用章";
        
        // 印章编号
        String stampNo = "1";
        
        // 缩放比例 - 增大比例使印章更清晰
        float scale = 6.0f;
        
        // 输出路径
        String outputPath = "C:\\Users\\23101\\Pictures\\公章_类似图片.png";
        
        // 生成印章
        EllipseStampUtil.drawStamp(corpName, creditCode, stampText, stampNo, scale, outputPath);
        
        System.out.println("类似图片的印章生成成功，请查看：" + outputPath);
    }
}
