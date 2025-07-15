package com.cqupt.utils;

import lombok.Data;

/**
 * @author Zhou Xinyang
 * @date 2025/06/19
 * @description 响应结果封装类
 */
@Data
public class ResultVo<T> {
    private boolean flag; // 响应标志
    private String message; // 响应消息
    private T data; // 响应数据

    // 响应成功
    public static <T> ResultVo<T> ok(String message) {
        ResultVo<T> resultVo = new ResultVo<>();
        resultVo.setMessage(message);
        resultVo.setFlag(true);
        return resultVo;
    }

    // 响应成功添加data
    public static <T> ResultVo<T> ok(T data) {
        ResultVo<T> resultVo = new ResultVo<>();
        resultVo.setData(data);
        resultVo.setFlag(true);
        return resultVo;
    }

    // 响应成功添加data和message
    public static <T> ResultVo<T> ok(T data, String message) {
        ResultVo<T> resultVo = new ResultVo<>();
        resultVo.setData(data);
        resultVo.setMessage(message);
        resultVo.setFlag(true);
        return resultVo;
    }

    // 响应失败添加message
    public static <T> ResultVo<T> fail(String message) {
        ResultVo<T> resultVo = new ResultVo<>();
        resultVo.setMessage(message);
        resultVo.setFlag(false);
        return resultVo;
    }

    // 响应失败添加data
    public static <T> ResultVo<T> fail(T data) {
        ResultVo<T> resultVo = new ResultVo<>();
        resultVo.setData(data);
        resultVo.setFlag(false);
        return resultVo;
    }

    // 响应失败添加data和message
    public static <T> ResultVo<T> fail(T data, String message) {
        ResultVo<T> resultVo = new ResultVo<>();
        resultVo.setData(data);
        resultVo.setMessage(message);
        resultVo.setFlag(false);
        return resultVo;
    }

    // 响应错误信息
    public static <T> ResultVo<T> error(Exception e) {
        ResultVo<T> resultVo = new ResultVo<>();
        resultVo.setFlag(false);
        resultVo.setMessage("【错误信息】 " + e.getMessage());
        return resultVo;
    }
}
