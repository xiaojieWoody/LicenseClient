package com.mylicense.service.impl;

import com.alibaba.fastjson.JSON;
import com.mylicense.common.ResMsg;
import com.mylicense.license.LicenseCheckListener;
import com.mylicense.license.LicenseVerify;
import com.mylicense.license.machine.AbstractMachineInfo;
import com.mylicense.license.machine.LinuxMachineInfo;
import com.mylicense.license.machine.WindowsMachineInfo;
import com.mylicense.license.model.LicenseCheckModel;
import com.mylicense.license.param.LicenseVerifyParam;
import com.mylicense.service.ILicenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;

@Slf4j
@Service
public class LicenseService implements ILicenseService {

    /**
     * 证书subject
     */
    @Value("${license.subject}")
    private String subject;

    /**
     * 公钥别称
     */
    @Value("${license.publicAlias}")
    private String publicAlias;

    /**
     * 访问公钥库的密码
     */
    @Value("${license.storePass}")
    private String storePass;

    /**
     * 证书生成路径
     */
    @Value("${license.licensePath}")
    private String licensePath;

    /**
     * 密钥库存储路径
     */
    @Value("${license.publicKeysStorePath}")
    private String publicKeysStorePath;

    /**
     * 获取服务器硬件信息
     * @return
     * @throws Exception
     */
    @Override
    public ResMsg getMachineCode() throws Exception {

        String osName = System.getProperty("os.name").toLowerCase();

        AbstractMachineInfo abstractMachineInfo = null;
        if (osName.startsWith("windows")) {
            abstractMachineInfo = new WindowsMachineInfo();
        } else {
            abstractMachineInfo = new LinuxMachineInfo();
        }
        LicenseCheckModel machineInfo = abstractMachineInfo.getMachineInfo();

        // 转Base64
        String encoderMsg = Base64.getUrlEncoder().encodeToString(JSON.toJSONBytes(machineInfo));
        return new ResMsg(200, "success", "", encoderMsg);
    }

    @Override
    public String importLicense2Resource(MultipartFile file) throws IOException {
        // 上传文件到Resource
        String filename = file.getOriginalFilename();
        log.info("filename-{}", filename);

        //String path = LicenseCheckListener.class.getClassLoader().getResource("").getPath();
        String path = ResourceUtils.getURL("classpath:").getPath();

        FileOutputStream out = new FileOutputStream(path + filename);
        out.write(file.getBytes());
        out.flush();
        out.close();

        URL resource = LicenseCheckListener.class.getClassLoader().getResource("license.dat");
        if(null != resource) {
            licensePath = resource.getPath();
        } else {
            log.error("请先添加授权证书");
            throw new RuntimeException("请先添加授权证书");
        }
        // 重新安装证书
        log.info("++++++++ 开始安装证书 ++++++++");

        LicenseVerifyParam param = new LicenseVerifyParam();
        param.setSubject(subject);
        param.setPublicAlias(publicAlias);
        param.setStorePass(storePass);
        param.setLicensePath(licensePath);
        param.setPublicKeysStorePath(publicKeysStorePath);

        LicenseVerify licenseVerify = new LicenseVerify();
        //安装证书
        licenseVerify.install(param);

        log.info("++++++++ 证书安装结束 ++++++++");

        return "success";
    }
}
