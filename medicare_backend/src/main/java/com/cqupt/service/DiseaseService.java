// DiseaseService.java
package com.cqupt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.dto.DiseaseDTO;
import com.cqupt.pojo.Disease;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.DiseaseVo;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description 疾病维护表Service
 */
public interface DiseaseService extends IService<Disease> {
    ResultVo<Page<DiseaseVo>> selectDiseaseVoPage(DiseaseDTO diseaseDTO) throws Exception;

    ResultVo<Void> addDisease(Disease disease);

    ResultVo<Void> updateDisease(Disease disease);

    ResultVo<Void> deleteDisease(Integer id);
}