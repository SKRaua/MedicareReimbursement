// ReimbursementRatioMapper.java
package com.cqupt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.cqupt.dto.ReimbursementRatioDTO;
import com.cqupt.pojo.ReimbursementRatio;
import com.cqupt.vo.InsurederVo;
import com.cqupt.vo.ReimbursementRatioVo;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description 报销比例维护Mapper
 */
public interface ReimbursementRatioMapper extends BaseMapper<ReimbursementRatio> {

    Page<ReimbursementRatioVo> selectReimbursementRatioPage(@Param("page") Page<ReimbursementRatioVo> page,
            @Param("ratioDTO") ReimbursementRatioDTO ratioDTO) throws Exception;
}