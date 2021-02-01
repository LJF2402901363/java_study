package com.moyisuiying.jwt.interceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyisuiying.jwt.uitl.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Classname:Jwtinterceptor
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2021-02-01 12:45
 * @Version: 1.0
 **/
@Slf4j
@Component
public class JwtInterceptor  implements HandlerInterceptor {
    @Autowired
    JwtUtil jwtUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取token值
        String token = request.getHeader(jwtUtil.getHeader());
        Map<String, String> map = new HashMap<>();
        try {
            //验证token是否正确
            jwtUtil.verifyToken(token);
          return  true;
        }catch (SignatureGenerationException signatureGenerationException){
            log.info("签名无效");
            map.put("status","0");
            map.put("msg",signatureGenerationException.getMessage());
        }catch (TokenExpiredException tokenExpiredException){
            log.info("token已过期");
            map.put("status","0");
            map.put("msg",tokenExpiredException.getMessage());
        }catch (AlgorithmMismatchException algorithmMismatchException){
            log.info("加密方法无效");
            map.put("status","0");
            map.put("msg",algorithmMismatchException.getMessage());
        }catch (Exception e){
            log.info("token无效");
            map.put("status","0");
            map.put("msg",e.getMessage());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(map);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
        response.getWriter().flush();
        return false;
    }
}
