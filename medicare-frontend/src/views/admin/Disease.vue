<template>
    <div class="common-layout">
        <el-container>
            <el-header>
                <el-row :gutter="30">
                    <el-col :span="7">
                        <el-input placeholder="请输入疾病名称查询" v-model="queryParams.diseaseName" @clear="query" clearable
                            size="large" @keyup.enter="query">
                            <template #append>
                                <el-button type="info" @click="query" style="color: black">查询</el-button>
                            </template>
                        </el-input>
                    </el-col>
                    <el-col :span="2">
                        <el-button type="primary" @click="openAdd" size="large">
                            <el-icon>
                                <Plus />
                            </el-icon>
                            添加疾病
                        </el-button>
                    </el-col>
                </el-row>
            </el-header>
            <el-divider style="margin: 0"></el-divider>
            <el-main>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <div class="table-main">
                            <div class="table-main-header">疾病信息</div>
                            <el-table :data="diseaseList" highlight-current-row size="small"
                                style="width: 100%; color: black; min-width: 900px;" stripe>
                                <el-table-column align="center" fixed type="index" label="序号" width="60"
                                    :index="(i) => (page.currentPage - 1) * page.pageSize + i + 1" />
                                <el-table-column align="center" prop="diseaseName" label="疾病名称" width="140" />
                                <el-table-column align="center" prop="diseaseCode" label="疾病编码" width="140" />
                                <el-table-column align="center" prop="icdCode" label="ICD编码" width="140" />
                                <el-table-column align="center" prop="diseaseType" label="疾病类型" width="120" />
                                <el-table-column align="center" prop="remark" label="备注" />
                                <el-table-column align="center" label="操作" width="160" fixed="right">
                                    <template #default="scope">
                                        <el-button link size="small" @click="openEdit(scope.row)"
                                            style="color: #1890ff">
                                            编辑
                                        </el-button>
                                        <el-button link size="small" @click="handleDelete(scope.row)"
                                            style="color: #ff4d4f">
                                            删除
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
            <!-- 新增/编辑弹窗 -->
            <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px" :close-on-click-modal="false">
                <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
                    <el-form-item label="疾病名称" prop="diseaseName">
                        <el-input v-model="form.diseaseName" />
                    </el-form-item>
                    <el-form-item label="疾病编码" prop="diseaseCode">
                        <el-input v-model="form.diseaseCode" />
                    </el-form-item>
                    <el-form-item label="ICD编码" prop="icdCode">
                        <el-input v-model="form.icdCode" />
                    </el-form-item>
                    <el-form-item label="疾病类型" prop="diseaseType">
                        <el-input v-model="form.diseaseType" />
                    </el-form-item>
                    <el-form-item label="备注" prop="remark">
                        <el-input v-model="form.remark" />
                    </el-form-item>
                </el-form>
                <template #footer>
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="handleSubmit">确定</el-button>
                </template>
            </el-dialog>
        </el-container>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { getDiseasePage, addDisease, updateDisease, removeDisease } from "@/api/diseaseApi.js";
import { Plus } from "@element-plus/icons-vue";

// 查询参数
const queryParams = reactive({
    diseaseName: "",
});

// 分页和条件
const page = reactive({
    total: 0,
    pageSize: 6,
    currentPage: 1,
});
const condition = reactive({
    diseaseName: "",
    page: 1,
    pageSize: 6
});

// 数据列表
const diseaseList = ref([]);

// 加载数据
const loadDiseaseList = async () => {
    condition.diseaseName = queryParams.diseaseName;
    condition.page = page.currentPage;
    condition.pageSize = page.pageSize;
    try {
        const res = await getDiseasePage(condition);
        if (res.flag) {
            diseaseList.value = res.data.records;
            page.total = res.data.total;
            page.pageSize = res.data.size;
            page.currentPage = res.data.current;
            console.log("diseaseList======>",diseaseList.value)
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
    loadDiseaseList();
};

// 分页
const handleCurrentChange = (curPage) => {
    page.currentPage = curPage;
    loadDiseaseList();
};

// 初始化
onMounted(() => {
    loadDiseaseList();
});

const dialogVisible = ref(false);
const dialogTitle = ref("新增疾病");
const form = reactive({
    id: null,
    diseaseName: "",
    diseaseCode: "",
    icdCode: "",
    diseaseType: "",
    remark: "",
});
const formRef = ref(null);
const rules = {
    diseaseName: [{ required: true, message: "请输入疾病名称", trigger: "blur" }],
    diseaseCode: [{ required: true, message: "请输入疾病编码", trigger: "blur" }],
    icdCode: [{ required: true, message: "请输入ICD编码", trigger: "blur" }],
    diseaseType: [{ required: true, message: "请输入疾病类型", trigger: "blur" }],
};

// 打开新增弹窗
const openAdd = () => {
    dialogTitle.value = "新增疾病";
    Object.assign(form, {
        id: null,
        diseaseName: "",
        diseaseCode: "",
        icdCode: "",
        diseaseType: "",
        remark: "",
    });
    dialogVisible.value = true;
};

// 打开编辑弹窗
const openEdit = (row) => {
    dialogTitle.value = "编辑疾病";
    Object.assign(form, row);
    dialogVisible.value = true;
};

// 提交表单
const handleSubmit = () => {
    formRef.value.validate(async (valid) => {
        if (!valid) return;
        try {
            let res;
            if (form.id) {
                res = await updateDisease(form);
            } else {
                res = await addDisease(form);
            }
            if (res.flag) {
                ElMessage.success(res.message || "操作成功");
                dialogVisible.value = false;
                loadDiseaseList();
            } else {
                ElMessage.error(res.message || "操作失败");
            }
        } catch (err) {
            ElMessage.error("操作失败，请重试");
        }
    });
};

// 删除
const handleDelete = (row) => {
    ElMessageBox.confirm("确定要删除该疾病吗？", "删除确认", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
    })
        .then(async () => {
            try {
                const res = await removeDisease({ id: row.id });
                if (res.flag) {
                    ElMessage.success("删除成功");
                    loadDiseaseList();
                } else {
                    ElMessage.error(res.message || "删除失败");
                }
            } catch (err) {
                ElMessage.error("删除失败，请重试");
            }
        })
        .catch(() => {
            ElMessage.info("已取消删除");
        });
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
    color: #ad1457;
}

.el-table th {
    background: #f8bbd0 !important;
    color: #ad1457 !important;
}

.el-button--primary {
    background-color: #ec407a;
    border-color: #ec407a;
}

.el-button--primary:hover {
    background-color: #ad1457;
    border-color: #ad1457;
}

.el-pagination.is-background .el-pager li:not(.disabled).active {
    background-color: #ec407a;
    color: #fff;
}
</style>
