package com.cqupt.config;

import com.cqupt.service.PdfExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Zhou Xinyang
 * @date 2025/07/16
 * @description PDF文件清理定时任务
 */
@Component
public class PdfCleanupTask {

    @Autowired
    private PdfExportService pdfExportService;

    /**
     * 每天凌晨3点清理过期PDF文件
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanExpiredPdfFiles() {
        try {
            pdfExportService.cleanExpiredPdfFiles();
            System.out.println("PDF清理任务执行完成: " + java.time.LocalDateTime.now());
        } catch (Exception e) {
            System.err.println("PDF清理任务执行失败: " + e.getMessage());
        }
    }
}
