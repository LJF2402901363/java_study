package com.moyisuiying.jwt.uitl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.moyisuiying.jwt.entity.LoginUser;
import com.moyisuiying.jwt.entity.User;
import lombok.Data;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Classname:JWTUtil
 *
 * @description: jWT的工具类
 * @author: 陌意随影
 * @Date: 2021-01-31 23:32
 * @Version: 1.0
 **/
@Slf4j
@Component
@Data
public class JwtUtil {
    // 令牌自定义标识
    @Value("${token.header}")
    private String header;
    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    // 令牌有效期（默认1）
    @Value("${token.expireTime}")
    public static  Integer expireTime;
    /**
     * 默认过期时间1天
     */
    public static final Integer DEFAULT_EXPIRETIME = 1;

    /**
     * 生成JwtToken
     * @param playloaMap  封装有用户书籍的map
     */
    public String createToken(Map<String, String> playloaMap) {
        if (playloaMap == null || playloaMap.size() == 0) {
            return null;
        }
        // 过期时间
        Calendar ca = Calendar.getInstance();
        if (expireTime == null || expireTime <= 0) {
            expireTime = DEFAULT_EXPIRETIME;
        }
        //设置token有效日期
        ca.add(Calendar.DATE, expireTime);
        // 创建JwtToken对象
        JWTCreator.Builder builder = JWT.create();
        playloaMap.forEach((k, v) -> {
            builder.withClaim(k, v);
        });
        // 发布时间
        builder.withIssuedAt(new Date());
        // 过期时间
        builder.withExpiresAt(ca.getTime());
        // 签名加密
        String  token = builder.sign(Algorithm.HMAC256(secret));
        return token;
    }
    /**
     * @Description :获取token数据声明中keyName对应的value值
     * @Date 16:55 2021/2/1 0001
     * @Param * @param keyName   数据生命的key
     * @param token 已有的token
     * @return String
     **/
  public String  getTokenClaimByName(String keyName,String token){
      DecodedJWT decode = JWT.decode(token);
      return decode.getClaim(keyName).asString();
  }
    /**
     * 验证JwtToken
     * @param token JwtToken数据
     * @return true 验证通过
     */
    public void verifyToken(String token) {
        JWTVerifier build = JWT.require(Algorithm.HMAC256(secret)).build();
        build.verify(token);
    }
    /**
     * @Description :通过User和token构建一个loginUser
     * @Date 16:04 2021/2/1 0001
     * @Param * @param user  需要登录的用户User对象
     * @param token ：token值
     * @return com.moyisuiying.jwt.entity.LoginUser
     **/
   public static LoginUser buildLoginUser(User user,String token){
        //将天数转化为毫秒  24 小时 * 60 分钟 * 60 秒 * 1000 毫秒 = 1 天
        Long expireTimeMillis = expireTime * 24 * 60 * 60 *1000L;
       LoginUser loginUser = new LoginUser();
       loginUser.setToken(token);
       loginUser.setExpireTime(expireTimeMillis);
       loginUser.setLoginTime(System.currentTimeMillis());
       loginUser.setUser(user);
       return loginUser;
   }

}

