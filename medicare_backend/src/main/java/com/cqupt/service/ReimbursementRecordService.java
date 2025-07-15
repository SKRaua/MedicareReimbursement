// ReimbursementRecordService.java
package com.cqupt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.dto.ReimbursementCalcDTO;
import com.cqupt.dto.ReimbursementRecordDTO;
import com.cqupt.pojo.ReimbursementRecord;
import com.cqupt.utils.ResultVo;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description 报销记录Service
 */
public interface ReimbursementRecordService extends IService<ReimbursementRecord> {
    // 计算费用并返回DTO
    public ResultVo<ReimbursementCalcDTO> calculateReimbursement(Integer insurederId) throws Exception;

    // 确认报销并保存记录
    public ResultVo<Void> confirmReimbursement(ReimbursementRecordDTO recordDTO) throws Exception;
}