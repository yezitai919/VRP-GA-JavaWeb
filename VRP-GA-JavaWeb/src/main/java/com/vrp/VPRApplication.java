package com.vrp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class VPRApplication {
    public static void main(String[] args) {
        SpringApplication.run(VPRApplication.class, args);
        /*运行自动打开登录页面*/
        try {
            Runtime.getRuntime().exec("cmd /c start http://localhost:8080/page/login.html");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
