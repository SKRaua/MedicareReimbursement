// DiseaseServiceImpl.java
package com.cqupt.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.dto.DiseaseDTO;
import com.cqupt.mapper.DiseaseMapper;
import com.cqupt.pojo.Disease;
import com.cqupt.service.DiseaseService;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.DiseaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description 疾病维护表Service实现类
 */
@Service
public class DiseaseServiceImpl extends ServiceImpl<DiseaseMapper, Disease> implements DiseaseService {
    @Autowired
    private DiseaseMapper diseaseMapper;

    @Override
    public ResultVo<Page<DiseaseVo>> selectDiseaseVoPage(DiseaseDTO diseaseDTO) throws Exception {
        Page<DiseaseVo> page = new Page<>(diseaseDTO.getPage(), 6);
        diseaseMapper.selectDiseaseVoPage(page, diseaseDTO);
        return ResultVo.ok(page);
    }

    @Override
    public ResultVo<Void> addDisease(Disease disease) {
        boolean res = this.save(disease);
        return res ? ResultVo.ok("新增成功") : ResultVo.fail("新增失败");
    }

    @Override
    public ResultVo<Void> updateDisease(Disease disease) {
        boolean res = this.updateById(disease);
        return res ? ResultVo.ok("修改成功") : ResultVo.fail("修改失败");
    }

    @Override
    public ResultVo<Void> deleteDisease(Integer id) {
        boolean res = this.removeById(id);
        return res ? ResultVo.ok("删除成功") : ResultVo.fail("删除失败");
    }
}