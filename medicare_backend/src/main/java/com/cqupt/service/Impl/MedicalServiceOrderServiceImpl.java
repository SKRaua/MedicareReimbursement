package com.cqupt.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.dto.MedicalServiceOrderDTO;
import com.cqupt.mapper.MedicalServiceOrderMapper;
import com.cqupt.pojo.MedicalServiceOrder;
import com.cqupt.service.MedicalServiceOrderService;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.MedicalServiceOrderVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description 医疗记录Service实现类
 */
@Service
public class MedicalServiceOrderServiceImpl extends ServiceImpl<MedicalServiceOrderMapper, MedicalServiceOrder>
        implements MedicalServiceOrderService {

    @Autowired
    private MedicalServiceOrderMapper medicalServiceOrderMapper;

    @Override
    public ResultVo<Page<MedicalServiceOrderVo>> selectMedicalServiceOrderVoPage(
            MedicalServiceOrderDTO medicalServiceOrderDTO) throws Exception {
        Page<MedicalServiceOrderVo> page = new Page<>(medicalServiceOrderDTO.getPage(), 6);
        medicalServiceOrderMapper.selectMedicalServiceOrderVoPage(page, medicalServiceOrderDTO);
        return ResultVo.ok(page);
    }

    @Override
    public ResultVo<Void> addMedicalServiceOrder(MedicalServiceOrderDTO dto) throws Exception {
        MedicalServiceOrder order = new MedicalServiceOrder();
        BeanUtils.copyProperties(dto, order);
        int res = medicalServiceOrderMapper.insert(order);
        return res > 0 ? ResultVo.ok("添加成功") : ResultVo.fail("添加失败");
    }

    @Override
    public ResultVo<Void> updateMedicalServiceOrder(MedicalServiceOrderDTO dto) throws Exception {
        MedicalServiceOrder order = new MedicalServiceOrder();
        BeanUtils.copyProperties(dto, order);
        int res = medicalServiceOrderMapper.updateById(order);
        return res > 0 ? ResultVo.ok("修改成功") : ResultVo.fail("修改失败");
    }

    @Override
    public ResultVo<Void> removeMedicalServiceOrder(Integer id) throws Exception {
        int res = medicalServiceOrderMapper.deleteById(id);
        return res > 0 ? ResultVo.ok("删除成功") : ResultVo.fail("删除失败");
    }
}