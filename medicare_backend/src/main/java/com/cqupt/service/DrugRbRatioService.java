// DrugRbRatioService.java
package com.cqupt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.dto.DrugRbRatioDTO;
import com.cqupt.pojo.DrugRbRatio;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.DrugRbRatioVo;

import java.util.List;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description 药品报销比例维护Service
 */
public interface DrugRbRatioService extends IService<DrugRbRatio> {
    ResultVo<Page<DrugRbRatioVo>> selectDrugRbRatioVoPage(DrugRbRatioDTO dto) throws Exception;

    List<DrugRbRatioVo> getEnabledDrugRbRatios();

    ResultVo<Void> addDrugRbRatio(DrugRbRatioDTO dto) throws Exception;

    ResultVo<Void> updateDrugRbRatio(DrugRbRatioDTO dto) throws Exception;

    ResultVo<Void> removeDrugRbRatio(Integer id) throws Exception;
}