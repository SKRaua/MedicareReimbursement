package com.cqupt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.dto.TreatmentItemDTO;
import com.cqupt.pojo.TreatmentItem;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.TreatmentItemVo;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description TreatmentItemService
 */
public interface TreatmentItemService extends IService<TreatmentItem> {
    ResultVo<Page<TreatmentItemVo>> selectTreatmentItemVoPage(TreatmentItemDTO dto) throws Exception;

    ResultVo<Void> addTreatmentItem(TreatmentItemDTO dto) throws Exception;

    ResultVo<Void> updateTreatmentItem(TreatmentItemDTO dto) throws Exception;

    ResultVo<Void> removeTreatmentItem(Integer id) throws Exception;
}