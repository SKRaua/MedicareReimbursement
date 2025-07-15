package com.cqupt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.dto.MedicalServiceDTO;
import com.cqupt.pojo.MedicalService;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.MedicalServiceVo;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description MedicalServiceService
 */
public interface MedicalServiceService extends IService<MedicalService> {

    ResultVo<Page<MedicalServiceVo>> selectMedicalServiceVoPage(MedicalServiceDTO dto) throws Exception;

    ResultVo<Void> addMedicalService(MedicalServiceDTO dto) throws Exception;

    ResultVo<Void> updateMedicalService(MedicalServiceDTO dto) throws Exception;

    ResultVo<Void> removeMedicalService(Integer id) throws Exception;

}