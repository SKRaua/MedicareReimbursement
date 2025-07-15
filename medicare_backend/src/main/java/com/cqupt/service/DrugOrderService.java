package com.cqupt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.dto.DrugOrderDTO;
import com.cqupt.pojo.DrugOrder;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.DrugOrderVo;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description DrugOrder的Service接口
 */
public interface DrugOrderService extends IService<DrugOrder> {

    public ResultVo<Page<DrugOrderVo>> selectDrugOrderVoPage(DrugOrderDTO drugOrderDTO) throws Exception;

    ResultVo<Void> addDrugOrder(DrugOrderDTO drugOrderDTO) throws Exception;

    ResultVo<Void> updateDrugOrder(DrugOrderDTO drugOrderDTO) throws Exception;

    ResultVo<Void> removeDrugOrder(Integer id) throws Exception;

}