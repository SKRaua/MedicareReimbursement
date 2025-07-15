// DiseaseMapper.java
package com.cqupt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.cqupt.dto.DiseaseDTO;
import com.cqupt.pojo.Disease;
import com.cqupt.vo.DiseaseVo;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description 疾病维护表Mapper
 */
public interface DiseaseMapper extends BaseMapper<Disease> {
    Page<DiseaseVo> selectDiseaseVoPage(Page<DiseaseVo> page, @Param("diseaseDTO") DiseaseDTO diseaseDTO);
}