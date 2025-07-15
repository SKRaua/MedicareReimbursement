package com.cqupt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.dto.InsurederDTO;
import com.cqupt.pojo.Insureder;
import com.cqupt.utils.ResultVo;
import com.cqupt.vo.InsurederVo;

/**
 * @author Zhou Xinyang
 * @date 2025/07/08
 * @description
 */
public interface InsurederService extends IService<Insureder> {

    public ResultVo<Page<InsurederVo>> selectInsurederPage(InsurederDTO patientDTO) throws Exception;

    // public ResultVo<Void> addInsurederInfo(InsurederDTO patientDTO) throws
    // Exception;

    // public ResultVo<Void> editInsurederInfo(InsurederDTO patientDTO) throws
    // Exception;

}
