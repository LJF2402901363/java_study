package com.quark.rest.constant;


/**
 * Classname:quarkbbs
 *
 * @description:{description}
 * @author: 陌意随影
 * @Date: 2021-05-25 18:29
 */
public enum PostsTypeEnum {
     TYPE_TOP("top","置顶"),
     TYPE_GOOD("good","好评"),
     DEFAULT_TYPE("","默认");

    private String msg;
    private String code;
    PostsTypeEnum(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
