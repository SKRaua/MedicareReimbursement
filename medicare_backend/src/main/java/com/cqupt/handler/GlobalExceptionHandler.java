package com.cqupt.handler;

import com.cqupt.utils.ResultVo;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.SignatureException;

/*
 * @author Wang Yanchen
 * @date 2025/07/12/08:58:59
 * @description
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // SignatureException token非法异常
    @ExceptionHandler(SignatureException.class)
    @ResponseBody
    public ResultVo<String> SignatureExceptionHandler(SignatureException e) {
        // 记录异常信息
        System.err.println("SignatureException: " + e.getMessage());
        return ResultVo.fail("token令牌非法", "token_error");
    }

    // MalformedJwtException token格式异常
    @ExceptionHandler(MalformedJwtException.class)
    @ResponseBody
    public ResultVo<String> MalformedJwtExceptionHandler(MalformedJwtException e) {
        return ResultVo.fail("token令牌格式不正确", "token_error");
    }

    // ExpiredJwtException token过期异常
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseBody
    public ResultVo<String> ExpiredJwtExceptionHandler(ExpiredJwtException e) {
        return ResultVo.fail("token令牌已过期，请重新登录！", "token_error");
    }

    // 统一处理其他异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultVo<String> ExceptionHandler(Exception e) {
        // return ResultVo.fail("服务器异常，请稍后再试！", "server_error");
        if (e.getMessage().contains("token")) {
            return ResultVo.fail(e.getMessage(), "token_error");
        }
        return ResultVo.fail(e.getMessage(), "server_error");
    }
}
