package com.cqupt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.cqupt.dto.DrugOrderDTO;
import com.cqupt.pojo.DrugOrder;
import com.cqupt.vo.DrugOrderVo;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description DrugOrder的Mapper接口
 */
public interface DrugOrderMapper extends BaseMapper<DrugOrder> {

    /**
     * 按投保人/病人的id分页查询DrugOrderVo
     * 
     * @param page         分页参数
     * @param id           投保人/病人的id
     * @param drugOrderDTO 药品订单dto
     * @return 分页结果
     */
    Page<DrugOrderVo> selectDrugOrderVoPage(Page<DrugOrderVo> page, @Param("drugOrderDTO") DrugOrderDTO drugOrderDTO);
}