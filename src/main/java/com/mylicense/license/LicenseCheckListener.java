package com.mylicense.license;

import com.mylicense.config.LicenseConfig;
import com.mylicense.license.param.LicenseVerifyParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 在项目启动时安装证书
 *
 * @author zifangsky
 * @date 2018/4/24
 * @since 1.0.0
 */
@Slf4j
@Component
public class LicenseCheckListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private LicenseConfig licenseConfig;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //root application context 没有parent
        ApplicationContext context = event.getApplicationContext().getParent();
        if(context == null){
            // 测试 获取文件路 ok
//            //licensePath = LicenseCheckListener.class.getClassLoader().getResource("license.lic").getPath();;
//            URL resource = LicenseCheckListener.class.getClassLoader().getResource("license.dat");
//            if(null != resource) {
//                licensePath = resource.getPath();
//            } else {
//                log.error("请先添加授权证书");
//            }
            String licensePath = licenseConfig.getLicensePath();
            File license = new File(licensePath);
            if(license.exists()){
                log.info("++++++++ 开始安装证书 ++++++++");
                LicenseVerifyParam param = new LicenseVerifyParam();
                BeanUtils.copyProperties(licenseConfig, param);

                LicenseVerify licenseVerify = new LicenseVerify();
                //安装证书
                licenseVerify.install(param);

                log.info("++++++++ 证书安装结束 ++++++++");
            } else {
                log.info("License证书不存在!");
            }
        }
    }
}
