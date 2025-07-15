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
                                            @click.stop="openAddTreatmentItemOrder(scope.row)">
                                            添加项目
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

                    <!-- 右侧：诊疗项目订单表 -->
                    <el-col :span="14">
                        <div class="table-main cost-panel" style="height: 100%;">
                            <div class="table-main-header">诊疗项目记录</div>
                            <el-table :data="treatmentItemOrderList" size="small" stripe empty-text="暂无诊疗项目记录">
                                <el-table-column prop="itemName" label="项目名称" />
                                <el-table-column prop="orderTime" label="开单时间" />
                                <el-table-column prop="quantity" label="数量" />
                                <el-table-column prop="unit" label="单位" />
                                <el-table-column label="操作" width="100" fixed="right">
                                    <template #default="scope">
                                        <el-button link size="small" style="color: #1976d2"
                                            @click.stop="openEditTreatmentItemOrder(scope.row)">
                                            编辑
                                        </el-button>
                                        <el-button link size="small" style="color: #e53935"
                                            @click.stop="handleDeleteTreatmentItemOrder(scope.row)">
                                            删除
                                        </el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                            <el-pagination v-model:current-page="treatmentItemOrderPage.currentPage"
                                :page-size="treatmentItemOrderPage.pageSize" :total="treatmentItemOrderPage.total"
                                layout="prev, pager, next, total" @current-change="loadTreatmentItemOrderList"
                                style="margin-top: 10px" />
                        </div>
                    </el-col>
                </el-row>
            </el-main>
        </el-container>

        <!-- 添加/编辑诊疗项目弹窗 -->
        <el-dialog :title="isEditTreatmentItemOrder ? '编辑诊疗项目' : '添加诊疗项目'" v-model="addTreatmentItemOrderDialogVisible"
            width="900px">
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
                        <el-form :model="addTreatmentItemOrderForm" label-width="90px">
                            <el-form-item label="项目数量">
                                <el-input-number v-model="addTreatmentItemOrderForm.quantity" :min="1" />
                            </el-form-item>
                            <el-form-item label="开单时间">
                                <el-date-picker v-model="addTreatmentItemOrderForm.orderTime" type="datetime"
                                    value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择时间" style="width: 100%;" />
                            </el-form-item>
                            <el-form-item label="开单医生">
                                <el-input v-model="addTreatmentItemOrderForm.doctorName" placeholder="请输入医生姓名" />
                            </el-form-item>
                            <el-form-item label="执行时间" prop="executeTime">
                                <el-date-picker v-model="addTreatmentItemOrderForm.executeTime" type="datetime"
                                    width="100%;" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择执行时间" />
                            </el-form-item>
                            <el-form-item label="执行护士" prop="nurseName">
                                <el-input v-model="addTreatmentItemOrderForm.nurseName" placeholder="请输入护士姓名" />
                            </el-form-item>
                        </el-form>
                    </div>
                </el-col>
                <!-- 右侧：诊疗项目选择表格 -->
                <el-col :span="14">
                    <div class="disease-select-panel">
                        <div style="display: flex; align-items: center; margin-bottom: 10px;">
                            <el-input v-model="treatmentItemQuery" placeholder="请输入项目名称查询" clearable
                                style="width: 220px; margin-right: 10px;" @keyup.enter="treatmentItemQuerySearch" />
                            <el-button type="primary" @click="treatmentItemQuerySearch">查询</el-button>
                        </div>
                        <div v-if="addTreatmentItemOrderForm.itemId" class="selected-disease-tip"
                            style="margin-bottom: 10px; color: #ad1457;">
                            当前选中项目：<b>{{ addTreatmentItemOrderForm.itemName }}</b>
                        </div>
                        <el-table :data="treatmentItemTableList" size="small" stripe highlight-current-row
                            @row-click="handleSelectTreatmentItem" :row-class-name="treatmentItemRowClass"
                            style="max-height: 320px; overflow-y: auto;">
                            <el-table-column prop="itemName" label="项目名称" />
                            <el-table-column prop="unit" label="单位" />
                            <el-table-column prop="price" label="单价" />
                        </el-table>
                        <el-pagination :page-size="treatmentItemPage.pageSize"
                            :current-page="treatmentItemPage.currentPage" :total="treatmentItemPage.total"
                            layout="prev, pager, next, total" @current-change="handleTreatmentItemPageChange"
                            style="margin-top: 10px" />
                    </div>
                </el-col>
            </el-row>
            <template #footer>
                <el-button @click="addTreatmentItemOrderDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleAddTreatmentItemOrder">{{ isEditTreatmentItemOrder ? '确认修改' :
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
import { getTreatmentItemOrderPage, addTreatmentItemOrder, updateTreatmentItemOrder, removeTreatmentItemOrder } from "@/api/treatmentItemOrderApi.js";
import { getTreatmentItemPage } from "@/api/treatmentItemApi.js";

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

// 诊疗项目订单数据
const treatmentItemOrderList = ref([]);
const treatmentItemOrderPage = reactive({
    total: 0,
    pageSize: 5,
    currentPage: 1,
});

// 诊疗项目表数据
const treatmentItemTableList = ref([]);
const treatmentItemPage = reactive({
    total: 0,
    pageSize: 5,
    currentPage: 1,
});
const treatmentItemQuery = ref("");

// 添加/编辑弹窗
const addTreatmentItemOrderDialogVisible = ref(false);
const isEditTreatmentItemOrder = ref(false);
const editingTreatmentItemOrderId = ref(null);
const addTreatmentItemOrderForm = reactive({
    itemId: null,
    itemName: "",
    quantity: 1,
    orderTime: "",
    doctorName: "",
});

// 诊疗项目表选中高亮
function treatmentItemRowClass({ row }) {
    return row.id === addTreatmentItemOrderForm.itemId ? 'selected-disease-row' : '';
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

// 选择患者，加载诊疗项目订单
const handleRowClick = (row) => {
    currentInsurederId.value = row.id;
    currentPatient.value = row;
    loadTreatmentItemOrderList(1);
};

// 查询诊疗项目订单
const loadTreatmentItemOrderList = async (pageNum = 1) => {
    treatmentItemOrderPage.currentPage = pageNum;
    try {
        const res = await getTreatmentItemOrderPage({
            patientId: currentInsurederId.value,
            page: treatmentItemOrderPage.currentPage,
            pageSize: treatmentItemOrderPage.pageSize,
        });
        if (res.flag) {
            treatmentItemOrderList.value = res.data.records;
            treatmentItemOrderPage.total = res.data.total;
            treatmentItemOrderPage.pageSize = res.data.size;
            treatmentItemOrderPage.currentPage = res.data.current;
        } else {
            treatmentItemOrderList.value = [];
            treatmentItemOrderPage.total = 0;
            ElMessage.error(res.message || "诊疗项目记录加载失败");
        }
    } catch (err) {
        treatmentItemOrderList.value = [];
        treatmentItemOrderPage.total = 0;
        ElMessage.error("网络错误，请重试");
    }
};

// 打开添加弹窗
const openAddTreatmentItemOrder = (row) => {
    currentInsurederId.value = row.id;
    currentPatient.value = row;
    isEditTreatmentItemOrder.value = false;
    editingTreatmentItemOrderId.value = null;
    addTreatmentItemOrderForm.itemId = null;
    addTreatmentItemOrderForm.itemName = "";
    addTreatmentItemOrderForm.quantity = 1;
    addTreatmentItemOrderForm.orderTime = "";
    addTreatmentItemOrderForm.doctorName = "";
    treatmentItemPage.currentPage = 1;
    loadTreatmentItemList();
    addTreatmentItemOrderDialogVisible.value = true;
};

// 打开编辑弹窗
const openEditTreatmentItemOrder = (order) => {
    isEditTreatmentItemOrder.value = true;
    editingTreatmentItemOrderId.value = order.id;
    addTreatmentItemOrderForm.itemId = order.itemId;
    addTreatmentItemOrderForm.itemName = order.itemName;
    addTreatmentItemOrderForm.quantity = order.quantity;
    addTreatmentItemOrderForm.orderTime = order.orderTime;
    addTreatmentItemOrderForm.doctorName = order.doctorName;
    treatmentItemPage.currentPage = 1;
    loadTreatmentItemList();
    addTreatmentItemOrderDialogVisible.value = true;
};

// 删除诊疗项目订单
const handleDeleteTreatmentItemOrder = (order) => {
    ElMessageBox.confirm('确定要删除该诊疗项目记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
    }).then(async () => {
        try {
            const res = await removeTreatmentItemOrder({ id: order.id });
            if (res.flag) {
                ElMessage.success("删除成功");
                loadTreatmentItemOrderList(1);
            } else {
                ElMessage.error(res.message || "删除失败");
            }
        } catch (err) {
            ElMessage.error("网络错误，请重试");
        }
    });
};

// 诊疗项目表格相关
const loadTreatmentItemList = async () => {
    try {
        const res = await getTreatmentItemPage({
            itemName: treatmentItemQuery.value,
            page: treatmentItemPage.currentPage,
            pageSize: treatmentItemPage.pageSize,
        });
        if (res.flag) {
            treatmentItemTableList.value = res.data.records;
            treatmentItemPage.total = res.data.total;
            treatmentItemPage.pageSize = res.data.size;
            treatmentItemPage.currentPage = res.data.current;
        } else {
            treatmentItemTableList.value = [];
            treatmentItemPage.total = 0;
            ElMessage.error(res.message || "诊疗项目数据加载失败");
        }
    } catch (err) {
        treatmentItemTableList.value = [];
        treatmentItemPage.total = 0;
        ElMessage.error("网络错误，请重试");
    }
};

// 诊疗项目查询
const treatmentItemQuerySearch = () => {
    treatmentItemPage.currentPage = 1;
    loadTreatmentItemList();
};

// 诊疗项目分页
const handleTreatmentItemPageChange = (curPage) => {
    treatmentItemPage.currentPage = curPage;
    loadTreatmentItemList();
};

// 选择诊疗项目
const handleSelectTreatmentItem = (row) => {
    addTreatmentItemOrderForm.itemId = row.id;
    addTreatmentItemOrderForm.itemName = row.itemName;
};

// 添加/编辑确认
const handleAddTreatmentItemOrder = async () => {
    if (!addTreatmentItemOrderForm.itemId) {
        ElMessage.warning("请选择诊疗项目");
        return;
    }
    let dto = {
        ...addTreatmentItemOrderForm,
        patientId: currentInsurederId.value,
        id: isEditTreatmentItemOrder.value ? editingTreatmentItemOrderId.value : undefined,
    };
    let res;
    if (isEditTreatmentItemOrder.value) {
        res = await updateTreatmentItemOrder(dto);
    } else {
        res = await addTreatmentItemOrder(dto);
    }
    if (res.flag) {
        ElMessage.success(isEditTreatmentItemOrder.value ? "修改成功" : "添加成功");
        addTreatmentItemOrderDialogVisible.value = false;
        loadTreatmentItemOrderList(1);
    } else {
        ElMessage.error(res.message || (isEditTreatmentItemOrder.value ? "修改失败" : "添加失败"));
    }
};

// 初始化
onMounted(() => {
    loadCustomerList();
    loadTreatmentItemList();
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