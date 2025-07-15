package com.cqupt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.dto.DrugDTO;
import com.cqupt.pojo.Drug;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.DrugVo;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description DrugService
 */
public interface DrugService extends IService<Drug> {
    ResultVo<Page<DrugVo>> selectDrugVoPage(DrugDTO drugDTO) throws Exception;

    ResultVo<Void> addDrug(DrugDTO dto) throws Exception;

    ResultVo<Void> updateDrug(DrugDTO dto) throws Exception;

    ResultVo<Void> removeDrug(Integer id) throws Exception;

}