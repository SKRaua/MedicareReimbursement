package com.cqupt.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.dto.MedicalServiceDTO;
import com.cqupt.mapper.MedicalServiceMapper;
import com.cqupt.pojo.MedicalService;
import com.cqupt.service.MedicalServiceService;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.MedicalServiceVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description MedicalServiceServiceImpl
 */
@Service
public class MedicalServiceServiceImpl extends ServiceImpl<MedicalServiceMapper, MedicalService>
        implements MedicalServiceService {
    @Autowired
    private MedicalServiceMapper medicalServiceMapper;

    @Override
    public ResultVo<Page<MedicalServiceVo>> selectMedicalServiceVoPage(MedicalServiceDTO dto) throws Exception {
        Page<MedicalServiceVo> page = new Page<>(dto.getPage(), 6);
        medicalServiceMapper.selectMedicalServiceVoPage(page, dto);
        return ResultVo.ok(page);
    }

    @Override
    public ResultVo<Void> addMedicalService(MedicalServiceDTO dto) throws Exception {
        MedicalService service = new MedicalService();
        BeanUtils.copyProperties(dto, service);
        int res = medicalServiceMapper.insert(service);
        return res > 0 ? ResultVo.ok("添加成功") : ResultVo.fail("添加失败");
    }

    @Override
    public ResultVo<Void> updateMedicalService(MedicalServiceDTO dto) throws Exception {
        MedicalService service = new MedicalService();
        BeanUtils.copyProperties(dto, service);
        int res = medicalServiceMapper.updateById(service);
        return res > 0 ? ResultVo.ok("修改成功") : ResultVo.fail("修改失败");
    }

    @Override
    public ResultVo<Void> removeMedicalService(Integer id) throws Exception {
        int res = medicalServiceMapper.deleteById(id);
        return res > 0 ? ResultVo.ok("删除成功") : ResultVo.fail("删除失败");
    }
}