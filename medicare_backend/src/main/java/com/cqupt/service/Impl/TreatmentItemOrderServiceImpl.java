package com.cqupt.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.dto.TreatmentItemOrderDTO;
import com.cqupt.mapper.TreatmentItemOrderMapper;
import com.cqupt.pojo.TreatmentItemOrder;
import com.cqupt.service.TreatmentItemOrderService;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.TreatmentItemOrderVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description 医疗记录Service实现类
 */
@Service
public class TreatmentItemOrderServiceImpl extends ServiceImpl<TreatmentItemOrderMapper, TreatmentItemOrder>
        implements TreatmentItemOrderService {

    @Autowired
    private TreatmentItemOrderMapper treatmentItemOrderMapper;

    @Override
    public ResultVo<Page<TreatmentItemOrderVo>> selectTreatmentItemOrderVoPage(
            TreatmentItemOrderDTO treatmentItemOrderDTO) throws Exception {
        Page<TreatmentItemOrderVo> page = new Page<>(treatmentItemOrderDTO.getPage(), 6);
        treatmentItemOrderMapper.selectTreatmentItemOrderVoPage(page, treatmentItemOrderDTO);
        return ResultVo.ok(page);
    }

    @Override
    public ResultVo<Void> addTreatmentItemOrder(TreatmentItemOrderDTO dto) throws Exception {
        TreatmentItemOrder order = new TreatmentItemOrder();
        BeanUtils.copyProperties(dto, order);
        int res = treatmentItemOrderMapper.insert(order);
        return res > 0 ? ResultVo.ok("添加成功") : ResultVo.fail("添加失败");
    }

    @Override
    public ResultVo<Void> updateTreatmentItemOrder(TreatmentItemOrderDTO dto) throws Exception {
        TreatmentItemOrder order = new TreatmentItemOrder();
        BeanUtils.copyProperties(dto, order);
        int res = treatmentItemOrderMapper.updateById(order);
        return res > 0 ? ResultVo.ok("修改成功") : ResultVo.fail("修改失败");
    }

    @Override
    public ResultVo<Void> removeTreatmentItemOrder(Integer id) throws Exception {
        int res = treatmentItemOrderMapper.deleteById(id);
        return res > 0 ? ResultVo.ok("删除成功") : ResultVo.fail("删除失败");
    }
}