package com.cqupt.service;

import com.cqupt.dto.ReimbursementCalcDTO;
import com.cqupt.pojo.*;

import java.util.List;

/**
 * @author Zhou Xinyang
 * @date 2025/07/16
 * @description PDF导出服务接口
 */
public interface PdfExportService {

    /**
     * 生成报销记录PDF文件
     * 
     * @param insureder   投保人信息
     * @param calc        报销计算结果
     * @param drugAList   甲类药品列表
     * @param drugBList   乙类药品列表
     * @param drugCList   丙类药品列表
     * @param serviceList 医疗服务列表
     * @param itemList    诊疗项目列表
     * @return PDF文件路径
     */
    String generateReimbursementPdf(
            Insureder insureder,
            ReimbursementCalcDTO calc,
            List<DrugOrder> drugAList,
            List<DrugOrder> drugBList,
            List<DrugOrder> drugCList,
            List<MedicalServiceOrder> serviceList,
            List<TreatmentItemOrder> itemList) throws Exception;

    /**
     * 清理过期的PDF文件
     */
    void cleanExpiredPdfFiles();
}
