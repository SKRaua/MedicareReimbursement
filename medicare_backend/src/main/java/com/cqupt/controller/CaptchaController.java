package com.cqupt.controller;

import com.cqupt.utils.ResultVo;
import com.cqupt.utils.CaptchaUtil;
import com.cqupt.utils.CaptchaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class CaptchaController {

    @Autowired
    private CaptchaManager captchaManager;

    @GetMapping("/captcha")
    public ResultVo<Map<String, String>> getCaptcha() {
        try {
            CaptchaUtil.CaptchaResult captcha = CaptchaUtil.generateCaptcha();

            // 生成唯一ID
            String captchaId = UUID.randomUUID().toString();
            
            // 存储到缓存中，5分钟过期
            captchaManager.put(captchaId, captcha.getCode());

            Map<String, String> result = new HashMap<>();
            result.put("image", captcha.getImageBase64());
            result.put("captchaId", captchaId);

            return ResultVo.ok(result, "获取验证码成功");
        } catch (IOException e) {
            return ResultVo.fail("生成验证码失败");
        }
    }
}