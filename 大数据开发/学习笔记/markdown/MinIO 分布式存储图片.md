# MinIO 分布式存储图片

## 1.MinIO 的Docker安装

```bash
docker run  -d  -p 9900:9900  -p 9901:9901 --name minio \
  -e "MINIO_ROOT_USER=minio_shiyun" \
  -e "MINIO_ROOT_PASSWORD=LJFPBJ12345" \
  -v /minio/data:/data \
  -v /minio/config:/root/.minio \
  minio/minio server /data \
  --address ":9900" --console-address ":9901"
```



## 2.springboot整合minio

### 2.1导入依赖

```xml
<!-- https://mvnrepository.com/artifact/io.minio/minio -->
<dependency>
    <groupId>io.minio</groupId>
    <artifactId>minio</artifactId>
    <version>8.3.4</version>
</dependency>

```

### 2.2书写minio连接配置 application.yml

```yaml
# 图片存储服务器minio配置
minio:
  host: 139.198.109.98
  port: 9900
  user:
    name: minio_shiyun
    password: LJFPBJ12345
  bucket-name: shiyun-imagages
```



### 2.3书写minioClient配置类

```java
package com.moyisuiying.shiyunserver.file.config;

import io.minio.MinioClient;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classname:MinioConfig
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2021-12-23 14:47
 * @Version: 1.0
 **/
@Configuration
@Getter
public  class MinioConfig {
    @Value("${minio.host}")
    private  String minioHost;
    @Value("${minio.port}")
    private int port;
    @Value("${minio.user.name}")
    private String minioUser;
    @Value("${minio.user.password}")
    private String minioPassword;
    @Value("${minio.bucket-name}")
    private String bucketName;

    @Bean
    public MinioClient minioClient(){
       return MinioClient.builder()
                        .endpoint(minioHost,port,false)
                        .credentials(minioUser, minioPassword)
                        .build();

  }
}

```

### 2.4书写工具类

```java
package com.moyisuiying.shiyunserver.file.util;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Classname:MinIoUtil
 * @description:  minio 工具类
 * @author: 陌意随影
 * @Date: 2021-12-23 14:47
 * @Version: 1.0
 **/
@Slf4j
@Component
public  class MinIoUtil {
    @Autowired
    public  MinioClient minioClient;


    /**
     * 判断 bucket是否存在
     * @param bucketName: 桶名
     * @return: boolean
     */
    @SneakyThrows(Exception.class)
    public  boolean bucketExists(String bucketName) {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 创建 bucket
     * @param bucketName:  桶名
     * @return: void
     */
    @SneakyThrows(Exception.class)
    public  void createBucket(String bucketName) {
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
        boolean isExist = minioClient.bucketExists(bucketExistsArgs);
        if (!isExist) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    /**
     * 获取全部bucket
     * @param :
     * @return: java.util.List<io.minio.messages.Bucket>
     */
    @SneakyThrows(Exception.class)
    public  List<Bucket> getAllBuckets() {
        return minioClient.listBuckets();
    }

    /**
     * 文件上传
     * @param bucketName: 桶名
     * @param fileNameOfInBucket: 保存到bucket中作为的文件名
     * @param filePath:  源文件路径
     * @return: java.lang.String : 文件url地址
     */
    @SneakyThrows(Exception.class)
    public  String upload(String bucketName, String fileNameOfInBucket, String filePath) {
        UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder().bucket(bucketName).object(fileNameOfInBucket).filename(filePath).build();
        minioClient.uploadObject(uploadObjectArgs);
        return  getFileUrl(bucketName, fileNameOfInBucket);
    }
    /**
     * 文件上传
     * @param bucketName: 桶名
     * @param fileNameOfInBucket: 保存到bucket中作为的文件名
     * @param file:  源文件路
     * @return: java.lang.String : 文件url地址
     */
    @SneakyThrows(Exception.class)
    public  String upload(String bucketName, String fileNameOfInBucket, File file) {
        FileInputStream fileStream = new FileInputStream(file);
        PutObjectArgs putObjectArgs = PutObjectArgs.builder().
                bucket(bucketName).object(fileNameOfInBucket).
                stream(fileStream,fileStream.available(),-1).build();
        minioClient.putObject(putObjectArgs);
        return getFileUrl(bucketName, fileNameOfInBucket);

    }
    /**
     * 文件上传
     * @param bucketName: 桶名
     * @param fileNameOfInBucket: 保存到bucket中作为的文件名
     * @param fileStream:  需要上传的文件流
     * @return: java.lang.String : 文件url地址
     */
    @SneakyThrows(Exception.class)
    public  String upload(String bucketName, String fileNameOfInBucket, InputStream fileStream) {
        PutObjectArgs putObjectArgs = PutObjectArgs.builder().
                bucket(bucketName).object(fileNameOfInBucket).
                stream(fileStream,fileStream.available(),-1).build();
        minioClient.putObject(putObjectArgs);
        return getFileUrl(bucketName, fileNameOfInBucket);
    }

    /**
     * @Description :文件上传
     * @Date 19:06 2021/12/23 0023
     * @Param * @param bucketName 桶名
     * @param file ： 需要上传的文件MultipartFile
     * @return java.lang.String  文件url地址
     **/
    @SneakyThrows(Exception.class)
    public  String upload(String bucketName, MultipartFile file) {
        final InputStream fileStream = file.getInputStream();
        final String fileNameOfInBucket = file.getOriginalFilename();
        PutObjectArgs putObjectArgs = PutObjectArgs.builder().
                bucket(bucketName).object(fileNameOfInBucket).
                stream(fileStream,fileStream.available(),-1).build();
        minioClient.putObject(putObjectArgs);
        return getFileUrl(bucketName, fileNameOfInBucket);
    }

    /**
     * @Description :删除文件
     * @Date 19:06 2021/12/23 0023
     * @Param * @param bucketName  桶名
     * @param fileNameOfInBucket ：文件名
     * @return void
     **/
    @SneakyThrows(Exception.class)
    public  void deleteFile(String bucketName, String fileNameOfInBucket) {
        RemoveObjectArgs removeBucketArgs = RemoveObjectArgs.builder()
                .bucket(bucketName).object(fileNameOfInBucket).build();
        minioClient.removeObject(removeBucketArgs);
    }


    /**
     * @Description :获取minio文件的下载地址
     * @Date 19:05 2021/12/23 0023
     * @Param * @param bucketName  桶名
     * @param fileNameOfInBucket ：文件名
     * @return java.lang.String
     **/
    @SneakyThrows(Exception.class)
    public  String getFileUrl(String bucketName, String fileNameOfInBucket) {
        GetPresignedObjectUrlArgs getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder().bucket(bucketName)
                .method(Method.GET).object(fileNameOfInBucket).build();
        return minioClient.getPresignedObjectUrl(getPresignedObjectUrlArgs);
    }


}
```

### 2.5测试类

```java
package com.moyisuiying.shiyunserver.minio;

import com.moyisuiying.shiyunserver.file.util.MinIoUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

/**
 * Classname:MinioUtilTest
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2021-12-23 19:39
 * @Version: 1.0
 **/
@SpringBootTest
public class MinioUtilTest {
    @Value("${minio.bucket-name}")
    private String bucketName;
    @Autowired
   private MinIoUtil minIoUtil;
    @Test
    public void testIsBucketExists(){
        boolean bucketExists = minIoUtil.bucketExists(bucketName);
        System.out.println(bucketExists);
    }

    @Test
    public void testCreateBucket(){
        minIoUtil.createBucket(bucketName);
    }
    @Test
    public void testUpload() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("images/shiyun_test1.jpeg");
        File file = classPathResource.getFile();
        if (!file.exists()){
            System.out.println("文件不存在！！");
            return;
        }
        String name = file.getName();
        String url = minIoUtil.upload(bucketName,name,file);
        System.out.println(url);
    }
    @Test
    public void testDelete(){
        minIoUtil.deleteFile(bucketName,"shiyun_test1.jpeg");
    }
}

```

