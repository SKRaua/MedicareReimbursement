// MedicalOrderService.java
package com.cqupt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.dto.MedicalServiceOrderDTO;
import com.cqupt.pojo.MedicalServiceOrder;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.MedicalServiceOrderVo;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description 医疗记录Service
 */
public interface MedicalServiceOrderService extends IService<MedicalServiceOrder> {

    public ResultVo<Page<MedicalServiceOrderVo>> selectMedicalServiceOrderVoPage(
            MedicalServiceOrderDTO medicalServiceOrderDTO) throws Exception;

    ResultVo<Void> addMedicalServiceOrder(MedicalServiceOrderDTO dto) throws Exception;

    ResultVo<Void> updateMedicalServiceOrder(MedicalServiceOrderDTO dto) throws Exception;

    ResultVo<Void> removeMedicalServiceOrder(Integer id) throws Exception;

}