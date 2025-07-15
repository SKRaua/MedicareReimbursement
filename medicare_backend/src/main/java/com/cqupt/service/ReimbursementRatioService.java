// ReimbursementRatioService.java
package com.cqupt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.dto.ReimbursementRatioDTO;
import com.cqupt.pojo.ReimbursementRatio;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.ReimbursementRatioVo;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description 报销比例维护Service
 */
public interface ReimbursementRatioService extends IService<ReimbursementRatio> {
    public ResultVo<Page<ReimbursementRatioVo>> selectReimbursementRatioPage(ReimbursementRatioDTO ratioDTO)
            throws Exception;

    ResultVo<Void> addReimbursementRatio(ReimbursementRatioDTO dto) throws Exception;

    ResultVo<Void> updateReimbursementRatio(ReimbursementRatioDTO dto) throws Exception;

    ResultVo<Void> removeReimbursementRatio(Integer id) throws Exception;

}