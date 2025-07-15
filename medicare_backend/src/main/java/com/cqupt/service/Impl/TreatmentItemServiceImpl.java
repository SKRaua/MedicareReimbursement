package com.cqupt.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.dto.TreatmentItemDTO;
import com.cqupt.mapper.TreatmentItemMapper;
import com.cqupt.pojo.TreatmentItem;
import com.cqupt.service.TreatmentItemService;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.TreatmentItemVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description TreatmentItemServiceImpl
 */
@Service
public class TreatmentItemServiceImpl extends ServiceImpl<TreatmentItemMapper, TreatmentItem>
                implements TreatmentItemService {

        @Autowired
        private TreatmentItemMapper treatmentItemMapper;

        @Override
        public ResultVo<Page<TreatmentItemVo>> selectTreatmentItemVoPage(TreatmentItemDTO dto) throws Exception {
                Page<TreatmentItemVo> page = new Page<>(dto.getPage(), 6);
                treatmentItemMapper.selectTreatmentItemVoPage(page, dto);
                return ResultVo.ok(page);
        }

        @Override
        public ResultVo<Void> addTreatmentItem(TreatmentItemDTO dto) throws Exception {
                TreatmentItem item = new TreatmentItem();
                BeanUtils.copyProperties(dto, item);
                int res = treatmentItemMapper.insert(item);
                return res > 0 ? ResultVo.ok("添加成功") : ResultVo.fail("添加失败");
        }

        @Override
        public ResultVo<Void> updateTreatmentItem(TreatmentItemDTO dto) throws Exception {
                TreatmentItem item = new TreatmentItem();
                BeanUtils.copyProperties(dto, item);
                int res = treatmentItemMapper.updateById(item);
                return res > 0 ? ResultVo.ok("修改成功") : ResultVo.fail("修改失败");
        }

        @Override
        public ResultVo<Void> removeTreatmentItem(Integer id) throws Exception {
                int res = treatmentItemMapper.deleteById(id);
                return res > 0 ? ResultVo.ok("删除成功") : ResultVo.fail("删除失败");
        }
}