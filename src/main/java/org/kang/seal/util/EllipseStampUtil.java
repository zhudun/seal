package org.kang.seal.util;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * 椭圆印章工具类
 * 基于Delphi算法实现的Java版本
 * 严格参照原始Delphi代码实现
 * 原作者：海之边 QQ-3094353627
 */
public class EllipseStampUtil {

    /**
     * 绘制专用章
     * @param corpName 公司名称
     * @param creditCode 信用代码/税号
     * @param stampText 印章文字（如"贷款专用章"）
     * @param stampNo 印章编号
     * @param scale 缩放比例
     * @param outputPath 输出路径
     * @throws IOException IO异常
     */
    public static void drawStamp(String corpName, String creditCode, String stampText, String stampNo, 
                                 float scale, String outputPath) throws IOException {
        // 创建图像
        int width = (int)(80 * scale);
        int height = (int)(80 * scale);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // 设置抗锯齿和高质量渲染 - 对应Delphi代码中的SetSmoothingMode等设置
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        // 设置背景透明 - 对应Delphi代码中的Clear背景
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, width, height);
        g2d.setComposite(AlphaComposite.SrcOver);
        
        // 设置颜色为红色 - 对应Delphi代码中的MakeColor(255, 0, 0)
        g2d.setColor(Color.RED);
        
        // 计算椭圆参数 - 对应Delphi代码中的fOffsetX等参数
        float offsetX = 5.0f * scale;
        float offsetY = 5.0f * scale;
        float ellipseWidth = 40.0f * scale;
        float ellipseHeight = 30.0f * scale;
        
        // 绘制椭圆边框 - 对应Delphi代码中的DrawEllipse
        g2d.setStroke(new BasicStroke(1.0f * scale));
        g2d.draw(new Ellipse2D.Float(offsetX + 0.5f * scale, offsetY + 0.5f * scale, 
                                     ellipseWidth - 1.0f * scale, ellipseHeight - 1.0f * scale));
        
        // 计算椭圆中心点 - 对应Delphi代码中的rPntCenter
        float centerX = offsetX + ellipseWidth / 2;
        float centerY = offsetY + ellipseHeight / 2;
        
        // 平移坐标系到椭圆中心 - 对应Delphi代码中的TranslateTransform
        g2d.translate(centerX, centerY);
        
        // 绘制公司名称 - 对应Delphi代码中的第4步
        if (corpName != null && !corpName.isEmpty()) {
            drawCompanyName(g2d, corpName, scale);
        }
        
        // 绘制中心文字（如"贷款专用章"）- 现在放在中间位置
        if (stampText != null && !stampText.isEmpty()) {
            drawCenterText(g2d, stampText, scale);
        }
        
        // 绘制信用代码/税号 - 现在放在下方位置
        if (creditCode != null && !creditCode.isEmpty()) {
            drawCreditCodeAtBottom(g2d, creditCode, scale);
        }
        
        // 绘制印章编号 - 已注释掉
//        if (stampNo != null && !stampNo.isEmpty()) {
//            drawStampNumber(g2d, stampNo, scale);
//        }
        
        // 释放资源
        g2d.dispose();
        
        // 保存图像
        File outputFile = new File(outputPath);
        ImageIO.write(image, "PNG", outputFile);
    }
    
    /**
     * 绘制信用代码/税号 - 原始位置（中间）
     * 对应Delphi代码中的第3步
     */
    private static void drawCreditCode(Graphics2D g2d, String creditCode, float scale) {
        // 设置字体 - 对应Delphi代码中的Arial字体
        Font font = new Font("Arial", Font.PLAIN, (int)(2.273f * scale));
        
        // 创建文本布局 - 对应Delphi代码中的AddString
        FontRenderContext frc = g2d.getFontRenderContext();
        TextLayout textLayout = new TextLayout(creditCode, font, frc);
        
        // 获取文本边界
        Rectangle2D bounds = textLayout.getBounds();
        
        // 创建路径 - 对应Delphi代码中的创建Path
        Shape outline = textLayout.getOutline(AffineTransform.getTranslateInstance(-bounds.getWidth() / 2, bounds.getHeight() / 2));
        
        // 应用缩放变换 - 对应Delphi代码中的scalAffine(1, 1.3121, pPnts, iPntCnt)
        AffineTransform scaleTransform = new AffineTransform();
        scaleTransform.scale(1.0, 1.3121);
        Shape transformedShape = scaleTransform.createTransformedShape(outline);
        
        // 绘制文本 - 对应Delphi代码中的FillPath
        g2d.fill(transformedShape);
    }
    
    /**
     * 绘制信用代码/税号 - 新位置（下方）
     */
    private static void drawCreditCodeAtBottom(Graphics2D g2d, String creditCode, float scale) {
        // 保存当前变换
        AffineTransform currentTransform = g2d.getTransform();
        
        // 平移坐标系原点到下方位置 - 使用原来"贷款专用章"的位置
        g2d.translate(0, 7.5f * scale);
        
        // 设置字体 - 对应Delphi代码中的Arial字体
        Font font = new Font("Arial", Font.PLAIN, (int)(2.273f * scale));
        
        // 创建文本布局 - 对应Delphi代码中的AddString
        FontRenderContext frc = g2d.getFontRenderContext();
        TextLayout textLayout = new TextLayout(creditCode, font, frc);
        
        // 获取文本边界
        Rectangle2D bounds = textLayout.getBounds();
        
        // 创建路径 - 对应Delphi代码中的创建Path
        Shape outline = textLayout.getOutline(AffineTransform.getTranslateInstance(-bounds.getWidth() / 2, bounds.getHeight() / 2));
        
        // 应用缩放变换 - 对应Delphi代码中的scalAffine(1, 1.3121, pPnts, iPntCnt)
        AffineTransform scaleTransform = new AffineTransform();
        scaleTransform.scale(1.0, 1.3121);
        Shape transformedShape = scaleTransform.createTransformedShape(outline);
        
        // 绘制文本 - 对应Delphi代码中的FillPath
        g2d.fill(transformedShape);
        
        // 恢复变换
        g2d.setTransform(currentTransform);
    }
    
   /*
     * 绘制公司名称
     * 对应Delphi代码中的第4步
    **/

    private static void drawCompanyName(Graphics2D g2d, String corpName, float scale) {
        // 设置椭圆参数 - 对应Delphi代码中的fAInter和fBInter
        float aInter = 16.4f * scale;
        float bInter = 11.4f * scale;

        // 设置角度范围 - 对应Delphi代码中的角度设置逻辑
        float startAngle, endAngle;
        int wordCount = corpName.length();

        if (wordCount >= 23) {
            startAngle = 140.0f;
            endAngle = 400.0f;
        } else if (wordCount > 20) {
            startAngle = 145.0f;
            endAngle = 390.0f;
        } else {
            startAngle = 165.0f;
            endAngle = 375.0f;
        }

        // 计算均匀分布的点 - 对应Delphi代码中的splitEllipseArc
        Point2D.Float[] points = distributePointsOnEllipseArc(aInter, bInter, startAngle, endAngle, wordCount);

        // 设置字体 - 对应Delphi代码中的'仿宋'字体
        Font font = new Font("仿宋", Font.PLAIN, (int)(3.3158f * scale));

        // 逐字绘制 - 对应Delphi代码中的逐字绘制逻辑
        for (int i = 0; i < wordCount; i++) {
            // 获取当前点
            Point2D.Float point = points[i];

            // 计算切线角度 - 对应Delphi代码中的calcEllipseTangentLineDegree
            double tangentAngle = calculateTangentAngle(aInter, bInter, point.x, point.y);

            // 保存当前变换
            AffineTransform charTransform = g2d.getTransform();

            // 平移到椭圆上的点 - 对应Delphi代码中的TranslateTransform
            g2d.translate(point.x, point.y);

            // 获取当前字符
            String character = corpName.substring(i, i + 1);

            // 创建文本布局
            FontRenderContext frc = g2d.getFontRenderContext();
            TextLayout textLayout = new TextLayout(character, font, frc);

            // 获取文本边界
            Rectangle2D bounds = textLayout.getBounds();

            // 创建路径
            Shape outline = textLayout.getOutline(AffineTransform.getTranslateInstance(-bounds.getWidth() / 2, bounds.getHeight() / 2));

            // 应用旋转变换 - 对应Delphi代码中的rotateAffine
            AffineTransform rotateTransform = new AffineTransform();
            // 注意：Delphi代码中是 fTagent * 180.0 / Pi，这里直接使用度数
            if (i == 0 ){
                rotateTransform.rotate(Math.toRadians(tangentAngle)+135.2);
            } else if (i==11) {
                rotateTransform.rotate(Math.toRadians(tangentAngle)+15.7);
            } else{
                rotateTransform.rotate(Math.toRadians(tangentAngle));
            }
            Shape transformedShape = rotateTransform.createTransformedShape(outline);

            // 绘制文本
            g2d.fill(transformedShape);

            // 恢复变换
            g2d.setTransform(charTransform);
        }
    }

    /**
     * 绘制中心文字 - 现在放在中间位置
     * 对应Delphi代码中的第5步
     */
    private static void drawCenterText(Graphics2D g2d, String centerText, float scale) {
        // 保存当前变换
        AffineTransform currentTransform = g2d.getTransform();
        
        // 设置字体 - 对应Delphi代码中的'仿宋'字体
        Font font = new Font("仿宋", Font.PLAIN, (int)(2.201f * scale));
        
        // 创建文本布局
        FontRenderContext frc = g2d.getFontRenderContext();
        TextLayout textLayout = new TextLayout(centerText, font, frc);
        
        // 获取文本边界
        Rectangle2D bounds = textLayout.getBounds();
        
        // 创建路径
        Shape outline = textLayout.getOutline(AffineTransform.getTranslateInstance(-bounds.getWidth() / 2, bounds.getHeight() / 2));
        
        // 应用缩放变换 - 对应Delphi代码中的scalAffine(1, 1.6487, pPnts, iPntCnt)
        AffineTransform scaleTransform = new AffineTransform();
        scaleTransform.scale(1.0, 1.6487);
        Shape transformedShape = scaleTransform.createTransformedShape(outline);
        
        // 绘制文本
        g2d.fill(transformedShape);
        
        // 恢复变换
        g2d.setTransform(currentTransform);
    }
    
    /**
     * 绘制印章编号
     * 对应Delphi代码中的第6步
     */
    private static void drawStampNumber(Graphics2D g2d, String stampNo, float scale) {
        // 保存当前变换
        AffineTransform currentTransform = g2d.getTransform();
        
        // 平移坐标系原点 - 对应Delphi代码中的TranslateTransform(0, 11.1)
        g2d.translate(0, 11.1f * scale);
        
        // 格式化印章编号 - 对应Delphi代码中的Format('(%s)', [stampNo])
        String formattedStampNo = "(" + stampNo + ")";
        
        // 设置字体 - 对应Delphi代码中的Arial字体
        Font font = new Font("Arial", Font.PLAIN, (int)(1.873f * scale));
        
        // 创建文本布局
        FontRenderContext frc = g2d.getFontRenderContext();
        TextLayout textLayout = new TextLayout(formattedStampNo, font, frc);
        
        // 获取文本边界
        Rectangle2D bounds = textLayout.getBounds();
        
        // 创建路径
        Shape outline = textLayout.getOutline(AffineTransform.getTranslateInstance(-bounds.getWidth() / 2, bounds.getHeight() / 2));
        
        // 应用缩放变换 - 对应Delphi代码中的scalAffine(1, 1.9442, pPnts, iPntCnt)
        AffineTransform scaleTransform = new AffineTransform();
        scaleTransform.scale(1.0, 1.9442);
        Shape transformedShape = scaleTransform.createTransformedShape(outline);
        
        // 绘制文本
        g2d.fill(transformedShape);
        
        // 恢复变换
        g2d.setTransform(currentTransform);
    }
    
    /**
     * 计算椭圆上的点坐标
     * 对应Delphi代码中的getEllipsePoint函数
     */
    private static Point2D.Float getEllipsePoint(float a, float b, double angle) {
        double radians = Math.toRadians(angle);
        float x = (float) (a * Math.cos(radians));
        float y = (float) (b * Math.sin(radians));
        return new Point2D.Float(x, y);
    }
    
    /**
     * 计算椭圆上点的切线角度
     * 对应Delphi代码中的calcEllipseTangentLineDegree函数
     */
    private static double calculateTangentAngle(float a, float b, float x, float y) {
        // 椭圆上点(x,y)处的切线斜率为 -b²x/a²y
        if (Math.abs(y) < 0.0001) { // 防止除以零
            return (x > 0) ? 90.0 : -90.0;
        }
        double slope = -Math.pow(b, 2) * x / (Math.pow(a, 2) * y);
        return Math.toDegrees(Math.atan(slope)); // 转换为度
    }
    
    /**
     * 计算椭圆弧上均匀分布的点
     * 对应Delphi代码中的splitEllipseArc函数
     */
    private static Point2D.Float[] distributePointsOnEllipseArc(float a, float b, float startAngle, float endAngle, int count) {
        Point2D.Float[] points = new Point2D.Float[count];
        
        float angleStep = (endAngle - startAngle) / (count - 1);
        
        for (int i = 0; i < count; i++) {
            float angle = startAngle + i * angleStep;
            points[i] = getEllipsePoint(a, b, angle);
        }
        
        return points;
    }
} 