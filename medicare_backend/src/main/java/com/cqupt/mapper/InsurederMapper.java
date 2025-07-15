package com.cqupt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.cqupt.dto.InsurederDTO;
import com.cqupt.pojo.Insureder;
import com.cqupt.vo.InsurederVo;

/**
 * @author Zhou Xinyang
 * @date 2025/07/08
 * @description InsurederMapper
 */
public interface InsurederMapper extends BaseMapper<Insureder> {

    Page<InsurederVo> selectPatientPage(@Param("page") Page<InsurederVo> page,
            @Param("insurederDTO") InsurederDTO insurederDTO) throws Exception;
}
