package com.cqupt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.cqupt.dto.MedicalServiceDTO;
import com.cqupt.pojo.MedicalService;
import com.cqupt.vo.MedicalServiceVo;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description MedicalServiceMapper
 */
public interface MedicalServiceMapper extends BaseMapper<MedicalService> {
    Page<MedicalServiceVo> selectMedicalServiceVoPage(Page<MedicalServiceVo> page,
            @Param("medicalServiceDTO") MedicalServiceDTO dto);
}