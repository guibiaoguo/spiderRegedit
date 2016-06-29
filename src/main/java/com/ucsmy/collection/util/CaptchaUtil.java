package com.ucsmy.collection.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Administrator on 2015/12/11.
 */
public class CaptchaUtil {

    public static String tesseract(File file,String language) {
        String recognizeText = null;
        try{
            final String destDir = file.getParent() + "/tmp";
            ClearImageHelper.cleanImage(file, destDir);
            recognizeText = new OCRHelper(language).recognizeText(new File(destDir + "/" + file.getName()));
            return recognizeText;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recognizeText;
    }

}
