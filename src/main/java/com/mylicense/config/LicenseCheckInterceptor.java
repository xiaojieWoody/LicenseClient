package com.mylicense.config;

import com.alibaba.fastjson.JSON;
import com.mylicense.license.LicenseVerify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class LicenseCheckInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LicenseVerify licenseVerify = new LicenseVerify();

        boolean verifyResult = false;
        try {
            //校验证书是否有效
            verifyResult = licenseVerify.verify();
        } catch (Exception e) {
            log.error("验证证书异常，没安装证书或证书失效...",e);
            response.setCharacterEncoding("utf-8");
            Map<String,String> result = new HashMap<>(1);
            result.put("result","您的证书无效，请核查服务器是否取得授权或重新申请证书！");

            response.getWriter().write(JSON.toJSONString(result));

            return false;
        }
        if(verifyResult){
            return true;
        }else{
            response.setCharacterEncoding("utf-8");
            Map<String,String> result = new HashMap<>(1);
            result.put("result","您的证书无效，请核查服务器是否取得授权或重新申请证书！");

            response.getWriter().write(JSON.toJSONString(result));

            return false;
        }
    }
}
