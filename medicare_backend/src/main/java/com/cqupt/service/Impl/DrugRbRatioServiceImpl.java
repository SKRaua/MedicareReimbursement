// DrugRbRatioServiceImpl.java
package com.cqupt.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.dto.DrugRbRatioDTO;
import com.cqupt.mapper.DrugRbRatioMapper;
import com.cqupt.pojo.DrugRbRatio;
import com.cqupt.service.DrugRbRatioService;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.DrugRbRatioVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description 药品报销比例维护Service实现类
 */
@Service
public class DrugRbRatioServiceImpl extends ServiceImpl<DrugRbRatioMapper, DrugRbRatio> implements DrugRbRatioService {
    @Autowired
    private DrugRbRatioMapper drugRbRatioMapper;

    @Override
    public ResultVo<Page<DrugRbRatioVo>> selectDrugRbRatioVoPage(DrugRbRatioDTO dto) throws Exception {
        Page<DrugRbRatioVo> page = new Page<>(dto.getPage(), 6);
        drugRbRatioMapper.selectDrugRbRatioVoPage(page, dto);
        return ResultVo.ok(page);
    }

    @Override
    public List<DrugRbRatioVo> getEnabledDrugRbRatios() {
        List<String> types = Arrays.asList("甲类", "乙类", "丙类");
        List<DrugRbRatioVo> result = new ArrayList<>();
        for (String type : types) {
            QueryWrapper<DrugRbRatio> qw = new QueryWrapper<>();
            qw.eq("drug_type", type).eq("status", "启用").orderByDesc("effective_date");
            DrugRbRatio ratio = drugRbRatioMapper.selectOne(qw.last("limit 1"));
            if (ratio != null) {
                DrugRbRatioVo vo = new DrugRbRatioVo();
                BeanUtils.copyProperties(ratio, vo);
                result.add(vo);
            }
        }
        return result;
    }

    @Override
    public ResultVo<Void> addDrugRbRatio(DrugRbRatioDTO dto) throws Exception {
        // 校验：同类型药品只能有一条启用
        if ("启用".equals(dto.getStatus())) {
            QueryWrapper<DrugRbRatio> qw = new QueryWrapper<>();
            qw.eq("drug_type", dto.getDrugType()).eq("status", "启用");
            int enabledCount = drugRbRatioMapper.selectCount(qw);
            if (enabledCount > 0) {
                return ResultVo.fail("该药品类型已有启用比例，请先停用后再启用新比例");
            }
        }
        DrugRbRatio ratio = new DrugRbRatio();
        BeanUtils.copyProperties(dto, ratio);
        int res = drugRbRatioMapper.insert(ratio);
        return res > 0 ? ResultVo.ok("添加成功") : ResultVo.fail("添加失败");
    }

    @Override
    public ResultVo<Void> updateDrugRbRatio(DrugRbRatioDTO dto) throws Exception {
        // 校验：同类型药品只能有一条启用
        if ("启用".equals(dto.getStatus())) {
            QueryWrapper<DrugRbRatio> qw = new QueryWrapper<>();
            qw.eq("drug_type", dto.getDrugType()).eq("status", "启用").ne("id", dto.getId());
            int enabledCount = drugRbRatioMapper.selectCount(qw);
            if (enabledCount > 0) {
                return ResultVo.fail("该药品类型已有启用比例，请先停用后再启用新比例");
            }
        }
        DrugRbRatio ratio = new DrugRbRatio();
        BeanUtils.copyProperties(dto, ratio);
        int res = drugRbRatioMapper.updateById(ratio);
        return res > 0 ? ResultVo.ok("修改成功") : ResultVo.fail("修改失败");
    }

    @Override
    public ResultVo<Void> removeDrugRbRatio(Integer id) throws Exception {
        // 校验：删除后不能导致某类型药品无启用
        DrugRbRatio toDelete = drugRbRatioMapper.selectById(id);
        if (toDelete != null && "启用".equals(toDelete.getStatus())) {
            QueryWrapper<DrugRbRatio> qw = new QueryWrapper<>();
            qw.eq("drug_type", toDelete.getDrugType()).eq("status", "启用");
            int enabledCount = drugRbRatioMapper.selectCount(qw);
            if (enabledCount == 1) {
                return ResultVo.fail("每种药品类型必须至少有一条启用比例，不能全部停用或删除");
            }
        }
        int res = drugRbRatioMapper.deleteById(id);
        return res > 0 ? ResultVo.ok("删除成功") : ResultVo.fail("删除失败");
    }
}