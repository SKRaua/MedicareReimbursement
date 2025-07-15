package com.cqupt.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.dto.DiagnosisDTO;
import com.cqupt.mapper.DiagnosisMapper;
import com.cqupt.pojo.Diagnosis;
import com.cqupt.service.DiagnosisService;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.DiagnosisVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description 诊断表Service实现类
 */
@Service
public class DiagnosisServiceImpl extends ServiceImpl<DiagnosisMapper, Diagnosis> implements DiagnosisService {
    @Autowired
    private DiagnosisMapper diagnosisMapper;

    @Override
    public ResultVo<Page<DiagnosisVo>> selectDiagnosisVoPage(DiagnosisDTO diagnosisDTO) throws Exception {
        Page<DiagnosisVo> page = new Page<>(diagnosisDTO.getPage(), 5);
        diagnosisMapper.selectDiagnosisVoPage(page, diagnosisDTO);
        return ResultVo.ok(page);
    }

    @Override
    public ResultVo<Void> addDiagnosis(DiagnosisDTO diagnosisDTO) throws Exception {
        Diagnosis diagnosis = new Diagnosis();
        BeanUtils.copyProperties(diagnosisDTO, diagnosis);
        int res = diagnosisMapper.insert(diagnosis);
        return res > 0 ? ResultVo.ok("添加成功") : ResultVo.fail("添加失败");
    }

    @Override
    public ResultVo<Void> updateDiagnosis(DiagnosisDTO diagnosisDTO) throws Exception {
        Diagnosis diagnosis = new Diagnosis();
        BeanUtils.copyProperties(diagnosisDTO, diagnosis);
        int res = diagnosisMapper.updateById(diagnosis);
        return res > 0 ? ResultVo.ok("修改成功") : ResultVo.fail("修改失败");
    }

    @Override
    public ResultVo<Void> removeDiagnosis(Integer id) throws Exception {
        int res = this.baseMapper.deleteById(id);
        return res > 0 ? ResultVo.ok("删除成功") : ResultVo.fail("删除失败");
    }

}