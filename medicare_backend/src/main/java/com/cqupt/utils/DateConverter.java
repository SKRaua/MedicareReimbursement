package com.cqupt.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Zhou Xinyang
 * @date 2025/06/19
 * @description 日期转换工具类
 */
@Component
public class DateConverter implements Converter<String, Date> {

    private static final List<String> FORMATS = new ArrayList<>();
    static {
        FORMATS.add("yyyy-MM");
        FORMATS.add("yyyy-MM-dd");
        FORMATS.add("yyyy-MM-dd HH:mm");
        FORMATS.add("yyyy-MM-dd HH:mm:ss");
    }

    // 实现日期字符串转换成Date
    @Override
    public Date convert(@NonNull String dateStr) {
        // 实现日期字符串到Date的转换逻辑
        String value = dateStr.trim();// 去除首尾空格
        if ("".equals(value)) {
            return null;
        }
        // 正则表达式解释：
        // ^ 表示字符串的开头，$ 表示结尾
        // \\d 转义表示数字
        // {4} 表示数字的个数，{1,2} 表示重复1到2次
        if (dateStr.matches("^\\d{4}-\\d{1,2}$")) {
            return parseDate(dateStr, FORMATS.get(0));
        } else if (dateStr.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return parseDate(dateStr, FORMATS.get(1));
        } else if (dateStr.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            return parseDate(dateStr, FORMATS.get(2));
        } else if (dateStr.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return parseDate(dateStr, FORMATS.get(3));
        } else {
            throw new IllegalArgumentException("Invalid date: " + dateStr);
        }
    }

    // 日期字符串转换成Date
    private Date parseDate(String dateStr, String format) {
        Date date = null;
        try {
            date = new SimpleDateFormat(format).parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
}
