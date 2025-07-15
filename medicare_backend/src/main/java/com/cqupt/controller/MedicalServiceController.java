package com.cqupt.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.cqupt.dto.MedicalServiceDTO;
import com.cqupt.service.MedicalServiceService;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.MedicalServiceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicalService")
@Api(tags = "医疗服务")
@CrossOrigin
public class MedicalServiceController {
    @Autowired
    private MedicalServiceService medicalServiceService;

    @ApiOperation("分页查询")
    @GetMapping("/info")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "page", value = "页码", required = true)
    })
    public ResultVo<Page<MedicalServiceVo>> selectMedicalServiceVoPage(MedicalServiceDTO dto) throws Exception {
        return medicalServiceService.selectMedicalServiceVoPage(dto);
    }

    @ApiOperation("添加医疗服务")
    @PostMapping("/add")
    public ResultVo<Void> addMedicalService(@RequestBody MedicalServiceDTO dto) throws Exception {
        return medicalServiceService.addMedicalService(dto);
    }

    @ApiOperation("修改医疗服务")
    @PostMapping("/edit")
    public ResultVo<Void> updateMedicalService(@RequestBody MedicalServiceDTO dto) throws Exception {
        return medicalServiceService.updateMedicalService(dto);
    }

    @ApiOperation("删除医疗服务")
    @PostMapping("/remove")
    public ResultVo<Void> removeMedicalService(@RequestBody MedicalServiceDTO dto) throws Exception {
        return medicalServiceService.removeMedicalService(dto.getId());
    }
}