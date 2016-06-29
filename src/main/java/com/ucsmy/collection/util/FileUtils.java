package com.ucsmy.collection.util;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/11/5.
 */
public class FileUtils {
//    public static void main(String[] args) throws IOException{
//        File file = new File("D:\\Program Files\\Tesseract-OCR\\train2\\gsxt.font.exp0.box");
//        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
//        String temp = null;
//        StringBuffer buffer = new StringBuffer();
//        Pattern pattern = Pattern.compile("^[°Ë] 10");
//        Matcher matcher = null;
//        int temp1 = 0 ,temp2 = 0;
//        while((temp = reader.readLine()) != null) {
////            System.out.println(temp.substring(0,4).trim());
//            matcher = pattern.matcher(temp.substring(0,4).trim());
//            String[] tempArr = temp.split(" ");
//            if (matcher.find()) {
//                temp1 = 10;
//                temp2 = 30;
//                buffer.append(tempArr[0]).append(" ").append(10).append(" ").append(18).append(" ").append(29).append(" ").append(37).append(" ").append(tempArr[5]).append("\n");
//                System.out.println(temp);
//            } else {
//                buffer.append(temp).append("\n");
//            }
//        }
//        PrintWriter printWriter = new PrintWriter(new File("D:\\Program Files\\Tesseract-OCR\\train2\\gsxt.font.exp0.box.bak"));
//        printWriter.write(buffer.toString());
//        printWriter.flush();
//        printWriter.close();
//    }
}
