package com.example.boot.service;

import com.example.boot.entity.FileInformation;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Classname:boot
 *
 * @description:{description}
 * @author: 陌意随影
 * @Date: 2021-02-18 20:59
 */

public interface FileService {
    /**
     * 文件上传的实现方法
     * @author xct
     * @date 2020-11-20 16:41
     * @param image
     * @param title
     * @return java.lang.String
     */
    public String uploadFile(MultipartFile image, String title) throws Exception;

    List<FileInformation> getAllFile();
}
