package com.ucsmy.collection.util;

/**
 * Created by Administrator on 2015/12/11.
 */

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.ColorConvertOp;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

public class ClearImageHelper {

    public static void main(String[] args) throws IOException {


        File testDataDir = new File("E:\\workspace\\spider.application\\spider.crawler\\target\\test-classes\\20160429");
        final String destDir = testDataDir.getAbsolutePath() + "/tmp2";
        for (File file : testDataDir.listFiles()) {
            cleanImage(file, destDir);
        }

    }

    /**
     *
     * @param sfile
     *            需要去噪的图像
     * @param destDir
     *            去噪后的图像保存地址
     * @throws IOException
     */
    public static void cleanImage(File sfile, String destDir)
            throws IOException
    {
        File destF = new File(destDir);
        if (!destF.exists())
        {
            destF.mkdirs();
        }

        BufferedImage bufferedImage = ImageIO.read(sfile);
        int h = bufferedImage.getHeight();
        int w = bufferedImage.getWidth();

        // 灰度化
//        int[][] gray = new int[w][h];
//        for (int x = 0; x < w; x++)
//        {
//            for (int y = 0; y < h; y++)
//            {
//                int argb = bufferedImage.getRGB(x, y);
//                // 图像加亮（调整亮度识别率非常高）
//                int r = (int) (((argb >> 16) & 0xFF) * 1.1 + 30);
//                int g = (int) (((argb >> 8) & 0xFF) * 1.1 + 30);
//                int b = (int) (((argb >> 0) & 0xFF) * 1.1 + 30);
//                if (r >= 255)
//                {
//                    r = 255;
//                }
//                if (g >= 255)
//                {
//                    g = 255;
//                }
//                if (b >= 255)
//                {
//                    b = 255;
//                }
//                gray[x][y] = (int) Math
//                        .pow((Math.pow(r, 2.2) * 0.2973 + Math.pow(g, 2.2)
//                                * 0.6274 + Math.pow(b, 2.2) * 0.0753), 1 / 2.2);
//            }
//        }

        // 二值化
//        int threshold = ostu(gray, w, h);
//        BufferedImage binaryBufferedImage = new BufferedImage(w, h,
//                BufferedImage.TYPE_BYTE_BINARY);
//        for (int x = 0; x < w; x++)
//        {
//            for (int y = 0; y < h; y++)
//            {
//                if (gray[x][y] > threshold)
//                {
//                    gray[x][y] |= 0x00FFFF;
//                } else
//                {
//                    gray[x][y] &= 0xFF0000;
//                }
//                binaryBufferedImage.setRGB(x, y, gray[x][y]);
//            }
//        }

//        bufferedImage = getGrayPicture(bufferedImage);
        // 矩阵打印
        for (int y = 0; y < h; y++)
        {
            for (int x = 0; x < w; x++)
            {
                System.out.println(bufferedImage.getRGB(x,y));
//                if (isBlack(bufferedImage.getRGB(x, y)))
//                {
//                    System.out.print("*");
//                } else
//                {
//                    System.out.print(" ");
//                }
            }
            System.out.println();
        }

        ImageIO.write(bufferedImage, "png", new File(destDir, sfile
                .getName()));
    }

    public static void convert(String path) {
        // TODO Auto-generated constructor stub
        try {
            BufferedImage image = ImageIO.read(new File(path));
            ImageIcon imageIcon = new ImageIcon(image);
            BufferedImage bufferedImage = new BufferedImage(
                    imageIcon.getIconWidth(), imageIcon.getIconHeight(),
                    BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
            g2D.drawImage(imageIcon.getImage(), 0, 0,
                    imageIcon.getImageObserver());
            int alpha = 0;
            for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage
                    .getHeight(); j1++) {
                for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage
                        .getWidth(); j2++) {
                    int rgb = bufferedImage.getRGB(j2, j1);
                    if (colorInRange(rgb))
                        alpha = 0;
                    else
                        alpha = 255;
                    rgb = (alpha << 24) | (rgb & 0x00ffffff);
                    bufferedImage.setRGB(j2, j1, rgb);
                }
            }
            g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
            // 生成图片为PNG
            String outFile = path.substring(0, path.lastIndexOf("."));
            ImageIO.write(bufferedImage, "png", new File(outFile + ".png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static int color_range = 210;

    public static boolean colorInRange(int color) {
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);
        if (red >= color_range && green >= color_range && blue >= color_range)
            return true;
        return false;
    }
    public static BufferedImage getGrayPicture(BufferedImage originalImage)
    {
        BufferedImage grayPicture;
        int imageWidth = originalImage.getWidth();
        int imageHeight = originalImage.getHeight();

        grayPicture = new BufferedImage(imageWidth, imageHeight,
                BufferedImage.TYPE_3BYTE_BGR);
        ColorConvertOp cco = new ColorConvertOp(ColorSpace
                .getInstance(ColorSpace.CS_GRAY), null);
        cco.filter(originalImage, grayPicture);
        return grayPicture;
    }
    public static boolean isBlack(int colorInt)
    {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() <=300)
        {
            return true;
        }
        return false;
    }

    public static boolean isWhite(int colorInt)
    {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() > 300)
        {
            return true;
        }
        return false;
    }

    public static int isBlackOrWhite(int colorInt)
    {
        if (getColorBright(colorInt) < 30 || getColorBright(colorInt) > 730)
        {
            return 1;
        }
        return 0;
    }

    public static int getColorBright(int colorInt)
    {
        Color color = new Color(colorInt);
        return color.getRed() + color.getGreen() + color.getBlue();
    }

    public static int ostu(int[][] gray, int w, int h)
    {
        int[] histData = new int[w * h];
        // Calculate histogram
        for (int x = 0; x < w; x++)
        {
            for (int y = 0; y < h; y++)
            {
                int red = 0xFF & gray[x][y];
                histData[red]++;
            }
        }

        // Total number of pixels
        int total = w * h;

        float sum = 0;
        for (int t = 0; t < 256; t++)
            sum += t * histData[t];

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int t = 0; t < 256; t++)
        {
            wB += histData[t]; // Weight Background
            if (wB == 0)
                continue;

            wF = total - wB; // Weight Foreground
            if (wF == 0)
                break;

            sumB += (float) (t * histData[t]);

            float mB = sumB / wB; // Mean Background
            float mF = (sum - sumB) / wF; // Mean Foreground

            // Calculate Between Class Variance
            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            // Check if new maximum found
            if (varBetween > varMax)
            {
                varMax = varBetween;
                threshold = t;
            }
        }

        return threshold;
    }
}