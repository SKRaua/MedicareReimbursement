<template>
    <div class="common-layout">
        <el-container>
            <el-main>
                <el-row :gutter="20" style="min-height: 700px;">
                    <!-- 左侧：参保人表格 -->
                    <el-col :span="10">

                        <div class="table-main" style="height: 100%;">
                            <div>
                                <el-row :gutter="30">
                                    <el-col :span="7">
                                        <el-input placeholder="请输入姓名查询" v-model="queryParams.customerName"
                                            @clear="query" clearable size="large" @keyup.enter="query"
                                            style="width: 220px;">
                                            <template #append>
                                                <el-button type="info" @click="query"
                                                    style="color: black">查询</el-button>
                                            </template>
                                        </el-input>
                                    </el-col>
                                </el-row>
                            </div>
                            <div style="height: 10px;"></div>
                            <div class="table-main-header">参保人信息</div>
                            <el-table :data="customerInfoList" highlight-current-row size="small" style="width: 100%;"
                                stripe @row-click="handleRowClick">
                                <el-table-column type="index" label="序号" width="60" />
                                <el-table-column prop="name" label="姓名" width="60" />
                                <el-table-column align="center" prop="inpatientNo" label="住院号" width="120" />
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
                            </el-table>
                            <div style="margin-top: 15px">
                                <el-pagination :page-size="page.pageSize" :current-page="page.currentPage"
                                    :total="page.total" layout="prev, pager, next, total"
                                    @current-change="handleCurrentChange" />
                            </div>
                        </div>
                    </el-col>

                    <!-- 右侧：费用详情 -->
                    <el-col :span="14">
                        <div class="table-main cost-panel" style="height: 100%;">
                            <div class="table-main-header">费用详情</div>
                            <div class="cost-table">
                                <div class="cost-table-header">药品信息</div>
                                <el-table :data="drugList" size="small" stripe empty-text="暂无药品数据">
                                    <el-table-column prop="drugName" label="药品名称" />
                                    <el-table-column prop="specification" label="规格" />
                                    <el-table-column prop="unit" label="单位" />
                                    <el-table-column prop="manufacturer" label="生产企业" />
                                    <el-table-column prop="price" label="单价" width="80" />
                                    <el-table-column prop="quantity" label="数量" width="60" />
                                    <el-table-column prop="orderTime" label="开药时间" width="120" />
                                </el-table>
                                <el-pagination v-model:current-page="drugPage.currentPage"
                                    :page-size="drugPage.pageSize" :total="drugPage.total"
                                    layout="prev, pager, next, total" @current-change="loadDrugList"
                                    style="margin-top: 10px" />
                            </div>
                            <div class="cost-table">
                                <div class="cost-table-header">医疗服务</div>
                                <el-table :data="medicalServiceList" size="small" stripe empty-text="暂无医疗服务数据">
                                    <el-table-column prop="itemName" label="服务名称" />
                                    <el-table-column prop="unit" label="单位" />
                                    <el-table-column prop="price" label="单价" width="80" />
                                    <el-table-column prop="quantity" label="数量" width="60" />
                                    <el-table-column prop="orderTime" label="服务时间" width="120" />
                                </el-table>
                                <el-pagination v-model:current-page="medicalServicePage.currentPage"
                                    :page-size="medicalServicePage.pageSize" :total="medicalServicePage.total"
                                    layout="prev, pager, next, total" @current-change="loadMedicalServiceList"
                                    style="margin-top: 10px" />
                            </div>
                            <div class="cost-table">
                                <div class="cost-table-header">诊疗项目</div>
                                <el-table :data="treatmentItemList" size="small" stripe empty-text="暂无诊疗项目数据">
                                    <el-table-column prop="itemName" label="项目名称" />
                                    <el-table-column prop="unit" label="单位" />
                                    <el-table-column prop="price" label="单价" width="80" />
                                    <el-table-column prop="quantity" label="数量" width="60" />
                                    <el-table-column prop="orderTime" label="执行时间" width="120" />
                                </el-table>
                                <el-pagination v-model:current-page="treatmentItemPage.currentPage"
                                    :page-size="treatmentItemPage.pageSize" :total="treatmentItemPage.total"
                                    layout="prev, pager, next, total" @current-change="loadTreatmentItemList"
                                    style="margin-top: 10px" />
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </el-main>
        </el-container>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import { ElMessage } from "element-plus";
import { selectInsurederPage } from "@/api/insurederApi.js";
import { getDrugOrderPage } from "@/api/drugOrderApi.js";
import { getMedicalServiceOrderPage } from "@/api/medicalServiceOrderApi.js";
import { getTreatmentItemOrderPage } from "@/api/treatmentItemOrderApi.js";

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

// 参保人数据
const customerInfoList = ref([]);
const currentInsurederId = ref(null);

// 药品订单
const drugList = ref([]);
const drugPage = reactive({
    total: 0,
    pageSize: 5,
    currentPage: 1,
});

// 医疗服务订单
const medicalServiceList = ref([]);
const medicalServicePage = reactive({
    total: 0,
    pageSize: 5,
    currentPage: 1,
});

// 诊疗项目订单
const treatmentItemList = ref([]);
const treatmentItemPage = reactive({
    total: 0,
    pageSize: 5,
    currentPage: 1,
});

// 加载参保人列表
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
            // 默认选中第一个参保人
            if (customerInfoList.value.length > 0 && !currentInsurederId.value) {
                handleRowClick(customerInfoList.value[0]);
            }
        } else {
            ElMessage.error(res.message || "查询失败");
        }
    } catch (err) {
        ElMessage.error("网络错误，请重试");
    }
};

// 参保人表格行点击
const handleRowClick = (row) => {
    currentInsurederId.value = row.id;
    loadDrugList(1);
    loadMedicalServiceList(1);
    loadTreatmentItemList(1);
};

// 药品订单分页
const loadDrugList = async (pageNum = 1) => {
    if (!currentInsurederId.value) return;
    drugPage.currentPage = pageNum;
    try {
        const res = await getDrugOrderPage({
            patientId: currentInsurederId.value,
            page: drugPage.currentPage,
            pageSize: drugPage.pageSize,
        });
        if (res.flag) {
            drugList.value = res.data.records;
            drugPage.total = res.data.total;
        } else {
            ElMessage.error(res.message || "药品数据加载失败");
        }
    } catch (err) {
        ElMessage.error("网络错误，请重试");
    }
};

// 医疗服务订单分页
const loadMedicalServiceList = async (pageNum = 1) => {
    if (!currentInsurederId.value) return;
    medicalServicePage.currentPage = pageNum;
    try {
        const res = await getMedicalServiceOrderPage({
            patientId: currentInsurederId.value,
            page: medicalServicePage.currentPage,
            pageSize: medicalServicePage.pageSize,
        });
        if (res.flag) {
            medicalServiceList.value = res.data.records;
            medicalServicePage.total = res.data.total;
        } else {
            ElMessage.error(res.message || "医疗服务数据加载失败");
        }
    } catch (err) {
        ElMessage.error("网络错误，请重试");
    }
};

// 诊疗项目订单分页
const loadTreatmentItemList = async (pageNum = 1) => {
    if (!currentInsurederId.value) return;
    treatmentItemPage.currentPage = pageNum;
    try {
        const res = await getTreatmentItemOrderPage({
            patientId: currentInsurederId.value,
            page: treatmentItemPage.currentPage,
            pageSize: treatmentItemPage.pageSize,
        });
        if (res.flag) {
            treatmentItemList.value = res.data.records;
            treatmentItemPage.total = res.data.total;
        } else {
            ElMessage.error(res.message || "诊疗项目数据加载失败");
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

// 初始化
onMounted(() => {
    loadCustomerList();
});
</script>


<style scoped>
.table-main {
    min-height: 500px;
    border: 1.5px solid #f8bbd0;
    background: #fff0f6;
    border-radius: 14px;
    padding: 18px 18px 10px 18px;
    margin-bottom: 20px;
    box-shadow: 0 2px 8px #f8bbd0;
}

.table-main-header {
    height: 48px;
    background-color: #ec407a;
    color: #fff;
    font-size: 20px;
    font-family: "Microsoft YaHei";
    line-height: 48px;
    padding-left: 20px;
    border-radius: 8px 8px 0 0;
    margin-bottom: 18px;
    letter-spacing: 2px;
}

.cost-table {
    background: #fff;
    border-radius: 10px;
    margin-bottom: 18px;
    padding: 12px 10px 8px 10px;
    box-shadow: 0 1px 4px #f8bbd0;
    max-height: 200px;
    overflow-y: auto;
}

.cost-table-header {
    font-size: 16px;
    color: #ec407a;
    font-weight: bold;
    margin-bottom: 8px;
    letter-spacing: 1px;
}

.el-table {
    background: #fff0f6;
    /* color: #ad1457; */
    border-radius: 8px;
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

.el-form-item__label {
    color: #ad1457;
}
</style>