// DiagnosisService.java
package com.cqupt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.dto.DiagnosisDTO;
import com.cqupt.pojo.Diagnosis;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.DiagnosisVo;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description 诊断表Service
 */
public interface DiagnosisService extends IService<Diagnosis> {
    ResultVo<Page<DiagnosisVo>> selectDiagnosisVoPage(DiagnosisDTO diagnosisDTO) throws Exception;

    ResultVo<Void> addDiagnosis(DiagnosisDTO diagnosisDTO) throws Exception;

    ResultVo<Void> updateDiagnosis(DiagnosisDTO diagnosisDTO) throws Exception;

    ResultVo<Void> removeDiagnosis(Integer id) throws Exception;
}