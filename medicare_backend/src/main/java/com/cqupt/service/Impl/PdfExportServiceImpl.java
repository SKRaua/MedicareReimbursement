package com.cqupt.service.Impl;

import com.cqupt.dto.ReimbursementCalcDTO;
import com.cqupt.mapper.DrugMapper;
import com.cqupt.mapper.MedicalServiceMapper;
import com.cqupt.mapper.TreatmentItemMapper;
import com.cqupt.pojo.*;
import com.cqupt.service.PdfExportService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Zhou Xinyang
 * @date 2025/07/16
 * @description PDF导出服务实现类
 */
@Service
public class PdfExportServiceImpl implements PdfExportService {

    @Autowired
    private DrugMapper drugMapper;

    @Autowired
    private MedicalServiceMapper medicalServiceMapper;

    @Autowired
    private TreatmentItemMapper treatmentItemMapper;

    @Value("${pdf.export.path:./uploads/pdfs/}")
    private String pdfExportPath;

    @Value("${pdf.export.expire-hours:24}")
    private int expireHours;

    @Override
    public String generateReimbursementPdf(
            Insureder insureder,
            ReimbursementCalcDTO calc,
            List<DrugOrder> drugAList,
            List<DrugOrder> drugBList,
            List<DrugOrder> drugCList,
            List<MedicalServiceOrder> serviceList,
            List<TreatmentItemOrder> itemList) throws Exception {

        // 确保目录存在
        File directory = new File(pdfExportPath);
        System.out.println("PDF导出目录: " + directory.getAbsolutePath());
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            System.out.println("创建PDF目录: " + created);
        }

        // 生成文件名
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = String.format("reimbursement_%s_%s.pdf", insureder.getName(), timestamp);
        String filePath = pdfExportPath + fileName;

        System.out.println("PDF文件名: " + fileName);
        System.out.println("PDF完整路径: " + filePath);

        // 创建PDF文档
        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // 设置字体 - 支持中文
        PdfFont font = createChineseFont();

        try {
            // 标题
            Paragraph title = new Paragraph("医疗保险报销结算单")
                    .setFont(font)
                    .setFontSize(18)
                    .setMarginBottom(20);
            document.add(title);

            // 基本信息表格
            addBasicInfoTable(document, font, insureder, calc);

            // 费用明细
            addExpenseDetails(document, font, drugAList, drugBList, drugCList, serviceList, itemList);

            // 报销汇总
            addReimbursementSummary(document, font, calc);

            // 页脚信息
            addFooter(document, font);

        } finally {
            document.close();
        }

        // 验证文件是否创建成功
        File generatedFile = new File(filePath);
        if (generatedFile.exists()) {
            System.out.println(
                    "PDF文件生成成功: " + generatedFile.getAbsolutePath() + ", 大小: " + generatedFile.length() + " bytes");
        } else {
            System.err.println("PDF文件生成失败: " + filePath);
            throw new Exception("PDF文件生成失败");
        }

        return fileName; // 返回文件名，供前端下载
    }

    /**
     * 添加基本信息表格
     */
    private void addBasicInfoTable(Document document, PdfFont font, Insureder insureder, ReimbursementCalcDTO calc) {
        // 基本信息标题
        Paragraph infoTitle = new Paragraph("参保人基本信息")
                .setFont(font)
                .setFontSize(14)
                .setMarginTop(10)
                .setMarginBottom(10);
        document.add(infoTitle);

        // 基本信息表格
        Table infoTable = new Table(4).setWidth(500);

        // 添加基本信息行
        addInfoRow(infoTable, font, "姓名", insureder.getName(), "身份证号", insureder.getIdCard());
        addInfoRow(infoTable, font, "性别", getGenderText(insureder.getGender()), "年龄",
                String.valueOf(calculateAge(insureder.getBirthDate())));
        addInfoRow(infoTable, font, "工作状态", insureder.getWorkStatus(), "结算类型", insureder.getSettlementType());
        addInfoRow(infoTable, font, "住院号", insureder.getInpatientNo(), "联系电话", insureder.getContactPhone());
        addInfoRow(infoTable, font, "入院时间", formatDate(insureder.getAdmissionTime()), "出院时间",
                formatDate(insureder.getDischargeTime()));

        // 处理可能很长的地址
        infoTable.addCell(createHeaderCell(font, "家庭地址"));
        infoTable.addCell(createDataCell(font, insureder.getAddress() != null ? insureder.getAddress() : ""));
        infoTable.addCell(createHeaderCell(font, "紧急联系人"));
        infoTable.addCell(
                createDataCell(font, insureder.getEmergencyContact() != null ? insureder.getEmergencyContact() : ""));

        document.add(infoTable);
    }

    /**
     * 添加费用明细
     */
    private void addExpenseDetails(Document document, PdfFont font,
            List<DrugOrder> drugAList, List<DrugOrder> drugBList, List<DrugOrder> drugCList,
            List<MedicalServiceOrder> serviceList, List<TreatmentItemOrder> itemList) {

        // 甲类药品
        if (!drugAList.isEmpty()) {
            addDrugTable(document, font, "甲类药品明细", drugAList);
        }

        // 乙类药品
        if (!drugBList.isEmpty()) {
            addDrugTable(document, font, "乙类药品明细", drugBList);
        }

        // 丙类药品
        if (!drugCList.isEmpty()) {
            addDrugTable(document, font, "丙类药品明细", drugCList);
        }

        // 医疗服务
        if (!serviceList.isEmpty()) {
            addServiceTable(document, font, "医疗服务明细", serviceList);
        }

        // 诊疗项目
        if (!itemList.isEmpty()) {
            addTreatmentTable(document, font, "诊疗项目明细", itemList);
        }
    }

    /**
     * 添加药品明细表格
     */
    private void addDrugTable(Document document, PdfFont font, String title, List<DrugOrder> drugList) {
        Paragraph tableTitle = new Paragraph(title)
                .setFont(font)
                .setFontSize(12)
                .setMarginTop(15)
                .setMarginBottom(8);
        document.add(tableTitle);

        Table table = new Table(5).setWidth(500);

        // 表头
        table.addHeaderCell(createHeaderCell(font, "药品名称"));
        table.addHeaderCell(createHeaderCell(font, "单位"));
        table.addHeaderCell(createHeaderCell(font, "数量"));
        table.addHeaderCell(createHeaderCell(font, "单价"));
        table.addHeaderCell(createHeaderCell(font, "小计"));

        BigDecimal subtotal = BigDecimal.ZERO;
        for (DrugOrder order : drugList) {
            Drug drug = drugMapper.selectById(order.getDrugId());
            if (drug != null) {
                BigDecimal price = drug.getPrice() != null ? drug.getPrice() : BigDecimal.ZERO;
                BigDecimal total = price.multiply(new BigDecimal(order.getQuantity()));
                subtotal = subtotal.add(total);

                table.addCell(createDataCell(font, drug.getDrugName()));
                table.addCell(createDataCell(font, drug.getUnit()));
                table.addCell(createDataCell(font, String.valueOf(order.getQuantity())));
                table.addCell(createDataCell(font, String.format("%.2f", price)));
                table.addCell(createDataCell(font, String.format("%.2f", total)));
            }
        }

        // 小计行
        table.addCell(createDataCell(font, "小计"));
        table.addCell(createDataCell(font, ""));
        table.addCell(createDataCell(font, ""));
        table.addCell(createDataCell(font, ""));
        table.addCell(createDataCell(font, String.format("%.2f", subtotal)));

        document.add(table);
    }

    /**
     * 添加医疗服务表格
     */
    private void addServiceTable(Document document, PdfFont font, String title, List<MedicalServiceOrder> serviceList) {
        Paragraph tableTitle = new Paragraph(title)
                .setFont(font)
                .setFontSize(12)
                .setMarginTop(15)
                .setMarginBottom(8);
        document.add(tableTitle);

        Table table = new Table(5).setWidth(500);

        // 表头
        table.addHeaderCell(createHeaderCell(font, "服务名称"));
        table.addHeaderCell(createHeaderCell(font, "单位"));
        table.addHeaderCell(createHeaderCell(font, "数量"));
        table.addHeaderCell(createHeaderCell(font, "单价"));
        table.addHeaderCell(createHeaderCell(font, "小计"));

        BigDecimal subtotal = BigDecimal.ZERO;
        for (MedicalServiceOrder order : serviceList) {
            MedicalService service = medicalServiceMapper.selectById(order.getItemId());
            if (service != null) {
                BigDecimal price = service.getPrice() != null ? service.getPrice() : BigDecimal.ZERO;
                BigDecimal total = price.multiply(new BigDecimal(order.getQuantity()));
                subtotal = subtotal.add(total);

                table.addCell(createDataCell(font, service.getItemName())); // 使用itemName而不是serviceName
                table.addCell(createDataCell(font, service.getUnit()));
                table.addCell(createDataCell(font, String.valueOf(order.getQuantity())));
                table.addCell(createDataCell(font, String.format("%.2f", price)));
                table.addCell(createDataCell(font, String.format("%.2f", total)));
            }
        }

        // 小计行
        table.addCell(createDataCell(font, "小计"));
        table.addCell(createDataCell(font, ""));
        table.addCell(createDataCell(font, ""));
        table.addCell(createDataCell(font, ""));
        table.addCell(createDataCell(font, String.format("%.2f", subtotal)));

        document.add(table);
    }

    /**
     * 添加诊疗项目表格
     */
    private void addTreatmentTable(Document document, PdfFont font, String title, List<TreatmentItemOrder> itemList) {
        Paragraph tableTitle = new Paragraph(title)
                .setFont(font)
                .setFontSize(12)
                .setMarginTop(15)
                .setMarginBottom(8);
        document.add(tableTitle);

        Table table = new Table(5).setWidth(500);

        // 表头
        table.addHeaderCell(createHeaderCell(font, "项目名称"));
        table.addHeaderCell(createHeaderCell(font, "单位"));
        table.addHeaderCell(createHeaderCell(font, "数量"));
        table.addHeaderCell(createHeaderCell(font, "单价"));
        table.addHeaderCell(createHeaderCell(font, "小计"));

        BigDecimal subtotal = BigDecimal.ZERO;
        for (TreatmentItemOrder order : itemList) {
            TreatmentItem item = treatmentItemMapper.selectById(order.getItemId());
            if (item != null) {
                BigDecimal price = item.getPrice() != null ? item.getPrice() : BigDecimal.ZERO;
                BigDecimal total = price.multiply(new BigDecimal(order.getQuantity()));
                subtotal = subtotal.add(total);

                table.addCell(createDataCell(font, item.getItemName()));
                table.addCell(createDataCell(font, item.getUnit()));
                table.addCell(createDataCell(font, String.valueOf(order.getQuantity())));
                table.addCell(createDataCell(font, String.format("%.2f", price)));
                table.addCell(createDataCell(font, String.format("%.2f", total)));
            }
        }

        // 小计行
        table.addCell(createDataCell(font, "小计"));
        table.addCell(createDataCell(font, ""));
        table.addCell(createDataCell(font, ""));
        table.addCell(createDataCell(font, ""));
        table.addCell(createDataCell(font, String.format("%.2f", subtotal)));

        document.add(table);
    }

    /**
     * 添加报销汇总
     */
    private void addReimbursementSummary(Document document, PdfFont font, ReimbursementCalcDTO calc) {
        Paragraph summaryTitle = new Paragraph("报销费用汇总")
                .setFont(font)
                .setFontSize(14)
                .setMarginTop(20)
                .setMarginBottom(10);
        document.add(summaryTitle);

        Table summaryTable = new Table(2).setWidth(300);

        summaryTable.addCell(createHeaderCell(font, "项目"));
        summaryTable.addCell(createHeaderCell(font, "金额（元）"));

        summaryTable.addCell(createDataCell(font, "医疗费用总计"));
        summaryTable.addCell(createDataCell(font, String.format("%.2f", calc.getTotalFee())));

        summaryTable.addCell(createDataCell(font, "起付线"));
        summaryTable.addCell(createDataCell(font,
                String.format("%.2f", calc.getDeductible() != null ? calc.getDeductible() : BigDecimal.ZERO)));

        summaryTable.addCell(createDataCell(font, "可报销金额"));
        summaryTable.addCell(createDataCell(font,
                String.format("%.2f", calc.getReimbursable() != null ? calc.getReimbursable() : BigDecimal.ZERO)));

        summaryTable.addCell(createDataCell(font, "报销比例"));
        summaryTable.addCell(createDataCell(font,
                String.format("%.1f%%", calc.getRatio() != null ? calc.getRatio() : BigDecimal.ZERO)));

        summaryTable.addCell(createHeaderCell(font, "医保报销金额"));
        summaryTable.addCell(createHeaderCell(font, String.format("%.2f", calc.getReimbursementAmount())));

        summaryTable.addCell(createDataCell(font, "个人自付金额"));
        summaryTable.addCell(createDataCell(font, String.format("%.2f", calc.getSelfPayAmount())));

        document.add(summaryTable);
    }

    /**
     * 添加页脚
     */
    private void addFooter(Document document, PdfFont font) {
        Paragraph footer = new Paragraph()
                .setFont(font)
                .setFontSize(10)
                .setMarginTop(30)
                .add(new Paragraph(
                        "报销时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"))))
                .add(new Paragraph("系统生成，仅供参考"));
        document.add(footer);
    }

    // 辅助方法
    private void addInfoRow(Table table, PdfFont font, String label1, String value1, String label2, String value2) {
        table.addCell(createHeaderCell(font, label1));
        table.addCell(createDataCell(font, value1 != null ? value1 : ""));
        table.addCell(createHeaderCell(font, label2));
        table.addCell(createDataCell(font, value2 != null ? value2 : ""));
    }

    private Cell createHeaderCell(PdfFont font, String text) {
        return new Cell()
                .setFont(font)
                .setFontSize(10)
                .add(new Paragraph(text));
    }

    private Cell createDataCell(PdfFont font, String text) {
        return new Cell()
                .setFont(font)
                .setFontSize(9)
                .add(new Paragraph(text != null ? text : ""));
    }

    private String getGenderText(Integer gender) {
        if (gender == null)
            return "未知";
        return gender == 1 ? "男" : gender == 0 ? "女" : "未知";
    }

    private String formatDate(LocalDateTime dateTime) {
        if (dateTime == null)
            return "";
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    private int calculateAge(LocalDate birthDate) {
        if (birthDate == null)
            return 0;
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    @Override
    public void cleanExpiredPdfFiles() {
        File directory = new File(pdfExportPath);
        if (!directory.exists())
            return;

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".pdf"));
        if (files == null)
            return;

        long expireTime = System.currentTimeMillis() - (expireHours * 60 * 60 * 1000L);

        for (File file : files) {
            if (file.lastModified() < expireTime) {
                file.delete();
            }
        }
    }

    /**
     * 创建支持中文的字体 - 简化版
     */
    private PdfFont createChineseFont() {
        try {
            // 优先使用STSong-Light字体，支持中文
            return PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H");
        } catch (Exception e) {
            System.err.println("中文字体加载失败，使用默认字体: " + e.getMessage());
            try {
                // 降级为标准字体
                return PdfFontFactory.createFont(StandardFonts.HELVETICA);
            } catch (Exception e2) {
                throw new RuntimeException("字体加载完全失败", e2);
            }
        }
    }
}
