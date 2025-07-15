package com.cqupt.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.cqupt.dto.TreatmentItemOrderDTO;
import com.cqupt.service.TreatmentItemOrderService;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.TreatmentItemOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zhou Xinyang
 * @date 2025/07/08
 * @description
 */
@RestController
@RequestMapping("/treatmentItemOrder")
@Api(tags = "诊疗项目")
@CrossOrigin
public class TreatmentItemOrderController {
    @Autowired
    private TreatmentItemOrderService treatmentItemOrderService;

    @ApiOperation("信息查询")
    @GetMapping("/info")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "page", value = "页码", required = true)
    })
    public ResultVo<Page<TreatmentItemOrderVo>> selectDrugOrderVoPage(TreatmentItemOrderDTO treatmentItemOrderDTO)
            throws Exception {
        return treatmentItemOrderService.selectTreatmentItemOrderVoPage(treatmentItemOrderDTO);
    }

    @ApiOperation("添加诊疗项目订单")
    @PostMapping("/add")
    public ResultVo<Void> addTreatmentItemOrder(@RequestBody TreatmentItemOrderDTO dto) throws Exception {
        return treatmentItemOrderService.addTreatmentItemOrder(dto);
    }

    @ApiOperation("修改诊疗项目订单")
    @PostMapping("/edit")
    public ResultVo<Void> updateTreatmentItemOrder(@RequestBody TreatmentItemOrderDTO dto) throws Exception {
        return treatmentItemOrderService.updateTreatmentItemOrder(dto);
    }

    @ApiOperation("删除诊疗项目订单")
    @PostMapping("/remove")
    public ResultVo<Void> removeTreatmentItemOrder(@RequestBody TreatmentItemOrderDTO dto) throws Exception {
        return treatmentItemOrderService.removeTreatmentItemOrder(dto.getId());
    }
}
