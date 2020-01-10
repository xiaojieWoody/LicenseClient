## LicenseClient流程
* 根据实际业务场景是直接替换License文件而不用重启系统，所以没有采用License安装，而是采用License验证
* 在监听应用启动时、拦截器里拦截时都先获取License文件，然后进行License文件有效性验证
