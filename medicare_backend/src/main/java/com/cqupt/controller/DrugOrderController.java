package com.cqupt.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.cqupt.dto.DrugOrderDTO;
import com.cqupt.service.DrugOrderService;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.DrugOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zhou Xinyang
 * @date 2025/07/08
 * @description
 */
@RestController
@RequestMapping("/drugOrder")
@Api(tags = "药品订单")
@CrossOrigin
public class DrugOrderController {
    @Autowired
    private DrugOrderService drugOrderService;

    @ApiOperation("开药记录分页查询")
    @GetMapping("/info")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "page", value = "页码", required = true)
    })
    public ResultVo<Page<DrugOrderVo>> selectDrugOrderVoPage(DrugOrderDTO drugOrderDTO) throws Exception {
        return drugOrderService.selectDrugOrderVoPage(drugOrderDTO);
    }

    @ApiOperation("添加开药记录")
    @PostMapping("/add")
    public ResultVo<Void> addDrugOrder(@RequestBody DrugOrderDTO drugOrderDTO) throws Exception {
        return drugOrderService.addDrugOrder(drugOrderDTO);
    }

    @ApiOperation("修改开药记录")
    @PostMapping("/edit")
    public ResultVo<Void> updateDrugOrder(@RequestBody DrugOrderDTO drugOrderDTO) throws Exception {
        return drugOrderService.updateDrugOrder(drugOrderDTO);
    }

    @ApiOperation("删除开药记录")
    @PostMapping("/remove")
    public ResultVo<Void> removeDrugOrder(@RequestBody DrugOrderDTO drugOrderDTO) throws Exception {
        return drugOrderService.removeDrugOrder(drugOrderDTO.getId());
    }
}
