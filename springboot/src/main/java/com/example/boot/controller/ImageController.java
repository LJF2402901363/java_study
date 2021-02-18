package com.example.boot.controller;

import com.example.boot.entity.FileInformation;
import com.example.boot.service.FileService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description :
 * @Date 21:18 2021/2/18 0018
 * @Param * @param null ：
 * @return
 **/
@Controller
@RequestMapping("/images")
@Slf4j
public class ImageController {
    @Autowired
    private FileService fileService;

    /**
     * 文件上传
     * @author xct
     * @date 2020-11-20 16:50
     * @param image  用于做封面的图片
     * @param title   标题
     * @return java.lang.String
     */
    @PostMapping("/uploadFile")
    @ApiOperation(value = "图片上传",notes = "接收图片上传的请求")
    public String uploadFile(@RequestParam("fileImage")MultipartFile image,@RequestParam("title")String title){
        if(image.isEmpty()){
            log.error("上传失败，请选择文件！");
            return "redirect:/getAllFile";
        }
        try {
            String result = fileService.uploadFile(image,title);
            log.info(result);
            return "redirect:/getAllFile";
        } catch (Exception e) {
            e.printStackTrace();
            log.error("文件上传失败！");
            return "redirect:/getAllFile";
        }
    }

    /**
     * 查询所有的文件信息
     * @author xct
     * @date 2020-11-19 16:28
     * @param
     * @return java.lang.String
     */
    @GetMapping("/getAllFile")
    @ApiOperation(value = "获取所有图片的路径信息")
    @ResponseBody
    public String getAllFile(HttpServletRequest request){
        List<FileInformation> allFile = fileService.getAllFile();
        request.setAttribute("fileList", allFile);
        return "fileDownload";
    }
}