package com.cqupt.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.cqupt.dto.DiagnosisDTO;
import com.cqupt.service.DiagnosisService;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.DiagnosisVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/diagnosis")
@Api(tags = "诊断信息")
@CrossOrigin
public class DiagnosisController {
    @Autowired
    private DiagnosisService diagnosisService;

    @ApiOperation("诊断分页查询")
    @GetMapping("/info")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "page", value = "页码", required = true)
    })
    public ResultVo<Page<DiagnosisVo>> selectDiagnosisVoPage(DiagnosisDTO diagnosisDTO) throws Exception {
        return diagnosisService.selectDiagnosisVoPage(diagnosisDTO);
    }

    @ApiOperation("添加诊断")
    @PostMapping("/add")
    public ResultVo<Void> addDiagnosis(@RequestBody DiagnosisDTO diagnosisDTO) throws Exception {
        return diagnosisService.addDiagnosis(diagnosisDTO);
    }

    @ApiOperation("修改诊断")
    @PostMapping("/edit")
    public ResultVo<Void> updateDiagnosis(@RequestBody DiagnosisDTO diagnosisDTO) throws Exception {
        return diagnosisService.updateDiagnosis(diagnosisDTO);
    }

    @ApiOperation("删除诊断")
    @ApiImplicitParam(dataType = "int", name = "id", value = "诊断ID", required = true)
    @PostMapping("/remove")
    public ResultVo<Void> removeDiagnosis(@RequestBody DiagnosisDTO diagnosisDTO) throws Exception {
        return diagnosisService.removeDiagnosis(diagnosisDTO.getId());
    }
}