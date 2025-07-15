package com.cqupt.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.dto.DrugOrderDTO;
import com.cqupt.mapper.DrugOrderMapper;
import com.cqupt.pojo.DrugOrder;
import com.cqupt.service.DrugOrderService;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.DrugOrderVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description DrugOrder的Service实现类
 */
@Service
public class DrugOrderServiceImpl extends ServiceImpl<DrugOrderMapper, DrugOrder> implements DrugOrderService {

    @Autowired
    private DrugOrderMapper drugOrderMapper;

    @Override
    public ResultVo<Page<DrugOrderVo>> selectDrugOrderVoPage(DrugOrderDTO drugOrderDTO) throws Exception {
        Page<DrugOrderVo> page = new Page<>(drugOrderDTO.getPage(), 6);
        drugOrderMapper.selectDrugOrderVoPage(page, drugOrderDTO);
        return ResultVo.ok(page);
    }

    @Override
    public ResultVo<Void> addDrugOrder(DrugOrderDTO drugOrderDTO) throws Exception {
        DrugOrder order = new DrugOrder();
        BeanUtils.copyProperties(drugOrderDTO, order);
        int res = drugOrderMapper.insert(order);
        return res > 0 ? ResultVo.ok("添加成功") : ResultVo.fail("添加失败");
    }

    @Override
    public ResultVo<Void> updateDrugOrder(DrugOrderDTO drugOrderDTO) throws Exception {
        DrugOrder order = new DrugOrder();
        BeanUtils.copyProperties(drugOrderDTO, order);
        int res = drugOrderMapper.updateById(order);
        return res > 0 ? ResultVo.ok("修改成功") : ResultVo.fail("修改失败");
    }

    @Override
    public ResultVo<Void> removeDrugOrder(Integer id) throws Exception {
        int res = drugOrderMapper.deleteById(id);
        return res > 0 ? ResultVo.ok("删除成功") : ResultVo.fail("删除失败");
    }

}