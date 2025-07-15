package com.cqupt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.cqupt.dto.DrugRbRatioDTO;
import com.cqupt.pojo.DrugRbRatio;
import com.cqupt.vo.DrugRbRatioVo;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description 药品报销比例维护Mapper
 */
public interface DrugRbRatioMapper extends BaseMapper<DrugRbRatio> {
    Page<DrugRbRatioVo> selectDrugRbRatioVoPage(Page<DrugRbRatioVo> page, @Param("dto") DrugRbRatioDTO dto);
}