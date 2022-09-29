package com.vrp.controller.utils;

import lombok.Data;

/**
 * @Author jinjun99
 * @Date Created in 2022/4/6 13:52
 * @Description 返回给前端的通用格式
 * @Since version-1.0
 */
@Data
public class Res {
    /**
     * 请求是否成功
     */
    private Boolean flag;
    /**
     * 请求的数据
     */
    private Object data;
    /**
     * 返回提示信息
     */
    private String msg;
    public Res(){}
    public Res(Boolean flag){
        this.flag = flag;
    }

    public Res(Boolean flag, Object data) {
        this.flag = flag;
        this.data = data;
    }

    public Res(Boolean flag, String msg) {
        this.flag = flag;
        this.msg = msg;
    }

    public Res(Boolean flag, Object data, String msg) {
        this.flag = flag;
        this.data = data;
        this.msg = msg;
    }
}
