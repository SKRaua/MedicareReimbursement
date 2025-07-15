package com.cqupt.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.cqupt.dto.DiseaseDTO;
import com.cqupt.pojo.Disease;
import com.cqupt.service.DiseaseService;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.DiseaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zhou Xinyang
 * @date 2025/07/10
 * @description 疾病Controller
 */
@RestController
@RequestMapping("/disease")
@Api(tags = "疾病信息")
@CrossOrigin
public class DiseaseController {
    @Autowired
    private DiseaseService diseaseService;

    @ApiOperation("疾病分页查询")
    @GetMapping("/info")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "page", value = "页码", required = true)
    })
    public ResultVo<Page<DiseaseVo>> selectDiseaseVoPage(DiseaseDTO diseaseDTO) throws Exception {
        return diseaseService.selectDiseaseVoPage(diseaseDTO);
    }

    @ApiOperation("新增疾病")
    @PostMapping("/add")
    public ResultVo<Void> addDisease(@RequestBody Disease disease) {
        return diseaseService.addDisease(disease);
    }

    @ApiOperation("修改疾病")
    @PostMapping("/update")
    public ResultVo<Void> updateDisease(@RequestBody Disease disease) {
        return diseaseService.updateDisease(disease);
    }

    @ApiOperation("删除疾病")
    @PostMapping("/delete")
    public ResultVo<Void> deleteDisease(@RequestBody Disease disease) {
        return diseaseService.deleteDisease(disease.getId());
    }
}