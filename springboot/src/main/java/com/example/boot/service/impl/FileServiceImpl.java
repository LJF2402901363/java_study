package com.example.boot.service.impl;

import com.example.boot.entity.FileInformation;
import com.example.boot.mapper.FileInformationMapper;
import com.example.boot.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Classname:FileServiceImpl
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2021-02-18 21:01
 * @Version: 1.0
 **/
@Service
@Slf4j
public class FileServiceImpl  implements FileService {
    @Autowired
    FileInformationMapper fileMapper;


    /**
     * 文件上传的实现方法
     * @author xct
     * @date 2020-11-20 16:41
     * @param image
     * @return java.lang.String
     */
    @Override
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public String uploadFile(MultipartFile image,String title)  throws Exception {
        File imagePath;  //封面图片存放地址
        String imageName = "";
            String os = System.getProperty("os.name");
            if (os.toLowerCase().startsWith("win")) {  //windows系统
                String path = System.getProperty("user.dir");  //获取项目相对路径
                imagePath = new File(path+"/src/main/resources/static/images");
            }else{//linux系统
                //获取根目录
                //如果是在本地windows环境下，目录为项目的target\classes下
                //如果是linux环境下，目录为jar包同级目录
                File rootPath = new File(ResourceUtils.getURL("classpath:").getPath());
                if(!rootPath.exists()){
                    rootPath = new File("");
                }
                imagePath = new File(rootPath.getAbsolutePath()+"/images");
            }
            if(!imagePath.exists()){
                //不存在，创建
                imagePath.mkdirs();
            }
            //获取文件名称
             imageName = image.getOriginalFilename();
            //创建图片存放地址
            File imageResultPath = new File(imagePath+"/"+imageName);
            if(imageResultPath.exists()){
                log.warn("图片已经存在！");
                return "false！";
            }
            image.transferTo(imageResultPath);
            FileInformation fileInformation = new FileInformation();
            fileInformation.setCreatetimeat(new Date());
            fileInformation.setFilename(imageName);
            fileInformation.setFilepath(imageResultPath.getCanonicalPath());
            fileInformation.setIsdeleted(false);
            //将该图片信息保存到数据库中去
            fileMapper.insert(fileInformation);
            int i = 1/0;
            System.out.println("imageResultPath:"+imageResultPath.getCanonicalPath());
            return "true！";
    }

    @Override
    public List<FileInformation> getAllFile() {
        return fileMapper.findAllFile();
    }

}
