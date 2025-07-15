package com.cqupt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.cqupt.dto.TreatmentItemDTO;
import com.cqupt.pojo.TreatmentItem;
import com.cqupt.vo.TreatmentItemVo;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description TreatmentItemMapper
 */
public interface TreatmentItemMapper extends BaseMapper<TreatmentItem> {
    Page<TreatmentItemVo> selectTreatmentItemVoPage(Page<TreatmentItemVo> page,
            @Param("treatmentItemDTO") TreatmentItemDTO dto);
}