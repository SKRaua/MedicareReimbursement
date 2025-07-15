<template>
    <div class="common-layout">
        <el-container>
            <el-main>
                <el-row :gutter="20" style="min-height: 700px;">
                    <!-- 左侧：患者表格 -->
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
                            <div class="table-main-header">患者信息</div>
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
                                <el-table-column align="center" label="操作" width="80" fixed="right">
                                    <template #default="scope">
                                        <el-button link size="small" style="color: #ec407a"
                                            @click.stop="openAddDrugOrder(scope.row)">
                                            开药
                                        </el-button>
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

                    <!-- 右侧：开药记录表 -->
                    <el-col :span="14">
                        <div class="table-main cost-panel" style="height: 100%;">
                            <div class="table-main-header">开药记录</div>
                            <el-table :data="drugOrderList" size="small" stripe empty-text="暂无开药记录">
                                <el-table-column prop="drugName" label="药品名称" />
                                <el-table-column prop="orderTime" label="开药时间" />
                                <el-table-column prop="quantity" label="数量" />
                                <el-table-column prop="specification" label="规格" />
                                <el-table-column prop="unit" label="单位" />
                                <el-table-column label="操作" width="100" fixed="right">
                                    <template #default="scope">
                                        <el-button link size="small" style="color: #1976d2"
                                            @click.stop="openEditDrugOrder(scope.row)">
                                            编辑
                                        </el-button>
                                        <el-button link size="small" style="color: #e53935"
                                            @click.stop="handleDeleteDrugOrder(scope.row)">
                                            删除
                                        </el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                            <el-pagination v-model:current-page="drugOrderPage.currentPage"
                                :page-size="drugOrderPage.pageSize" :total="drugOrderPage.total"
                                layout="prev, pager, next, total" @current-change="loadDrugOrderList"
                                style="margin-top: 10px" />
                        </div>
                    </el-col>
                </el-row>
            </el-main>
        </el-container>



        <!-- 添加/编辑开药弹窗 -->
        <el-dialog :title="isEditDrugOrder ? '编辑开药' : '添加开药'" v-model="addDrugOrderDialogVisible" width="900px">
            <el-row :gutter="20">
                <!-- 左侧：患者信息+开药表单 -->
                <el-col :span="10">
                    <div class="patient-info-panel">
                        <div class="patient-info-title">患者信息</div>
                        <div class="patient-info-item"><b>姓名：</b>{{ currentPatient?.name || '--' }}</div>
                        <div class="patient-info-item"><b>性别：</b>{{ currentPatient?.gender === 0 ? '女' :
                            currentPatient?.gender
                                === 1 ? '男' : '未知' }}</div>
                        <div class="patient-info-item"><b>住院号：</b>{{ currentPatient?.inpatientNo || '--' }}</div>
                        <div class="patient-info-item"><b>身份证号：</b>{{ currentPatient?.idCard || '--' }}</div>
                        <div class="patient-info-item"><b>联系方式：</b>{{ currentPatient?.contactPhone || '--' }}</div>
                        <el-divider />
                        <el-form :model="addDrugOrderForm" label-width="90px">
                            <el-form-item label="药品数量">
                                <el-input-number v-model="addDrugOrderForm.quantity" :min="1" />
                            </el-form-item>
                            <el-form-item label="开药时间">
                                <el-date-picker v-model="addDrugOrderForm.orderTime" type="datetime" placeholder="选择时间"
                                    style="width: 100%;" value-format="YYYY-MM-DD HH:mm:ss" />
                            </el-form-item>
                            <el-form-item label="开单医生">
                                <el-input v-model="addDrugOrderForm.doctorName" placeholder="请输入医生姓名" />
                            </el-form-item>
                        </el-form>
                    </div>
                </el-col>
                <!-- 右侧：药品选择表格 -->
                <el-col :span="14">
                    <div class="drug-select-panel">
                        <div style="display: flex; align-items: center; margin-bottom: 10px;">
                            <el-input v-model="drugQuery" placeholder="请输入药品名称查询" clearable
                                style="width: 220px; margin-right: 10px;" @keyup.enter="drugQuerySearch" />
                            <el-button type="primary" @click="drugQuerySearch">查询</el-button>
                        </div>
                        <div v-if="addDrugOrderForm.drugId" class="selected-drug-tip"
                            style="margin-bottom: 10px; color: #ad1457;">
                            当前选中药品：<b>{{ addDrugOrderForm.drugName }}</b>
                        </div>
                        <el-table :data="drugTableList" size="small" stripe highlight-current-row
                            @row-click="handleSelectDrug" :row-class-name="drugRowClass"
                            style="max-height: 320px; overflow-y: auto;">
                            <el-table-column prop="drugName" label="药品名称" />
                            <el-table-column prop="specification" label="规格" />
                            <el-table-column prop="unit" label="单位" />
                        </el-table>
                        <el-pagination :page-size="drugPage.pageSize" :current-page="drugPage.currentPage"
                            :total="drugPage.total" layout="prev, pager, next, total"
                            @current-change="handleDrugPageChange" style="margin-top: 10px" />
                    </div>
                </el-col>
            </el-row>
            <template #footer>
                <el-button @click="addDrugOrderDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleAddDrugOrder">{{ isEditDrugOrder ? '确认修改' : '确认添加'
                    }}</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { selectInsurederPage } from "@/api/insurederApi.js";
import { getDrugPage } from "@/api/drugApi.js";
import { getDrugOrderPage, addDrugOrder, updateDrugOrder, removeDrugOrder } from "@/api/drugOrderApi.js";

// 查询参数
const queryParams = reactive({
    customerName: "",
});

// 患者分页
const page = reactive({
    total: 0,
    pageSize: 6,
    currentPage: 1,
});
const condition = reactive({
    name: "",
    page: 1,
});

// 患者数据
const customerInfoList = ref([]);
const currentInsurederId = ref(null);
const currentPatient = ref(null);



// 开药记录分页
const drugOrderList = ref([]);
const drugOrderPage = reactive({
    total: 0,
    pageSize: 5,
    currentPage: 1,
});




// 药品表格
const drugTableList = ref([]);
const drugPage = reactive({
    total: 0,
    pageSize: 5,
    currentPage: 1,
});
const drugQuery = ref("");


// 添加/编辑开药弹窗
const addDrugOrderDialogVisible = ref(false);
const isEditDrugOrder = ref(false);
const editingDrugOrderId = ref(null);
const addDrugOrderForm = reactive({
    drugId: null,
    drugName: "",
    quantity: 1,
    orderTime: "",
    doctorName: "",
});



// 药品表选中高亮
function drugRowClass({ row }) {
    return row.id === addDrugOrderForm.drugId ? 'selected-drug-row' : '';
}

// 加载患者列表
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
            // 默认选中第一个患者
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

// 选择患者，加载诊断表
const handleRowClick = (row) => {
    currentInsurederId.value = row.id;
    currentPatient.value = row;
    loadDrugOrderList(1);
};



// 查询开药记录
const loadDrugOrderList = async (pageNum = 1) => {
    drugOrderPage.currentPage = pageNum;
    try {
        const res = await getDrugOrderPage({
            patientId: currentInsurederId.value,
            page: drugOrderPage.currentPage,
            pageSize: drugOrderPage.pageSize,
        });
        if (res.flag) {
            drugOrderList.value = res.data.records;
            drugOrderPage.total = res.data.total;
            drugOrderPage.pageSize = res.data.size;
            drugOrderPage.currentPage = res.data.current;
        } else {
            drugOrderList.value = [];
            drugOrderPage.total = 0;
            ElMessage.error(res.message || "开药记录加载失败");
        }
    } catch (err) {
        drugOrderList.value = [];
        drugOrderPage.total = 0;
        ElMessage.error("网络错误，请重试");
    }
};

// 查询药品表
const loadDrugList = async () => {
    try {
        const res = await getDrugPage({
            drugName: drugQuery.value,
            page: drugPage.currentPage,
            pageSize: drugPage.pageSize,
        });
        if (res.flag) {
            drugTableList.value = res.data.records;
            drugPage.total = res.data.total;
            drugPage.pageSize = res.data.size;
            drugPage.currentPage = res.data.current;
        } else {
            drugTableList.value = [];
            drugPage.total = 0;
            ElMessage.error(res.message || "药品数据加载失败");
        }
    } catch (err) {
        drugTableList.value = [];
        drugPage.total = 0;
        ElMessage.error("网络错误，请重试");
    }
};

// 药品查询
const drugQuerySearch = () => {
    drugPage.currentPage = 1;
    loadDrugList();
};

// 药品分页
const handleDrugPageChange = (curPage) => {
    drugPage.currentPage = curPage;
    loadDrugList();
};

// 选择药品
const handleSelectDrug = (row) => {
    addDrugOrderForm.drugId = row.id;
    addDrugOrderForm.drugName = row.drugName;
};


// 打开添加开药弹窗
const openAddDrugOrder = (row) => {
    // currentInsurederId.value = row.id; // 如需指定患者
    isEditDrugOrder.value = false;
    editingDrugOrderId.value = null;
    addDrugOrderForm.drugId = null;
    addDrugOrderForm.drugName = "";
    addDrugOrderForm.quantity = 1;
    addDrugOrderForm.orderTime = "";
    addDrugOrderForm.doctorName = "";
    drugPage.currentPage = 1;
    loadDrugList();
    addDrugOrderDialogVisible.value = true;
};

// 打开编辑开药弹窗
const openEditDrugOrder = (order) => {
    isEditDrugOrder.value = true;
    editingDrugOrderId.value = order.id;
    addDrugOrderForm.drugId = order.drugId;
    addDrugOrderForm.drugName = order.drugName;
    addDrugOrderForm.quantity = order.quantity;
    addDrugOrderForm.orderTime = order.orderTime;
    addDrugOrderForm.doctorName = order.doctorName;
    drugPage.currentPage = 1;
    loadDrugList();
    addDrugOrderDialogVisible.value = true;
};

// 添加/编辑开药确认
const handleAddDrugOrder = async () => {
    if (!addDrugOrderForm.drugId) {
        ElMessage.warning("请选择药品");
        return;
    }
    let dto = {
        ...addDrugOrderForm,
        patientId: currentInsurederId.value,
        id: isEditDrugOrder.value ? editingDrugOrderId.value : undefined,
    };
    let res;
    if (isEditDrugOrder.value) {
        res = await updateDrugOrder(dto);
    } else {
        res = await addDrugOrder(dto);
    }
    if (res.flag) {
        ElMessage.success(isEditDrugOrder.value ? "修改成功" : "添加成功");
        addDrugOrderDialogVisible.value = false;
        loadDrugOrderList(1);
    } else {
        ElMessage.error(res.message || (isEditDrugOrder.value ? "修改失败" : "添加失败"));
    }
};

// 删除开药记录
const handleDeleteDrugOrder = (order) => {
    ElMessageBox.confirm('确定要删除该开药记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
    }).then(async () => {
        try {
            const res = await removeDrugOrder({ id: order.id });
            if (res.flag) {
                ElMessage.success("删除成功");
                loadDrugOrderList(1);
            } else {
                ElMessage.error(res.message || "删除失败");
            }
        } catch (err) {
            ElMessage.error("网络错误，请重试");
        }
    });
};


// 初始化
onMounted(() => {
    loadCustomerList();
    loadDrugList();
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

.el-table {
    background: #fff0f6;
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

.patient-info-panel {
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 1px 4px #f8bbd0;
    padding: 18px 12px 8px 12px;
    margin-bottom: 10px;
}

.patient-info-title {
    font-size: 16px;
    color: #ec407a;
    font-weight: bold;
    margin-bottom: 8px;
}

.patient-info-item {
    font-size: 14px;
    color: #333;
    margin-bottom: 4px;
}

.disease-select-panel {
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 1px 4px #f8bbd0;
    padding: 18px 12px 8px 12px;
    min-height: 380px;
}

.drug-select-panel {
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 1px 4px #f8bbd0;
    padding: 18px 12px 8px 12px;
    min-height: 380px;
}

.selected-disease-row {
    background: #f8bbd0 !important;
}

.selected-drug-row {
    background: #bbdefb !important;
}
</style>