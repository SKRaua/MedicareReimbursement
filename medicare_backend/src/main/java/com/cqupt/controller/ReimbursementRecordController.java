package com.cqupt.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.cqupt.dto.ReimbursementCalcDTO;
import com.cqupt.dto.ReimbursementRecordDTO;
import com.cqupt.service.ReimbursementRecordService;
import com.cqupt.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zhou Xinyang
 * @date 2025/07/08
 * @description
 */
@RestController
@RequestMapping("/reimbursementRecord")
@Api(tags = "报销记录")
@CrossOrigin
public class ReimbursementRecordController {
    @Autowired
    private ReimbursementRecordService rbRecordService;

    // @ApiOperation("信息查询")
    // @GetMapping("/info")
    // @ApiImplicitParams({
    // @ApiImplicitParam(dataType = "int", name = "page", value = "页码", required =
    // true)
    // })
    // public ResultVo<Page<MedicalServiceOrderVo>>
    // selectDrugOrderVoPage(MedicalServiceOrderDTO medicalServiceOrderDTO)
    // throws Exception {
    // return
    // medicalServiceOrderService.selectMedicalServiceOrderVoPage(medicalServiceOrderDTO);
    // }

    @ApiOperation("计算报销信息")
    @GetMapping("/calculateRb")
    @ApiImplicitParams({
    })
    public ResultVo<ReimbursementCalcDTO> selectDrugOrderVoPage(ReimbursementRecordDTO recordDTO)
            throws Exception {
        return rbRecordService.calculateReimbursement(recordDTO.getPatientId());
    }

    @ApiOperation("添加报销记录")
    @PostMapping("/confirmRb")
    @ApiImplicitParams({ @ApiImplicitParam(dataType = "int", name = "patientId", value = "患者id", required = true) })
    public ResultVo<Void> confirmReimbursement(@RequestBody ReimbursementRecordDTO recordDTO)
            throws Exception {
        return rbRecordService.confirmReimbursement(recordDTO);
    }

}
