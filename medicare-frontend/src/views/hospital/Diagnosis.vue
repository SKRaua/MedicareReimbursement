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
                                            @click.stop="openAddDiagnosis(scope.row)">
                                            添加诊断
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

                    <!-- 右侧：诊断表 -->
                    <el-col :span="14">
                        <div class="table-main cost-panel" style="height: 100%;">
                            <div class="table-main-header">诊断信息</div>
                            <el-table :data="diagnosisList" size="small" stripe empty-text="暂无诊断数据">
                                <el-table-column prop="diagnosisType" label="诊断类型" />
                                <el-table-column prop="diagnosisDate" label="诊断日期" />
                                <el-table-column prop="diseaseName" label="疾病名称" />
                                <el-table-column prop="diagnoseDoctor" label="诊断医生" />
                                <el-table-column prop="remark" label="诊断说明" />
                                <el-table-column label="操作" width="100" fixed="right">
                                    <template #default="scope">
                                        <el-button link size="small" style="color: #1976d2"
                                            @click.stop="openEditDiagnosis(scope.row)">
                                            编辑
                                        </el-button>
                                        <el-button link size="small" style="color: #e53935"
                                            @click.stop="handleDeleteDiagnosis(scope.row)">
                                            删除
                                        </el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                            <el-pagination v-model:current-page="diagnosisPage.currentPage"
                                :page-size="diagnosisPage.pageSize" :total="diagnosisPage.total"
                                layout="prev, pager, next, total" @current-change="loadDiagnosisList"
                                style="margin-top: 10px" />
                        </div>
                    </el-col>
                </el-row>
            </el-main>
        </el-container>

        <!-- 添加/编辑诊断弹窗 -->
        <el-dialog :title="isEditDiagnosis ? '编辑诊断' : '添加诊断'" v-model="addDiagnosisDialogVisible" width="900px">
            <el-row :gutter="20">
                <!-- 左侧：患者信息+诊断选项 -->
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
                        <el-form :model="addDiagnosisForm" label-width="90px">
                            <el-form-item label="诊断类型">
                                <el-select v-model="addDiagnosisForm.diagnosisType" placeholder="请选择类型">
                                    <el-option label="初诊" value="初诊" />
                                    <el-option label="复诊" value="复诊" />
                                </el-select>
                            </el-form-item>
                            <el-form-item label="诊断日期">
                                <el-date-picker v-model="addDiagnosisForm.diagnosisDate" type="date" placeholder="选择日期"
                                    style="width: 100%;" />
                            </el-form-item>
                            <el-form-item label="诊断医生">
                                <el-input v-model="addDiagnosisForm.diagnoseDoctor" placeholder="请输入医生姓名" />
                            </el-form-item>
                            <el-form-item label="诊断说明">
                                <el-input v-model="addDiagnosisForm.remark" type="textarea" placeholder="请输入说明" />
                            </el-form-item>
                        </el-form>
                    </div>
                </el-col>
                <!-- 右侧：疾病选择表格 -->
                <el-col :span="14">
                    <div class="disease-select-panel">
                        <div style="display: flex; align-items: center; margin-bottom: 10px;">
                            <el-input v-model="diseaseQuery" placeholder="请输入疾病名称查询" clearable
                                style="width: 220px; margin-right: 10px;" @keyup.enter="diseaseQuerySearch" />
                            <el-button type="primary" @click="diseaseQuerySearch">查询</el-button>
                        </div>
                        <!-- 选中疾病提示 -->
                        <div v-if="addDiagnosisForm.diseaseId" class="selected-disease-tip"
                            style="margin-bottom: 10px; color: #ad1457;">
                            诊断：
                            <b>{{ addDiagnosisForm.diseaseName }}</b>
                        </div>
                        <el-table :data="diseaseTableList" size="small" stripe highlight-current-row
                            @row-click="handleSelectDisease" :row-class-name="diseaseRowClass"
                            style="max-height: 320px; overflow-y: auto;">
                            <el-table-column prop="diseaseName" label="疾病名称" />
                            <el-table-column prop="diseaseType" label="类型" />
                            <el-table-column prop="icdCode" label="ICD编码" />
                        </el-table>
                        <el-pagination :page-size="diseasePage.pageSize" :current-page="diseasePage.currentPage"
                            :total="diseasePage.total" layout="prev, pager, next, total"
                            @current-change="handleDiseasePageChange" style="margin-top: 10px" />
                    </div>
                </el-col>
            </el-row>
            <template #footer>
                <el-button @click="addDiagnosisDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleAddDiagnosis">{{ isEditDiagnosis ? '确认修改' : '确认添加'
                    }}</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { selectInsurederPage } from "@/api/insurederApi.js";
import { getDiseasePage } from "@/api/diseaseApi.js";
import { getDiagnosisPage, addDiagnosis, updateDiagnosis } from "@/api/diagnosisApi.js";
import { removeDiagnosis } from "@/api/diagnosisApi.js";

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

// 诊断表数据
const diagnosisList = ref([]);
const diagnosisPage = reactive({
    total: 0,
    pageSize: 5,
    currentPage: 1,
});

// 疾病表数据
const diseaseTableList = ref([]);
const diseasePage = reactive({
    total: 0,
    pageSize: 5,
    currentPage: 1,
});
const diseaseQuery = ref("");

// 添加/编辑诊断弹窗
const addDiagnosisDialogVisible = ref(false);
const isEditDiagnosis = ref(false);
const editingDiagnosisId = ref(null);
const addDiagnosisForm = reactive({
    diseaseId: null,
    diseaseName: "",
    diagnosisType: "",
    diagnosisDate: "",
    diagnoseDoctor: "",
    remark: "",
});

// 疾病表选中高亮
function diseaseRowClass({ row }) {
    return row.id === addDiagnosisForm.diseaseId ? 'selected-disease-row' : '';
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
    loadDiagnosisList(1);
};

// 查询诊断
const loadDiagnosisList = async (pageNum = 1) => {
    diagnosisPage.currentPage = pageNum;
    try {
        const res = await getDiagnosisPage({
            patientId: currentInsurederId.value,
            page: diagnosisPage.currentPage,
            pageSize: diagnosisPage.pageSize,
        });
        if (res.flag) {
            diagnosisList.value = res.data.records;
            diagnosisPage.total = res.data.total;
            diagnosisPage.pageSize = res.data.size;
            diagnosisPage.currentPage = res.data.current;
        } else {
            diagnosisList.value = [];
            diagnosisPage.total = 0;
            ElMessage.error(res.message || "诊断数据加载失败");
        }
    } catch (err) {
        diagnosisList.value = [];
        diagnosisPage.total = 0;
        ElMessage.error("网络错误，请重试");
    }
};

// 打开添加诊断弹窗
const openAddDiagnosis = (row) => {
    currentInsurederId.value = row.id;
    currentPatient.value = row;
    isEditDiagnosis.value = false;
    editingDiagnosisId.value = null;
    addDiagnosisForm.diseaseId = null;
    addDiagnosisForm.diseaseName = "";
    addDiagnosisForm.diagnosisType = "";
    addDiagnosisForm.diagnosisDate = "";
    addDiagnosisForm.diagnoseDoctor = "";
    addDiagnosisForm.remark = "";
    diseasePage.currentPage = 1; // 查询第一页
    loadDiseaseList();
    addDiagnosisDialogVisible.value = true;
};

// 打开编辑诊断弹窗
const openEditDiagnosis = (diagnosis) => {
    isEditDiagnosis.value = true;
    editingDiagnosisId.value = diagnosis.id;
    addDiagnosisForm.diseaseId = diagnosis.diseaseId;
    addDiagnosisForm.diseaseName = diagnosis.diseaseName;
    addDiagnosisForm.diagnosisType = diagnosis.diagnosisType;
    addDiagnosisForm.diagnosisDate = diagnosis.diagnosisDate;
    addDiagnosisForm.diagnoseDoctor = diagnosis.diagnoseDoctor;
    addDiagnosisForm.remark = diagnosis.remark;
    diseasePage.currentPage = 1; // 查询第一页
    loadDiseaseList();
    addDiagnosisDialogVisible.value = true;
};

// 删除诊断
const handleDeleteDiagnosis = (diagnosis) => {
    ElMessageBox.confirm('确定要删除该诊断记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
    }).then(async () => {
        try {
            const res = await removeDiagnosis({ id: diagnosis.id });
            if (res.flag) {
                ElMessage.success("删除成功");
                loadDiagnosisList(1);
            } else {
                ElMessage.error(res.message || "删除失败");
            }
        } catch (err) {
            ElMessage.error("网络错误，请重试");
        }
    });
};

// 疾病表格相关
const loadDiseaseList = async () => {
    try {
        const res = await getDiseasePage({
            diseaseName: diseaseQuery.value,
            page: diseasePage.currentPage,
            pageSize: diseasePage.pageSize,
        });
        if (res.flag) {
            diseaseTableList.value = res.data.records;
            diseasePage.total = res.data.total;
            diseasePage.pageSize = res.data.size;
            diseasePage.currentPage = res.data.current;
        } else {
            diseaseTableList.value = [];
            diseasePage.total = 0;
            ElMessage.error(res.message || "疾病数据加载失败");
        }
    } catch (err) {
        diseaseTableList.value = [];
        diseasePage.total = 0;
        ElMessage.error("网络错误，请重试");
    }
};

// 疾病查询
const diseaseQuerySearch = () => {
    diseasePage.currentPage = 1;
    loadDiseaseList();
};

// 疾病分页
const handleDiseasePageChange = (curPage) => {
    diseasePage.currentPage = curPage;
    loadDiseaseList();
};

// 选择疾病
const handleSelectDisease = (row) => {
    addDiagnosisForm.diseaseId = row.id;
    addDiagnosisForm.diseaseName = row.diseaseName;
};

// 添加/编辑诊断确认
const handleAddDiagnosis = async () => {
    if (!addDiagnosisForm.diseaseId) {
        ElMessage.warning("请选择疾病");
        return;
    }
    let dto = {
        ...addDiagnosisForm,
        patientId: currentInsurederId.value,
        id: isEditDiagnosis.value ? editingDiagnosisId.value : undefined,
    };
    let res;
    if (isEditDiagnosis.value) {
        res = await updateDiagnosis(dto);
    } else {
        res = await addDiagnosis(dto);
    }
    if (res.flag) {
        ElMessage.success(isEditDiagnosis.value ? "修改成功" : "添加成功");
        addDiagnosisDialogVisible.value = false;
        loadDiagnosisList(1);
    } else {
        ElMessage.error(res.message || (isEditDiagnosis.value ? "修改失败" : "添加失败"));
    }
};

// 初始化
onMounted(() => {
    loadCustomerList();
    loadDiseaseList();
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