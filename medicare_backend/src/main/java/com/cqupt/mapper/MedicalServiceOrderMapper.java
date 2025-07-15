package com.cqupt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.cqupt.dto.MedicalServiceOrderDTO;
import com.cqupt.pojo.MedicalServiceOrder;
import com.cqupt.vo.MedicalServiceOrderVo;

/**
 * @author Zhou Xinyang
 * @date 2025/07/01
 * @description MedicalOrderMapper
 */
public interface MedicalServiceOrderMapper extends BaseMapper<MedicalServiceOrder> {

        Page<MedicalServiceOrderVo> selectMedicalServiceOrderVoPage(Page<MedicalServiceOrderVo> page,
                        @Param("medicalServiceOrderDTO") MedicalServiceOrderDTO medicalServiceOrderDTO);

}