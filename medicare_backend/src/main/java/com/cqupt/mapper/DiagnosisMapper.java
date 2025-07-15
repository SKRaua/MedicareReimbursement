// DiagnosisMapper.java
package com.cqupt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.cqupt.dto.DiagnosisDTO;
import com.cqupt.pojo.Diagnosis;
import com.cqupt.vo.DiagnosisVo;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description 诊断表Mapper
 */
public interface DiagnosisMapper extends BaseMapper<Diagnosis> {
    Page<DiagnosisVo> selectDiagnosisVoPage(Page<DiagnosisVo> page, @Param("diagnosisDTO") DiagnosisDTO diagnosisDTO);
}