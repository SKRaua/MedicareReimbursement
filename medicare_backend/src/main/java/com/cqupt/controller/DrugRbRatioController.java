package com.cqupt.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.cqupt.dto.DrugRbRatioDTO;
import com.cqupt.service.DrugRbRatioService;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.DrugRbRatioVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drugReimbursementRatio")
@Api(tags = "药品报销比例")
@CrossOrigin
public class DrugRbRatioController {
    @Autowired
    private DrugRbRatioService drugRbRatioService;

    @ApiOperation("分页查询")
    @GetMapping("/info")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "page", value = "页码", required = true)
    })
    public ResultVo<Page<DrugRbRatioVo>> selectDrugRbRatioVoPage(DrugRbRatioDTO dto) throws Exception {
        return drugRbRatioService.selectDrugRbRatioVoPage(dto);
    }

    @GetMapping("/enabledList")
    @ApiOperation("获取正在生效的三种药品类型的报销比例")
    public ResultVo<List<DrugRbRatioVo>> getEnabledDrugRbRatios() {
        List<DrugRbRatioVo> list = drugRbRatioService.getEnabledDrugRbRatios();
        return ResultVo.ok(list);
    }

    @ApiOperation("添加药品报销比例")
    @PostMapping("/add")
    public ResultVo<Void> addDrugRbRatio(@RequestBody DrugRbRatioDTO dto) throws Exception {
        return drugRbRatioService.addDrugRbRatio(dto);
    }

    @ApiOperation("修改药品报销比例")
    @PostMapping("/edit")
    public ResultVo<Void> updateDrugRbRatio(@RequestBody DrugRbRatioDTO dto) throws Exception {
        return drugRbRatioService.updateDrugRbRatio(dto);
    }

    @ApiOperation("删除药品报销比例")
    @PostMapping("/remove")
    public ResultVo<Void> removeDrugRbRatio(@RequestBody DrugRbRatioDTO dto) throws Exception {
        return drugRbRatioService.removeDrugRbRatio(dto.getId());
    }
}