package com.cqupt.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

public class CaptchaUtil {
    private static final String CODES = "23456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";
    private static final int WIDTH = 120;
    private static final int HEIGHT = 40;
    private static final int CODE_COUNT = 4;

    public static class CaptchaResult {
        private String code;
        private String imageBase64;

        public CaptchaResult(String code, String imageBase64) {
            this.code = code;
            this.imageBase64 = imageBase64;
        }

        public String getCode() {
            return code;
        }

        public String getImageBase64() {
            return imageBase64;
        }
    }

    public static CaptchaResult generateCaptcha() throws IOException {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        Random random = new Random();

        // 设置背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 设置字体
        g.setFont(new Font("Arial", Font.BOLD, 25));

        // 生成验证码
        StringBuilder codeBuilder = new StringBuilder();
        for (int i = 0; i < CODE_COUNT; i++) {
            String code = String.valueOf(CODES.charAt(random.nextInt(CODES.length())));
            codeBuilder.append(code);

            // 设置随机颜色
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            g.drawString(code, 20 + i * 25, 25);
        }

        // 添加干扰线
        for (int i = 0; i < 5; i++) {
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            g.drawLine(random.nextInt(WIDTH), random.nextInt(HEIGHT),
                    random.nextInt(WIDTH), random.nextInt(HEIGHT));
        }

        g.dispose();

        // 转换为Base64
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        String imageBase64 = Base64.getEncoder().encodeToString(baos.toByteArray());

        return new CaptchaResult(codeBuilder.toString(), "data:image/png;base64," + imageBase64);
    }
}