package com.quark.chat.entity;

import com.quark.common.entity.User;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author : ChinaLHR
 * @Date : Create in 21:31 2017/10/23
 * @Email : 13435500980@163.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatUser {
    private User user;
    private boolean isAuth = false;//是否认证

    private long time = 0;//活跃时间

    private Channel channel;//用户对应的channel

    private String addr;            // 地址

}
