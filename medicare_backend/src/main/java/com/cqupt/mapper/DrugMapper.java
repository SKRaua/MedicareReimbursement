package com.cqupt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.cqupt.dto.DrugDTO;
import com.cqupt.pojo.Drug;
import com.cqupt.vo.DrugVo;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description DrugMapper
 */
public interface DrugMapper extends BaseMapper<Drug> {
    Page<DrugVo> selectDrugVoPage(Page<DrugVo> page, @Param("drugDTO") DrugDTO drugDTO);
}