package com.cqupt.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.cqupt.annotation.RequireRole;
import com.cqupt.dto.ReimbursementRatioDTO;
import com.cqupt.service.ReimbursementRatioService;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.ReimbursementRatioVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zhou Xinyang
 * @date 2025/07/08
 * @description
 */
@RestController
@RequestMapping("/reimbursementRatio")
@Api(tags = "报销比例")
@CrossOrigin
public class ReimbursementRatioController {
    @Autowired
    private ReimbursementRatioService ratioService;

    @RequireRole(value = { 1, 2 }, description = "医院操作员和报销管理员可查询")
    @ApiOperation("信息查询")
    @GetMapping("/info")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "page", value = "页码", required = true)
    })
    public ResultVo<Page<ReimbursementRatioVo>> selectReimbursementRatioPage(ReimbursementRatioDTO ratioDTO)
            throws Exception {
        return ratioService.selectReimbursementRatioPage(ratioDTO);
    }

    // 其他操作默认只有管理员可以访问
    @ApiOperation("添加报销比例")
    @PostMapping("/add")
    public ResultVo<Void> addReimbursementRatio(@RequestBody ReimbursementRatioDTO dto) throws Exception {
        return ratioService.addReimbursementRatio(dto);
    }

    @ApiOperation("修改报销比例")
    @PostMapping("/edit")
    public ResultVo<Void> updateReimbursementRatio(@RequestBody ReimbursementRatioDTO dto) throws Exception {
        return ratioService.updateReimbursementRatio(dto);
    }

    @ApiOperation("删除报销比例")
    @PostMapping("/remove")
    public ResultVo<Void> removeReimbursementRatio(@RequestBody ReimbursementRatioDTO dto) throws Exception {
        return ratioService.removeReimbursementRatio(dto.getId());
    }
}
