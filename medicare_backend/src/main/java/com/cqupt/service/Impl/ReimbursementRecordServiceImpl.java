package com.cqupt.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.dto.ReimbursementCalcDTO;
import com.cqupt.dto.ReimbursementRecordDTO;
import com.cqupt.mapper.*;
import com.cqupt.pojo.*;
import com.cqupt.service.ReimbursementRecordService;
import com.cqupt.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description 报销记录Service实现类
 */
@Service
public class ReimbursementRecordServiceImpl extends ServiceImpl<ReimbursementRecordMapper, ReimbursementRecord>
        implements ReimbursementRecordService {

    @Autowired
    private DrugOrderMapper drugOrderMapper;

    @Autowired
    private InsurederMapper insurederMapper;

    @Autowired
    private DrugRbRatioMapper drugRbRatioMapper;

    @Autowired
    private DrugMapper drugMapper;

    @Autowired
    private MedicalServiceMapper medicalServiceMapper;

    @Autowired
    private MedicalServiceOrderMapper medicalServiceOrderMapper;

    @Autowired
    private ReimbursementRatioMapper reimbursementRatioMapper;

    @Autowired
    private ReimbursementRecordMapper reimbursementRecordMapper;

    @Autowired
    private TreatmentItemMapper treatmentItemMapper;

    @Autowired
    private TreatmentItemOrderMapper treatmentItemOrderMapper;

    /**
     * 计算报销相关费用
     */
    @Override
    public ResultVo<ReimbursementCalcDTO> calculateReimbursement(Integer insurederId) throws Exception {

        Insureder insureder = insurederMapper.selectById(insurederId);
        if (insureder == null) {
            return ResultVo.fail("未找到该投保人信息，无法计算报销");
        }
        // 查询药品订单
        QueryWrapper<DrugOrder> drugOrderQw = new QueryWrapper<>();
        drugOrderQw.eq("patient_id", insurederId);
        List<DrugOrder> drugOrders = drugOrderMapper.selectList(drugOrderQw);

        BigDecimal drugAFee = BigDecimal.ZERO;
        BigDecimal drugBFee = BigDecimal.ZERO;
        BigDecimal drugCFee = BigDecimal.ZERO;

        for (DrugOrder order : drugOrders) {
            Drug drug = drugMapper.selectById(order.getDrugId());
            if (drug == null)
                continue;
            BigDecimal price = drug.getPrice() == null ? BigDecimal.ZERO : drug.getPrice();
            BigDecimal fee = price.multiply(new BigDecimal(order.getQuantity()));
            String type = drug.getDrugType(); // 甲类/乙类/丙类
            if ("甲类".equals(type)) {
                drugAFee = drugAFee.add(fee);
            } else if ("乙类".equals(type)) {
                drugBFee = drugBFee.add(fee);
            } else if ("丙类".equals(type)) {
                drugCFee = drugCFee.add(fee);
            }
        }

        // 查询医疗服务订单
        QueryWrapper<MedicalServiceOrder> serviceOrderQw = new QueryWrapper<>();
        serviceOrderQw.eq("patient_id", insurederId);
        List<MedicalServiceOrder> serviceOrders = medicalServiceOrderMapper.selectList(serviceOrderQw);

        BigDecimal serviceFee = BigDecimal.ZERO;
        for (MedicalServiceOrder order : serviceOrders) {
            MedicalService service = medicalServiceMapper.selectById(order.getItemId());
            if (service == null)
                continue;
            BigDecimal price = service.getPrice() == null ? BigDecimal.ZERO : service.getPrice();
            BigDecimal fee = price.multiply(new BigDecimal(order.getQuantity()));
            serviceFee = serviceFee.add(fee);
        }

        // 查询诊疗项目订单
        QueryWrapper<TreatmentItemOrder> itemOrderQw = new QueryWrapper<>();
        itemOrderQw.eq("patient_id", insurederId);
        List<TreatmentItemOrder> itemOrders = treatmentItemOrderMapper.selectList(itemOrderQw);

        BigDecimal itemFee = BigDecimal.ZERO;
        for (TreatmentItemOrder order : itemOrders) {
            TreatmentItem item = treatmentItemMapper.selectById(order.getItemId());
            if (item == null)
                continue;
            BigDecimal price = item.getPrice() == null ? BigDecimal.ZERO : item.getPrice();
            BigDecimal fee = price.multiply(new BigDecimal(order.getQuantity()));
            itemFee = itemFee.add(fee);
        }

        // 计算总费用
        BigDecimal totalFee = drugAFee.add(drugBFee).add(drugCFee).add(serviceFee).add(itemFee);

        // 自费
        if (!insureder.getSettlementType().equals("医保")) {

            // 计算可报销费用
            // BigDecimal reimbursable = BigDecimal.ZERO;
            BigDecimal reimbursementAmount = BigDecimal.ZERO;
            BigDecimal selfPayAmount = totalFee;

            // 封装DTO返回
            ReimbursementCalcDTO dto = new ReimbursementCalcDTO(totalFee, reimbursementAmount, selfPayAmount, drugAFee,
                    drugBFee, drugCFee, serviceFee, itemFee, new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
            return ResultVo.ok(dto);
        }

        // 报销
        // 查询药品报销比例（Drug_Rb_Ratio），限定status=启用
        QueryWrapper<DrugRbRatio> rbRatioQw = new QueryWrapper<>();
        rbRatioQw.eq("status", "启用");
        List<DrugRbRatio> rbRatios = drugRbRatioMapper.selectList(rbRatioQw);

        // 只保留甲/乙/丙三类
        BigDecimal drugARatio = null, drugBRatio = null, drugCRatio = null;
        int aCount = 0, bCount = 0, cCount = 0;
        for (Object obj : rbRatios) {
            // 你可以用具体的DrugRbRatio类型替换Object
            com.cqupt.pojo.DrugRbRatio ratio = (com.cqupt.pojo.DrugRbRatio) obj;
            if ("甲类".equals(ratio.getDrugType())) {
                drugARatio = ratio.getDrugRatio();
                aCount++;
            } else if ("乙类".equals(ratio.getDrugType())) {
                drugBRatio = ratio.getDrugRatio();
                bCount++;
            } else if ("丙类".equals(ratio.getDrugType())) {
                drugCRatio = ratio.getDrugRatio();
                cCount++;
            }
        }
        // 检查每类只能有一条
        if (aCount != 1 || bCount != 1 || cCount != 1) {
            return ResultVo.fail("药品报销比例异常");
        }

        // 先对三类药品费用分别乘以各自比例
        drugAFee = drugAFee
                .multiply(drugARatio == null ? BigDecimal.ZERO : drugARatio.multiply(new BigDecimal("0.01")));
        drugBFee = drugBFee
                .multiply(drugBRatio == null ? BigDecimal.ZERO : drugBRatio.multiply(new BigDecimal("0.01")));
        drugCFee = drugCFee
                .multiply(drugCRatio == null ? BigDecimal.ZERO : drugCRatio.multiply(new BigDecimal("0.01")));

        // 查询报销比例（只取第一个启用的比例）
        QueryWrapper<ReimbursementRatio> ratioQw = new QueryWrapper<>();
        ratioQw.eq("status", "启用");
        ratioQw.ge("end_amount", totalFee); // end_amount >= totalFee
        ratioQw.orderByAsc("end_amount"); // 升序，取最小的
        ratioQw.last("limit 1");
        ReimbursementRatio ratio = reimbursementRatioMapper.selectOne(ratioQw);
        BigDecimal deductible = ratio != null && ratio.getStartAmount() != null ? ratio.getStartAmount()
                : BigDecimal.ZERO;
        BigDecimal reimbursementRatio = ratio != null && ratio.getRatio() != null ? ratio.getRatio()
                : BigDecimal.ONE;

        // 计算可报销费用
        BigDecimal reimbursable = totalFee.subtract(deductible).max(BigDecimal.ZERO);
        BigDecimal reimbursementAmount = reimbursable.multiply(reimbursementRatio.multiply(new BigDecimal("0.01")))
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal selfPayAmount = totalFee.subtract(reimbursementAmount);

        // 封装DTO返回
        ReimbursementCalcDTO dto = new ReimbursementCalcDTO(totalFee, reimbursementAmount, selfPayAmount, drugAFee,
                drugBFee, drugCFee, serviceFee, itemFee, reimbursable, deductible, reimbursementRatio);
        return ResultVo.ok(dto);
    }

    @Override
    public ResultVo<Void> confirmReimbursement(ReimbursementRecordDTO recordDTO) throws Exception {

        // 重新计算费用（防止前端篡改）
        ReimbursementCalcDTO calc = calculateReimbursement(recordDTO.getPatientId()).getData();
        if (calc == null) {
            throw new Exception("费用计算异常，无法完成报销");
        }
        // 保存报销记录
        ReimbursementRecord record = new ReimbursementRecord();
        record.setPatientId(recordDTO.getPatientId());
        record.setTotalMedicalFee(calc.getTotalFee());
        record.setReimbursementAmount(calc.getReimbursementAmount());
        record.setSelfPayAmount(calc.getSelfPayAmount());
        record.setDeductibleAmount(calc.getDeductible());
        record.setReimbursableAmount(calc.getReimbursable());
        record.setReimbursementRatio(calc.getRatio());
        record.setReimbursementDate(LocalDateTime.now());
        // ...其他字段赋值...
        Insureder insureder = insurederMapper.selectById(recordDTO.getPatientId());
        if (insureder == null) {
            throw new Exception("投保人信息不存在，无法完成报销");
        }
        record.setName(insureder.getName());
        record.setIdCard(insureder.getIdCard());
        record.setInpatientNo(insureder.getInpatientNo());
        record.setWorkStatus(insureder.getWorkStatus());

        int insert = reimbursementRecordMapper.insert(record);
        if (insert <= 0) {
            throw new Exception("保存报销记录失败");
        }

        // 删除院内药品，医疗服务，诊疗项目记录
        QueryWrapper<DrugOrder> drugOrderDelQw = new QueryWrapper<>();
        drugOrderDelQw.eq("patient_id", recordDTO.getPatientId());
        drugOrderMapper.delete(drugOrderDelQw);

        QueryWrapper<MedicalServiceOrder> serviceOrderDelQw = new QueryWrapper<>();
        serviceOrderDelQw.eq("patient_id", recordDTO.getPatientId());
        medicalServiceOrderMapper.delete(serviceOrderDelQw);

        QueryWrapper<TreatmentItemOrder> itemOrderDelQw = new QueryWrapper<>();
        itemOrderDelQw.eq("patient_id", recordDTO.getPatientId());
        treatmentItemOrderMapper.delete(itemOrderDelQw);

        // 校验三张表都无该患者记录才算成功
        int leftDrug = drugOrderMapper.selectCount(drugOrderDelQw);
        int leftService = medicalServiceOrderMapper.selectCount(serviceOrderDelQw);
        int leftItem = treatmentItemOrderMapper.selectCount(itemOrderDelQw);

        if (leftDrug > 0 || leftService > 0 || leftItem > 0) {
            throw new Exception("删除院内明细失败");
        }

        return ResultVo.ok("报销成功");
    }
}