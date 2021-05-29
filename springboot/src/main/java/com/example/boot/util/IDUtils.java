package com.example.boot.util;

import java.util.UUID;

/**
 * @Description :生成UUID工具类
 * @Date 23:57 2021/2/1 0001
 * @Param * @param null ：
 * @return
 **/
public class IDUtils {
    /**
     * @Description :生成一个随机的UUID
     * @Date 23:57 2021/2/1 0001
     * @return java.lang.String
     **/
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
    /**
     * @Description :生成随机的文件名
     * @Date 10:36 2021/2/20 0020
     * @Param * @param filename ：原来的文件名
     * @return java.lang.String 新的文件名
     **/
    public static String getFilename(String filename){
         if (filename == null||filename.trim().length() ==0){
             return null;
         }
         //获取文件后缀名
        String fileFormat = filename.substring(filename.lastIndexOf('.'));
         //获取生成的随机文件名
        String fileRandomName = UUID.randomUUID().toString().replace("-", "");
        //文章的图片名 fileName + fileFormat
        return fileRandomName + fileFormat;
    }
}
