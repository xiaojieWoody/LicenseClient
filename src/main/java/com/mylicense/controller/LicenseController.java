package com.mylicense.controller;

import com.mylicense.common.CommonConstant;
import com.mylicense.common.ResMsg;
import com.mylicense.service.ILicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LicenseController {

    @Autowired
    private ILicenseService licenseService;

    /**
     * 获取机器码
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getMachineCode", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResMsg getMachineCode() throws Exception {
        return licenseService.getMachineCode();
    }

    /**
     * 导入证书
     * @param file
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("/import")
    public String importLicense(MultipartFile file) throws IOException {
        return licenseService.importLicense2Resource(file);
    }

    /**
     * 模拟登录验证
     */
    @PostMapping("/check")
    @ResponseBody
    public Map<String,Object> test(@RequestParam(required = true) String username, @RequestParam(required = true) String password){
        Map<String,Object> result = new HashMap<>(1);
        System.out.println(MessageFormat.format("用户名：{0}，密码：{1}",username,password));

        //模拟登录
        System.out.println("模拟登录流程");
        result.put("code",200);

        return result;
    }

    /**
     * 用户首页
     */
    @RequestMapping("/userIndex")
    public String userIndex(){
        return "userIndex";
    }
}
