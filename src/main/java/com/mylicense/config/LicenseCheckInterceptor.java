package com.mylicense.config;

import com.alibaba.fastjson.JSON;
import com.mylicense.common.ResMsg;
import com.mylicense.common.SpringContextUtils;
import com.mylicense.license.LicenseVerify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
public class LicenseCheckInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        LicenseVerify licenseVerify = new LicenseVerify();
        LicenseVerify licenseVerify = SpringContextUtils.getBeanByClass(LicenseVerify.class);

        boolean verifyResult = false;
        try {
            //校验证书是否有效
            verifyResult = licenseVerify.verify();
        } catch (Exception e) {
            log.error("验证证书异常，没安装证书或证书失效...",e);
            response.setCharacterEncoding("utf-8");
            ResMsg res = new ResMsg(-1, "license is expired or not exist!", "", null );
            response.getWriter().write(JSON.toJSONString(res));
            return false;
        }
        if(verifyResult){
            return true;
        }else{
            log.error("验证证书异常，没安装证书或证书失效...");
            response.setCharacterEncoding("utf-8");
            ResMsg res = new ResMsg(-1, "您的证书无效，请核查服务器是否取得授权或重新申请证书！", "", null );
            response.getWriter().write(JSON.toJSONString(res));

            return false;
        }
    }
}
