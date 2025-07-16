package com.cqupt.controller;

import com.cqupt.service.PdfExportService;
import com.cqupt.utils.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Zhou Xinyang
 * @date 2025/07/16
 * @description PDF文件下载控制器
 */
@RestController
@RequestMapping("/pdf")
@Api(tags = "PDF文件下载管理")
public class PdfDownloadController {

    @Autowired
    private PdfExportService pdfExportService;

    @Value("${pdf.export.path:./uploads/pdfs/}")
    private String pdfExportPath;

    @GetMapping("/download/{fileName}")
    @ApiOperation("下载PDF文件")
    public ResponseEntity<Resource> downloadPdf(@PathVariable String fileName) {
        try {
            System.out.println("收到PDF下载请求: " + fileName);

            // 验证文件名安全性
            if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
                System.out.println("文件名不安全: " + fileName);
                return ResponseEntity.badRequest().build();
            }

            // 确保文件名以.pdf结尾
            if (!fileName.endsWith(".pdf")) {
                System.out.println("文件名不是PDF格式: " + fileName);
                return ResponseEntity.badRequest().build();
            }

            File file = new File(pdfExportPath + fileName);
            System.out.println("查找PDF文件路径: " + file.getAbsolutePath());

            if (!file.exists()) {
                System.out.println("PDF文件不存在: " + file.getAbsolutePath());
                return ResponseEntity.notFound().build();
            }

            if (!file.isFile()) {
                System.out.println("不是有效的文件: " + file.getAbsolutePath());
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(file);

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + encodeFileName(fileName) + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);
            headers.add(HttpHeaders.CACHE_CONTROL, "no-cache");

            System.out.println("PDF文件下载成功: " + fileName + ", 文件大小: " + file.length() + " bytes");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .body(resource);

        } catch (Exception e) {
            System.err.println("PDF下载失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/check/{fileName}")
    @ApiOperation("检查PDF文件是否存在")
    public ResultVo<String> checkPdfExists(@PathVariable String fileName) {
        try {
            String message = "收到PDF检查请求: " + fileName;
            System.out.println(message);

            // 验证文件名安全性
            if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
                String errorMsg = "文件名不安全: " + fileName;
                System.out.println(errorMsg);
                return ResultVo.fail(errorMsg);
            }

            // 确保文件名以.pdf结尾
            if (!fileName.endsWith(".pdf")) {
                String errorMsg = "文件名不是PDF格式: " + fileName;
                System.out.println(errorMsg);
                return ResultVo.fail(errorMsg);
            }

            File file = new File(pdfExportPath + fileName);
            String pathMsg = "查找PDF文件路径: " + file.getAbsolutePath();
            System.out.println(pathMsg);

            if (!file.exists()) {
                String errorMsg = "PDF文件不存在: " + file.getAbsolutePath();
                System.out.println(errorMsg);
                return ResultVo.fail(errorMsg);
            }

            if (!file.isFile()) {
                String errorMsg = "不是有效的文件: " + file.getAbsolutePath();
                System.out.println(errorMsg);
                return ResultVo.fail(errorMsg);
            }

            String successMsg = "PDF文件存在且有效: " + fileName + ", 文件大小: " + file.length() + " bytes";
            System.out.println(successMsg);
            return ResultVo.ok(fileName, successMsg);

        } catch (Exception e) {
            String errorMsg = "PDF检查失败: " + e.getMessage();
            System.err.println(errorMsg);
            e.printStackTrace();
            return ResultVo.fail(errorMsg);
        }
    }

    @PostMapping("/clean-expired")
    @ApiOperation("清理过期PDF文件")
    public ResultVo<String> cleanExpiredFiles() {
        try {
            String startMsg = "开始清理过期PDF文件: " + java.time.LocalDateTime.now();
            System.out.println(startMsg);

            File directory = new File(pdfExportPath);
            String dirMsg = "检查目录: " + directory.getAbsolutePath();
            System.out.println(dirMsg);

            if (!directory.exists()) {
                String errorMsg = "PDF目录不存在: " + directory.getAbsolutePath();
                System.out.println(errorMsg);
                return ResultVo.fail(errorMsg);
            }

            // 调用清理服务
            pdfExportService.cleanExpiredPdfFiles();

            // 统计清理结果
            File[] remainingFiles = directory.listFiles((dir, name) -> name.endsWith(".pdf"));
            int remainingCount = remainingFiles != null ? remainingFiles.length : 0;

            String successMsg = "PDF清理任务执行完成: " + java.time.LocalDateTime.now() +
                    ", 剩余文件数量: " + remainingCount;
            System.out.println(successMsg);

            return ResultVo.ok("清理完成", successMsg);

        } catch (Exception e) {
            String errorMsg = "PDF清理任务执行失败: " + e.getMessage();
            System.err.println(errorMsg);
            e.printStackTrace();
            return ResultVo.fail(errorMsg);
        }
    }

    /**
     * 编码文件名以支持中文
     */
    private String encodeFileName(String fileName) {
        try {
            return URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            return fileName;
        }
    }
}
