 package com.vrp.controller.utils;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

 /**
  * @Author jinjun99
  * @Date Created in 2022/4/6 13:52
  * @Description springmvc的异常处理器
  * @Since version-1.0
  */
@RestControllerAdvice
public class ProjectExceptionAdvice {
     /**
      * 拦截所有的异常信息
      * @param ex
      * @return
      */
    @ExceptionHandler(Exception.class)
    public Res doException(Exception ex){

        ex.printStackTrace();
        return new Res(false,"服务器故障，请稍后再试！");
    }
}
