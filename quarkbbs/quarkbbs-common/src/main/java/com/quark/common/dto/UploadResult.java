package com.quark.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author LHR
 * Create By 2017/8/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadResult implements Serializable{

    /**
     * 0表示成功 ，其余失败
     */
   private Integer code;

    /**
     * 错误消息
     */
   private String msg;

   private Data data;


    public static class Data {
       /**
        * 路径
        */
       private String src;

       /**
        * 名称
        */
       private String title;

       public String getSrc() {
           return src;
       }

       public String getTitle() {
           return title;
       }

        public Data(String src) {
            this.src = src;
        }
    }

    /**
     * 成功的构造函数
     * @param code
     * @param data
     */
    public UploadResult(Integer code, Data data) {
        this.code = code;
        this.data = data;
    }

    /**
     * 失败的构造函数
     * @param code
     * @param msg
     */
    public UploadResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
