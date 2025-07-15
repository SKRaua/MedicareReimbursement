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
                                        <el-button link size="small" style="color: #1976d2"
                                            @click.stop="openAddMedicalServiceOrder(scope.row)">
                                            添加服务
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

                    <!-- 右侧：医疗服务订单表 -->
                    <el-col :span="14">
                        <div class="table-main cost-panel" style="height: 100%;">
                            <div class="table-main-header">医疗服务记录</div>
                            <el-table :data="medicalServiceOrderList" size="small" stripe empty-text="暂无医疗服务记录">
                                <el-table-column prop="itemName" label="服务名称" />
                                <el-table-column prop="orderTime" label="开单时间" />
                                <el-table-column prop="quantity" label="数量" />
                                <el-table-column prop="unit" label="单位" />
                                <el-table-column label="操作" width="100" fixed="right">
                                    <template #default="scope">
                                        <el-button link size="small" style="color: #1976d2"
                                            @click.stop="openEditMedicalServiceOrder(scope.row)">
                                            编辑
                                        </el-button>
                                        <el-button link size="small" style="color: #e53935"
                                            @click.stop="handleDeleteMedicalServiceOrder(scope.row)">
                                            删除
                                        </el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                            <el-pagination v-model:current-page="medicalServiceOrderPage.currentPage"
                                :page-size="medicalServiceOrderPage.pageSize" :total="medicalServiceOrderPage.total"
                                layout="prev, pager, next, total" @current-change="loadMedicalServiceOrderList"
                                style="margin-top: 10px" />
                        </div>
                    </el-col>
                </el-row>
            </el-main>
        </el-container>

        <!-- 添加/编辑医疗服务弹窗 -->
        <el-dialog :title="isEditMedicalServiceOrder ? '编辑医疗服务' : '添加医疗服务'"
            v-model="addMedicalServiceOrderDialogVisible" width="900px">
            <el-row :gutter="20">
                <!-- 左侧：患者信息+表单 -->
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
                        <el-form :model="addMedicalServiceOrderForm" label-width="90px" :rules="formRules">
                            <el-form-item label="服务数量">
                                <el-input-number v-model="addMedicalServiceOrderForm.quantity" :min="1" />
                            </el-form-item>
                            <el-form-item label="开单时间" prop="orderTime">
                                <el-date-picker v-model="addMedicalServiceOrderForm.orderTime" type="datetime"
                                    placeholder="选择时间" style="width: 100%;" value-format="YYYY-MM-DD HH:mm:ss" />
                            </el-form-item>
                            <el-form-item label="开单医生">
                                <el-input v-model="addMedicalServiceOrderForm.doctorName" placeholder="请输入医生姓名" />
                            </el-form-item>
                            <el-form-item label="执行时间" prop="executeTime">
                                <el-date-picker v-model="addMedicalServiceOrderForm.executeTime" type="datetime"
                                    width="100%;" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择执行时间" />
                            </el-form-item>
                            <el-form-item label="执行护士" prop="nurseName">
                                <el-input v-model="addMedicalServiceOrderForm.nurseName" placeholder="请输入护士姓名" />
                            </el-form-item>

                        </el-form>
                    </div>
                </el-col>
                <!-- 右侧：医疗服务选择表格 -->
                <el-col :span="14">
                    <div class="disease-select-panel">
                        <div style="display: flex; align-items: center; margin-bottom: 10px;">
                            <el-input v-model="medicalServiceQuery" placeholder="请输入服务名称查询" clearable
                                style="width: 220px; margin-right: 10px;" @keyup.enter="medicalServiceQuerySearch" />
                            <el-button type="primary" @click="medicalServiceQuerySearch">查询</el-button>
                        </div>
                        <div v-if="addMedicalServiceOrderForm.itemId" class="selected-disease-tip"
                            style="margin-bottom: 10px; color: #ad1457;">
                            当前选中服务：<b>{{ addMedicalServiceOrderForm.itemName }}</b>
                        </div>
                        <el-table :data="medicalServiceTableList" size="small" stripe highlight-current-row
                            @row-click="handleSelectMedicalService" :row-class-name="medicalServiceRowClass"
                            style="max-height: 320px; overflow-y: auto;">
                            <el-table-column prop="itemName" label="服务名称" />
                            <el-table-column prop="unit" label="单位" />
                            <el-table-column prop="price" label="单价" />
                        </el-table>
                        <el-pagination :page-size="medicalServicePage.pageSize"
                            :current-page="medicalServicePage.currentPage" :total="medicalServicePage.total"
                            layout="prev, pager, next, total" @current-change="handleMedicalServicePageChange"
                            style="margin-top: 10px" />
                    </div>
                </el-col>
            </el-row>
            <template #footer>
                <el-button @click="addMedicalServiceOrderDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleAddMedicalServiceOrder">{{ isEditMedicalServiceOrder ? '确认修改' :
                    '确认添加'
                }}</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { selectInsurederPage } from "@/api/insurederApi.js";
import { getMedicalServiceOrderPage, addMedicalServiceOrder, updateMedicalServiceOrder, removeMedicalServiceOrder } from "@/api/medicalServiceOrderApi.js";
import { getMedicalServicePage } from "@/api/medicalServiceApi.js";

// 定义表单验证规则
const formRules = reactive({
    orderTime: [
        { required: true, message: '请选择开单时间', trigger: 'change' },
        {
            pattern: /^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/,
            message: '日期格式错误，请使用 yyyy-MM-dd HH:mm:ss',
            trigger: 'change'
        }
    ]
});

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

// 医疗服务订单数据
const medicalServiceOrderList = ref([]);
const medicalServiceOrderPage = reactive({
    total: 0,
    pageSize: 5,
    currentPage: 1,
});

// 医疗服务表数据
const medicalServiceTableList = ref([]);
const medicalServicePage = reactive({
    total: 0,
    pageSize: 5,
    currentPage: 1,
});
const medicalServiceQuery = ref("");

// 添加/编辑弹窗
const addMedicalServiceOrderDialogVisible = ref(false);
const isEditMedicalServiceOrder = ref(false);
const editingMedicalServiceOrderId = ref(null);
const addMedicalServiceOrderForm = reactive({
    itemId: null,
    itemName: "",
    quantity: 1,
    orderTime: "",
    doctorName: "",
    executeTime: "",
    nurseName: "",
});

// 医疗服务表选中高亮
function medicalServiceRowClass({ row }) {
    return row.id === addMedicalServiceOrderForm.itemId ? 'selected-disease-row' : '';
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

// 选择患者，加载医疗服务订单
const handleRowClick = (row) => {
    currentInsurederId.value = row.id;
    currentPatient.value = row;
    loadMedicalServiceOrderList(1);
};

// 查询医疗服务订单
const loadMedicalServiceOrderList = async (pageNum = 1) => {
    medicalServiceOrderPage.currentPage = pageNum;
    try {
        const res = await getMedicalServiceOrderPage({
            patientId: currentInsurederId.value,
            page: medicalServiceOrderPage.currentPage,
            pageSize: medicalServiceOrderPage.pageSize,
        });
        if (res.flag) {
            medicalServiceOrderList.value = res.data.records;
            medicalServiceOrderPage.total = res.data.total;
            medicalServiceOrderPage.pageSize = res.data.size;
            medicalServiceOrderPage.currentPage = res.data.current;
        } else {
            medicalServiceOrderList.value = [];
            medicalServiceOrderPage.total = 0;
            ElMessage.error(res.message || "医疗服务记录加载失败");
        }
    } catch (err) {
        medicalServiceOrderList.value = [];
        medicalServiceOrderPage.total = 0;
        ElMessage.error("网络错误，请重试");
    }
};

// 打开添加弹窗
const openAddMedicalServiceOrder = (row) => {
    currentInsurederId.value = row.id;
    currentPatient.value = row;
    isEditMedicalServiceOrder.value = false;
    editingMedicalServiceOrderId.value = null;
    addMedicalServiceOrderForm.itemId = null;
    addMedicalServiceOrderForm.itemName = "";
    addMedicalServiceOrderForm.quantity = 1;
    addMedicalServiceOrderForm.orderTime = "";
    addMedicalServiceOrderForm.doctorName = "";
    medicalServicePage.currentPage = 1;
    loadMedicalServiceList();
    addMedicalServiceOrderDialogVisible.value = true;
    // 延迟 0.1 秒设置默认值（等待组件初始化）
    setTimeout(() => {
        const now = new Date();
        // 格式化为 YYYY-MM-DD HH:mm:ss
        addMedicalServiceOrderForm.orderTime = now.toISOString().slice(0, 19).replace('T', ' ');
    }, 100);
};

// 打开编辑弹窗
const openEditMedicalServiceOrder = (order) => {
    isEditMedicalServiceOrder.value = true;
    editingMedicalServiceOrderId.value = order.id;
    addMedicalServiceOrderForm.itemId = order.itemId;
    addMedicalServiceOrderForm.itemName = order.itemName;
    addMedicalServiceOrderForm.quantity = order.quantity;
    addMedicalServiceOrderForm.orderTime = order.orderTime;
    addMedicalServiceOrderForm.doctorName = order.doctorName;
    medicalServicePage.currentPage = 1;
    loadMedicalServiceList();
    addMedicalServiceOrderDialogVisible.value = true;
    setTimeout(() => {
        if (order.orderTime) {
            addMedicalServiceOrderForm.orderTime = order.orderTime;
        } else {
            const now = new Date();
            addMedicalServiceOrderForm.orderTime = now.toISOString().slice(0, 19).replace('T', ' ');
        }
    }, 100);
};

// 删除医疗服务订单
const handleDeleteMedicalServiceOrder = (order) => {
    ElMessageBox.confirm('确定要删除该医疗服务记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
    }).then(async () => {
        try {
            const res = await removeMedicalServiceOrder({ id: order.id });
            if (res.flag) {
                ElMessage.success("删除成功");
                loadMedicalServiceOrderList(1);
            } else {
                ElMessage.error(res.message || "删除失败");
            }
        } catch (err) {
            ElMessage.error("网络错误，请重试");
        }
    });
};

// 医疗服务表格相关
const loadMedicalServiceList = async () => {
    try {
        const res = await getMedicalServicePage({
            itemName: medicalServiceQuery.value,
            page: medicalServicePage.currentPage,
            pageSize: medicalServicePage.pageSize,
        });
        if (res.flag) {
            medicalServiceTableList.value = res.data.records;
            medicalServicePage.total = res.data.total;
            medicalServicePage.pageSize = res.data.size;
            medicalServicePage.currentPage = res.data.current;
        } else {
            medicalServiceTableList.value = [];
            medicalServicePage.total = 0;
            ElMessage.error(res.message || "医疗服务数据加载失败");
        }
    } catch (err) {
        medicalServiceTableList.value = [];
        medicalServicePage.total = 0;
        ElMessage.error("网络错误，请重试");
    }
};

// 医疗服务查询
const medicalServiceQuerySearch = () => {
    medicalServicePage.currentPage = 1;
    loadMedicalServiceList();
};

// 医疗服务分页
const handleMedicalServicePageChange = (curPage) => {
    medicalServicePage.currentPage = curPage;
    loadMedicalServiceList();
};

// 选择医疗服务
const handleSelectMedicalService = (row) => {
    addMedicalServiceOrderForm.itemId = row.id;
    addMedicalServiceOrderForm.itemName = row.itemName;
};

// 添加/编辑确认
const handleAddMedicalServiceOrder = async () => {
    if (!addMedicalServiceOrderForm.itemId) {
        ElMessage.warning("请选择医疗服务");
        return;
    }
    let dto = {
        ...addMedicalServiceOrderForm,
        patientId: currentInsurederId.value,
        id: isEditMedicalServiceOrder.value ? editingMedicalServiceOrderId.value : undefined,
    };
    let res;
    if (isEditMedicalServiceOrder.value) {
        res = await updateMedicalServiceOrder(dto);
    } else {
        res = await addMedicalServiceOrder(dto);
    }
    if (res.flag) {
        ElMessage.success(isEditMedicalServiceOrder.value ? "修改成功" : "添加成功");
        addMedicalServiceOrderDialogVisible.value = false;
        loadMedicalServiceOrderList(1);
    } else {
        ElMessage.error(res.message || (isEditMedicalServiceOrder.value ? "修改失败" : "添加失败"));
    }
};

// 初始化
onMounted(() => {
    loadCustomerList();
    loadMedicalServiceList();
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

.selected-disease-row {
    background: #f8bbd0 !important;
}
</style>