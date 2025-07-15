<template>
    <div class="common-layout">
        <el-container>
            <el-header>
                <div>
                    <el-row :gutter="30">
                        <el-col :span="7">
                            <el-input placeholder="请输入姓名查询" v-model="queryParams.customerName" @clear="query" clearable
                                size="large" @keyup.enter="query">
                                <template #append>
                                    <el-button type="info" @click="query" style="color: black">查询</el-button>
                                </template>
                            </el-input>
                        </el-col>
                    </el-row>
                </div>
            </el-header>
            <el-divider style="margin: 0"></el-divider>
            <el-main>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <div class="table-main">
                            <div class="table-main-header">参保人信息</div>
                            <el-table :data="customerInfoList" highlight-current-row size="small"
                                style="width: 100%; color: black" stripe>
                                <el-table-column align="center" fixed type="index" label="序号" width="60"
                                    :index="(i) => (page.currentPage - 1) * page.pageSize + i + 1" />
                                <el-table-column align="center" prop="name" label="姓名" width="60" />
                                <el-table-column align="center" prop="idCard" label="身份证号" width="180" />
                                <el-table-column align="center" prop="gender" label="性别" width="40">
                                    <template #default="scope">
                                        {{
                                            scope.row.gender === 0
                                                ? "女"
                                                : scope.row.gender === 1
                                                    ? "男"
                                                    : "未知"
                                        }}
                                    </template>
                                </el-table-column>
                                <el-table-column align="center" prop="age" label="年龄" width="40" />
                                <el-table-column align="center" prop="birthDate" label="出生日期" width="100" />
                                <el-table-column align="center" prop="inpatientNo" label="住院号" width="120" />
                                <el-table-column align="center" prop="settlementType" label="结算类型" width="100" />
                                <el-table-column align="center" prop="admissionTime" label="入院时间" width="160" />
                                <el-table-column align="center" prop="dischargeTime" label="出院时间" width="160" />
                                <el-table-column align="center" prop="emergencyContact" label="紧急联系人" width="120" />
                                <el-table-column align="center" prop="address" label="家庭地址" />
                                <el-table-column align="center" prop="workStatus" label="工作状态" width="100" />
                                <el-table-column align="center" prop="contactPhone" label="联系电话" width="130" />
                                <el-table-column align="center" label="操作" width="160" fixed="right">
                                    <template #default="scope">
                                        <el-button link size="small" @click="openDetail(scope.row)"
                                            style="color: #1890ff">
                                            详情
                                        </el-button>
                                        <el-button link size="small" @click="openReimburse(scope.row)"
                                            style="color: #ec407a">
                                            报销
                                        </el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                            <div style="margin-top: 15px">
                                <el-pagination :page-size="page.pageSize" background :current-page="page.currentPage"
                                    layout="prev, pager, next, total" :total="page.total"
                                    @current-change="handleCurrentChange" />
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </el-main>
        </el-container>

        <!-- 详情弹窗 -->
        <el-dialog title="报销详情" v-model="detailDialogVisible" width="700px">
            <div class="detail-pie-row">
                <div class="detail-pie-col">
                    <VueECharts :option="pieOption(detailPieData, '各费用占比')" autoresize style="height:260px;" />
                </div>
                <div class="detail-pie-col">
                    <VueECharts :option="pieOption(detailPieRbData, '报销/自付占比')" autoresize style="height:260px;" />
                </div>
            </div>
            <template #footer>
                <el-button @click="detailDialogVisible = false">关闭</el-button>
            </template>
        </el-dialog>

        <!-- 报销弹窗：报销确认页结构 -->
        <el-dialog title="报销" v-model="reimburseDialogVisible" width="1100px" top="40px">
            <div class="reimburse-confirm">
                <!-- 顶部信息 -->
                <div class="top-info">
                    <span>姓名：<span class="info-value">{{ selectedInsureder?.name || '--' }}</span></span>
                    <span>人员类别：<span class="info-value">{{ selectedInsureder?.workStatus || '--' }}</span></span>
                    <span>结算类别：<span class="info-value">{{ selectedInsureder?.settlementType || '--' }}</span></span>
                    <span>总费用：<span class="info-value">{{ totalFee || '--' }}</span></span>
                    <span>报销费用：<span class="info-value">{{ reimbursementFee || '--' }}</span></span>
                    <span>自付费用：<span class="info-value">{{ selfFee || '--' }}</span></span>
                </div>
                <!-- 四张表格 -->
                <div class="reimburse-table-row">
                    <div class="reimburse-table-col" v-for="(table, idx) in drugTables" :key="idx">
                        <div class="reimburse-table-header">
                            {{ table.title }}
                            <span v-if="table.ratio" class="ratio-tag">报销比例{{ table.ratio }}</span>
                            <span v-if="table.fee" class="fee-tag">费用 {{ table.fee }} 元</span>
                        </div>
                        <el-table :data="table.data" size="small" stripe
                            style="color: #222;max-height: 220px; overflow-y: auto;">
                            <el-table-column prop="drugName" label="药品名称" />
                            <el-table-column prop="unit" label="单位" width="60" />
                            <el-table-column prop="quantity" label="数量" width="60" />
                            <el-table-column label="价格" width="80">
                                <template #default="scope">
                                    {{ (scope.row.price * scope.row.quantity).toFixed(2) }}
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                    <!-- 医疗服务+诊疗项目 -->
                    <div class="reimburse-table-col">
                        <div class="reimburse-table-header">其他项目</div>
                        <el-table :data="serviceAndItemList" size="small" stripe
                            style="color: #222;max-height: 220px; overflow-y: auto;">
                            <el-table-column prop="itemName" label="项目名称" />
                            <el-table-column prop="unit" label="单位" width="60" />
                            <el-table-column prop="quantity" label="数量" width="60" />
                            <el-table-column label="价格" width="80">
                                <template #default="scope">
                                    {{ (scope.row.price * scope.row.quantity).toFixed(2) }}
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
                <!-- 计算公式和报销比例表 -->
                <div class="formula-section">
                    <div class="formula-tip">
                        医保报销费用 = [（甲类药品报销费用 + 乙类药品报销费用 + 丙类药品报销费用 + 其他费用）- 起付线] × 报销比例
                    </div>
                    <el-table :data="reimbursementRatioList" size="small" stripe
                        style="margin-top: 10px; color: #222;max-height: 220px; overflow-y: auto;">
                        <el-table-column prop="startAmount" label="起付线" />
                        <el-table-column prop="endAmount" label="等级线" />
                        <el-table-column prop="ratio" label="报销比例" />
                    </el-table>
                    <div class="formula-tip" style="color: #888; margin-top: 8px;"
                        v-if="!reimbursementRatioList.length">
                    </div>
                </div>
            </div>
            <template #footer>
                <el-button @click="reimburseDialogVisible = false">关闭</el-button>
                <el-button type="primary" @click="handleConfirmRb">确认报销</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from "vue";
import { ElMessage } from "element-plus";
import { selectInsurederPage } from "@/api/insurederApi.js";
import { getDrugOrderPage } from "@/api/drugOrderApi.js";
import { getMedicalServiceOrderPage } from "@/api/medicalServiceOrderApi.js";
import { getTreatmentItemOrderPage } from "@/api/treatmentItemOrderApi.js";
import { getReimbursementRatioPage, } from "@/api/reimbursementRatioApi.js";
import { getEnabledDrugRbRatios } from "@/api/drugReimbursementRatioApi.js";
import { calculateRb, confirmRb } from "@/api/reimbursementRecordApi.js";


import { use } from 'echarts/core';
import { PieChart } from 'echarts/charts';
import { CanvasRenderer } from 'echarts/renderers';
import { LegendComponent, TooltipComponent } from 'echarts/components';
import VueECharts from 'vue-echarts';
use([PieChart, CanvasRenderer, LegendComponent, TooltipComponent]);

// 查询参数
const queryParams = reactive({
    customerName: "",
});

// 分页和条件
const page = reactive({
    total: 0,
    pageSize: 6,
    currentPage: 1,
});
const condition = reactive({
    name: "",
    page: 1,
});

// 数据列表
const customerInfoList = ref([]);

// 弹窗控制
const detailDialogVisible = ref(false);
const reimburseDialogVisible = ref(false);

// 报销弹窗相关数据
const selectedInsureder = ref(null);// 被选中的医保对象
const totalFee = ref('');// 总费用
const reimbursementFee = ref('');// 医保报销费用
const selfFee = ref('');// 自付费用
const drugAList = ref([]); // 甲类
const drugBList = ref([]); // 乙类
const drugCList = ref([]); // 丙类
const serviceAndItemList = ref([]); // 医疗服务+诊疗项目
const reimbursementRatioList = ref([]);// 报销比例
const enabledRatios = ref([]); // 启用的药品报销比例

// 四张表格配置，ratio动态取自enabledRatios
const drugTables = computed(() => {
    const ratioMap = {};
    enabledRatios.value.forEach(item => {
        ratioMap[item.drugType] = item.drugRatio;
    });
    return [
        { title: "甲类药品", ratio: ratioMap["甲类"] ? ratioMap["甲类"] + "%" : "-", fee: "", data: drugAList.value },
        { title: "乙类药品", ratio: ratioMap["乙类"] ? ratioMap["乙类"] + "%" : "-", fee: "", data: drugBList.value },
        { title: "丙类药品", ratio: ratioMap["丙类"] ? ratioMap["丙类"] + "%" : "-", fee: "", data: drugCList.value }
    ];
});

// 饼图数据：药品/其他费用占比
const detailPieData = computed(() => {
    // 计算各类费用
    const drugA = drugAList.value.reduce((sum, d) => sum + d.price * d.quantity, 0);
    const drugB = drugBList.value.reduce((sum, d) => sum + d.price * d.quantity, 0);
    const drugC = drugCList.value.reduce((sum, d) => sum + d.price * d.quantity, 0);
    const other = serviceAndItemList.value.reduce((sum, d) => sum + d.price * d.quantity, 0);
    return [
        { value: drugA, name: '甲类药品' },
        { value: drugB, name: '乙类药品' },
        { value: drugC, name: '丙类药品' },
        { value: other, name: '其他费用' },
    ].filter(item => item.value > 0);
});
// 饼图数据：报销/自付占比
const detailPieRbData = computed(() => {
    const rb = Number(reimbursementFee.value) || 0;
    const self = Number(selfFee.value) || 0;
    return [
        { value: rb, name: '报销费用' },
        { value: self, name: '自付费用' },
    ].filter(item => item.value > 0);
});

// 饼图option
const pieOption = (data, title) => ({
    // color: ['#ec407a', '#42a5f5', '#ffd600', '#66bb6a', '#ab47bc', '#ff7043'],
    title: {
        text: title,
        left: 'center',
        top: 10,
        textStyle: { fontSize: 15, color: '#ad1457' }
    },
    tooltip: { trigger: 'item', formatter: '{b}: {c}元 ({d}%)' },
    legend: { bottom: 0, left: 'center', textStyle: { color: '#ad1457' } },
    series: [
        {
            name: title,
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: { borderRadius: 8, borderColor: '#fff', borderWidth: 2 },
            label: { show: true, formatter: '{b}\n{d}%' },
            data,
        },
    ],
});

// 加载数据
const loadCustomerList = async () => {
    condition.name = queryParams.customerName;
    condition.page = page.currentPage;
    try {
        const res = await selectInsurederPage(condition);
        if (res.flag) {
            customerInfoList.value = res.data.records;
            page.total = res.data.total;
            page.pageSize = res.data.size;
            page.currentPage = res.data.current;
        } else {
            ElMessage.error(res.message || "查询失败");
        }
    } catch (err) {
        ElMessage.error("网络错误，请重试");
    }
};

// 查询
const query = () => {
    page.currentPage = 1;
    loadCustomerList();
};

// 分页
const handleCurrentChange = (curPage) => {
    page.currentPage = curPage;
    loadCustomerList();
};

// 打开详情弹窗
const openDetail = async (row) => {
    // 查询三类药品
    const [drugARes, drugBRes, drugCRes] = await Promise.all([
        getDrugOrderPage({ patientId: row.id, drugType: "甲类", page: 1, pageSize: 100 }),
        getDrugOrderPage({ patientId: row.id, drugType: "乙类", page: 1, pageSize: 100 }),
        getDrugOrderPage({ patientId: row.id, drugType: "丙类", page: 1, pageSize: 100 }),
    ]);
    drugAList.value = drugARes.flag ? drugARes.data.records : [];
    drugBList.value = drugBRes.flag ? drugBRes.data.records : [];
    drugCList.value = drugCRes.flag ? drugCRes.data.records : [];
    // 查询医疗服务和诊疗项目，并合并
    const [serviceRes, itemRes] = await Promise.all([
        getMedicalServiceOrderPage({ patientId: row.id, page: 1, pageSize: 100 }),
        getTreatmentItemOrderPage({ patientId: row.id, page: 1, pageSize: 100 }),
    ]);
    const serviceList = (serviceRes.flag ? serviceRes.data.records : []).map(item => ({
        itemName: item.serviceName || item.itemName,
        unit: item.unit,
        quantity: item.quantity,
        price: item.price,
    }));
    const itemList = (itemRes.flag ? itemRes.data.records : []).map(item => ({
        itemName: item.itemName,
        unit: item.unit,
        quantity: item.quantity,
        price: item.price,
    }));
    serviceAndItemList.value = [...serviceList, ...itemList];
    // 查询报销金额详情
    const params = { patientId: row.id };
    const res = await calculateRb(params);
    if (res.flag) {
        totalFee.value = res.data.totalFee;
        reimbursementFee.value = res.data.reimbursementAmount;
        selfFee.value = res.data.selfPayAmount;
    } else {
        totalFee.value = '';
        reimbursementFee.value = '';
        selfFee.value = '';
    }
    detailDialogVisible.value = true;
};

// 初始化
onMounted(() => {
    loadCustomerList();
});


// --------------------------------
// 打开报销弹窗
const openReimburse = async (row) => {
    selectedInsureder.value = row;
    // 查询三类药品
    const [drugARes, drugBRes, drugCRes] = await Promise.all([
        getDrugOrderPage({ patientId: row.id, drugType: "甲类", page: 1, pageSize: 100 }),
        getDrugOrderPage({ patientId: row.id, drugType: "乙类", page: 1, pageSize: 100 }),
        getDrugOrderPage({ patientId: row.id, drugType: "丙类", page: 1, pageSize: 100 }),
    ]);
    drugAList.value = drugARes.flag ? drugARes.data.records : [];
    drugBList.value = drugBRes.flag ? drugBRes.data.records : [];
    drugCList.value = drugCRes.flag ? drugCRes.data.records : [];
    // 查询医疗服务和诊疗项目，并合并
    const [serviceRes, itemRes] = await Promise.all([
        getMedicalServiceOrderPage({ patientId: row.id, page: 1, pageSize: 100 }),
        getTreatmentItemOrderPage({ patientId: row.id, page: 1, pageSize: 100 }),
    ]);
    const serviceList = (serviceRes.flag ? serviceRes.data.records : []).map(item => ({
        itemName: item.serviceName || item.itemName,
        unit: item.unit,
        quantity: item.quantity,
        price: item.price,
    }));
    const itemList = (itemRes.flag ? itemRes.data.records : []).map(item => ({
        itemName: item.itemName,
        unit: item.unit,
        quantity: item.quantity,
        price: item.price,
    }));
    serviceAndItemList.value = [...serviceList, ...itemList];

    // 查询报销比例
    await loadReimbursementRatioList();

    // 查询药品报销比例（只在弹窗打开时查）
    await loadEnabledRatios();

    // 查询报销
    await loadCalculatedRb();

    reimburseDialogVisible.value = true;
};

// 查询报销比例
const loadReimbursementRatioList = async () => {
    // 医院等级固定'一级'，status固定'启用'，staffType从选中投保人workStatus取
    const params = {
        page: 1,
        pageSize: 100,
        hospitalLevel: "一级",
        staffType: selectedInsureder.value?.workStatus || "",
        status: "启用",
    };
    const res = await getReimbursementRatioPage(params);
    if (res.flag) {
        reimbursementRatioList.value = res.data.records;
    } else {
        reimbursementRatioList.value = [];
    }
};

// 查询药品报销比例
const loadEnabledRatios = async () => {
    const res = await getEnabledDrugRbRatios();
    if (res.flag) {
        enabledRatios.value = res.data;
    } else {
        enabledRatios.value = [];
    }
};

// 查询报销金额详情
const loadCalculatedRb = async () => {
    const params = {
        patientId: selectedInsureder.value?.id,
    };
    const res = await calculateRb(params);
    if (res.flag) {
        totalFee.value = res.data.totalFee;// 总费用
        reimbursementFee.value = res.data.reimbursementAmount;// 医保报销费用
        selfFee.value = res.data.selfPayAmount;// 自付费用
    } else {
    }
};

// 确认报销
const handleConfirmRb = async () => {
    if (!selectedInsureder.value?.id) {
        ElMessage.error("未选中参保人");
        return;
    }
    try {
        const res = await confirmRb({ patientId: selectedInsureder.value.id });
        if (res.flag) {
            ElMessage.success("报销成功");
            reimburseDialogVisible.value = false;
            loadCustomerList(); // 刷新列表
        } else {
            ElMessage.error(res.message || "报销失败");
        }
    } catch (err) {
        ElMessage.error("网络错误，请重试");
    }
};

</script>

<style scoped>
.table-main {
    min-height: 500px;
    border: 1px solid #f8bbd0;
    background: #fff0f6;
    border-radius: 8px;
    padding: 16px;
}

.table-main .table-main-header {
    height: 50px;
    background-color: #ec407a;
    color: #fff;
    font-size: 20px;
    font-family: "Microsoft YaHei";
    line-height: 50px;
    padding-left: 20px;
    border-radius: 8px 8px 0 0;
    margin-bottom: 10px;
}

.el-table {
    background: #fff0f6;
    color: #222;
}

.el-table th {
    background: #f8bbd0 !important;
    color: #ad1457 !important;
}

.el-pagination.is-background .el-pager li:not(.disabled).active {
    background-color: #ec407a;
    color: #fff;
}

.el-button--primary {
    background-color: #ec407a;
    border-color: #ec407a;
}

.el-button--primary:hover {
    background-color: #ad1457;
    border-color: #ad1457;
}

.el-dialog__header {
    background: #f8bbd0;
    color: #ad1457;
}

.el-dialog__footer {
    background: #fff0f6;
}

.el-form-item__label {
    color: #ad1457;
}

/* 报销弹窗专用样式 */
.reimburse-confirm {
    background: #fff0f6;
    padding: 10px 10px 0 10px;
    border-radius: 8px;
}

.top-info {
    margin: 10px 0 18px 0;
    font-size: 16px;
    color: #ad1457;
    display: flex;
    flex-wrap: wrap;
    gap: 30px;
    align-items: center;
}

.info-value {
    color: #ec407a;
    font-weight: bold;
    margin-left: 4px;
}

.reimburse-table-row {
    display: flex;
    gap: 18px;
    margin-bottom: 24px;
    justify-content: space-between;
}

.reimburse-table-col {
    background: #fff;
    border-radius: 10px;
    box-shadow: 0 1px 4px #f8bbd0;
    padding: 12px 10px 8px 10px;
    min-width: 220px;
    flex: 1;
    display: flex;
    flex-direction: column;
}

.reimburse-table-header {
    font-size: 16px;
    color: #ec407a;
    font-weight: bold;
    margin-bottom: 8px;
    letter-spacing: 1px;
    display: flex;
    align-items: center;
    gap: 10px;
}

.ratio-tag {
    background: #f8bbd0;
    color: #ad1457;
    border-radius: 4px;
    padding: 2px 8px;
    font-size: 13px;
}

.fee-tag {
    background: #e1bee7;
    color: #ad1457;
    border-radius: 4px;
    padding: 2px 8px;
    font-size: 13px;
}

.formula-section {
    background: #fff;
    border-radius: 10px;
    box-shadow: 0 1px 4px #f8bbd0;
    padding: 18px 18px 10px 18px;
    margin-top: 18px;
}

.formula-tip {
    color: #ec407a;
    font-size: 15px;
    margin-bottom: 8px;
    font-weight: bold;
}

.detail-pie-row {
    display: flex;
    gap: 30px;
    justify-content: space-between;
    align-items: flex-start;
    margin: 10px 0 0 0;
}

.detail-pie-col {
    flex: 1;
    min-width: 0;
    background: #fff;
    border-radius: 10px;
    box-shadow: 0 1px 4px #f8bbd0;
    padding: 10px 10px 0 10px;
    display: flex;
    flex-direction: column;
    align-items: center;
}
</style>