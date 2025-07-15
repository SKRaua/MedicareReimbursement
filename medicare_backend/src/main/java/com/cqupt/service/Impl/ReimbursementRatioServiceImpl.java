// ReimbursementRatioServiceImpl.java
package com.cqupt.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.dto.ReimbursementRatioDTO;
import com.cqupt.mapper.ReimbursementRatioMapper;
import com.cqupt.pojo.ReimbursementRatio;
import com.cqupt.service.ReimbursementRatioService;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.ReimbursementRatioVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description 报销比例维护Service实现类
 */
@Service
public class ReimbursementRatioServiceImpl extends ServiceImpl<ReimbursementRatioMapper, ReimbursementRatio>
        implements ReimbursementRatioService {

    @Autowired
    private ReimbursementRatioMapper ratioMapper;

    @Override
    public ResultVo<Page<ReimbursementRatioVo>> selectReimbursementRatioPage(ReimbursementRatioDTO ratioDTO)
            throws Exception {
        Page<ReimbursementRatioVo> page = new Page<>(ratioDTO.getPage(), 6);
        ratioMapper.selectReimbursementRatioPage(page, ratioDTO);
        return ResultVo.ok(page);
    }

    @Override
    public ResultVo<Void> addReimbursementRatio(ReimbursementRatioDTO dto) throws Exception {
        ReimbursementRatio ratio = new ReimbursementRatio();
        BeanUtils.copyProperties(dto, ratio);
        int res = ratioMapper.insert(ratio);
        return res > 0 ? ResultVo.ok("添加成功") : ResultVo.fail("添加失败");
    }

    @Override
    public ResultVo<Void> updateReimbursementRatio(ReimbursementRatioDTO dto) throws Exception {
        ReimbursementRatio ratio = new ReimbursementRatio();
        BeanUtils.copyProperties(dto, ratio);
        int res = ratioMapper.updateById(ratio);
        return res > 0 ? ResultVo.ok("修改成功") : ResultVo.fail("修改失败");
    }

    @Override
    public ResultVo<Void> removeReimbursementRatio(Integer id) throws Exception {
        int res = ratioMapper.deleteById(id);
        return res > 0 ? ResultVo.ok("删除成功") : ResultVo.fail("删除失败");
    }
}