package com.cqupt.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.cqupt.dto.TreatmentItemDTO;
import com.cqupt.service.TreatmentItemService;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.TreatmentItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/treatmentItem")
@Api(tags = "诊疗项目")
@CrossOrigin
public class TreatmentItemController {
    @Autowired
    private TreatmentItemService treatmentItemService;

    @ApiOperation("分页查询")
    @GetMapping("/info")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "page", value = "页码", required = true)
    })
    public ResultVo<Page<TreatmentItemVo>> selectTreatmentItemVoPage(TreatmentItemDTO dto) throws Exception {
        return treatmentItemService.selectTreatmentItemVoPage(dto);
    }

    @ApiOperation("添加诊疗项目")
    @PostMapping("/add")
    public ResultVo<Void> addTreatmentItem(@RequestBody TreatmentItemDTO dto) throws Exception {
        return treatmentItemService.addTreatmentItem(dto);
    }

    @ApiOperation("修改诊疗项目")
    @PostMapping("/edit")
    public ResultVo<Void> updateTreatmentItem(@RequestBody TreatmentItemDTO dto) throws Exception {
        return treatmentItemService.updateTreatmentItem(dto);
    }

    @ApiOperation("删除诊疗项目")
    @PostMapping("/remove")
    public ResultVo<Void> removeTreatmentItem(@RequestBody TreatmentItemDTO dto) throws Exception {
        return treatmentItemService.removeTreatmentItem(dto.getId());
    }
}