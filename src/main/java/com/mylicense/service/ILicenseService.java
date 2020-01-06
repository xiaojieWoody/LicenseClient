package com.mylicense.service;

import com.mylicense.common.ResMsg;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ILicenseService {
    /**
     * 获取机器码
     * @return
     * @throws Exception
     */
    ResMsg getMachineCode() throws Exception;

    /**
     * 上传证书
     * @param file
     * @return
     * @throws IOException
     */
    String importLicense2Resource(MultipartFile file) throws IOException;
}
