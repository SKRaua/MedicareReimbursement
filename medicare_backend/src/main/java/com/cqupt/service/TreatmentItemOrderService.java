package com.cqupt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.dto.TreatmentItemOrderDTO;
import com.cqupt.pojo.TreatmentItemOrder;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.TreatmentItemOrderVo;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description TreatmentItemOrderService
 */
public interface TreatmentItemOrderService extends IService<TreatmentItemOrder> {
    public ResultVo<Page<TreatmentItemOrderVo>> selectTreatmentItemOrderVoPage(
            TreatmentItemOrderDTO treatmentItemOrderDTO) throws Exception;

    ResultVo<Void> addTreatmentItemOrder(TreatmentItemOrderDTO dto) throws Exception;

    ResultVo<Void> updateTreatmentItemOrder(TreatmentItemOrderDTO dto) throws Exception;

    ResultVo<Void> removeTreatmentItemOrder(Integer id) throws Exception;
}