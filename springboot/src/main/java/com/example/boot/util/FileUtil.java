package com.shiyun.Util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Classname:FileUtil
 *
 * @description: 文件的工具类
 * @author: 陌意随影
 * @Date: 2021-02-20 11:50
 * @Version: 1.0
 **/
public class FileUtil {
    public static final Set<String> imageFormatSet = new HashSet<>(Arrays.asList(".jpg",".jpeg",".png",".bmp"));

    /**
     * @Description :验证是否是支持的图片格式
     * @Date 11:51 2021/2/20 0020
     * @Param * @param fileName ： 文件名
     * @return boolean 支持返回true，不支持返回false
     **/
    public static boolean isImageFormat(String fileName){
        if (fileName == null || fileName.trim().length() ==0){
            return  false;
        }
        String fileFormat = fileName.substring(fileName.lastIndexOf('.'));
        return  imageFormatSet.contains(fileFormat);
    }
}
