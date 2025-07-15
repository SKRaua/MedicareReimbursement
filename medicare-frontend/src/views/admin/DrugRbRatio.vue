<template>
    <div class="common-layout">
        <el-container>
            <el-header>
                <el-row :gutter="30">
                    <el-col :span="7">
                        <el-input placeholder="请输入药品类型查询" v-model="queryParams.drugType" @clear="query" clearable
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
                            添加比例
                        </el-button>
                    </el-col>
                </el-row>
            </el-header>
            <el-divider style="margin: 0"></el-divider>
            <el-main>
                <el-row :gutter="20">
                    <!-- 药品报销比例编辑 -->
                    <el-col :span="14">
                        <div class="table-main">
                            <div class="table-main-header">药品报销比例</div>
                            <el-table :data="ratioList" highlight-current-row size="small"
                                style="width: 100%; color: black" stripe>
                                <el-table-column align="center" fixed type="index" label="序号" width="60"
                                    :index="(i) => (page.currentPage - 1) * page.pageSize + i + 1" />
                                <el-table-column align="center" prop="drugType" label="药品类型" min-width="90" />
                                <el-table-column align="center" prop="drugRatio" label="报销比例(%)" min-width="100" />
                                <el-table-column align="center" prop="status" label="状态" min-width="80">
                                    <template #default="scope">
                                        <el-tag :type="scope.row.status === '启用' ? 'success' : 'info'">
                                            {{ scope.row.status === '启用' ? '启用' : '停用' }}
                                        </el-tag>
                                    </template>
                                </el-table-column>
                                <el-table-column align="center" prop="effectiveDate" label="生效日期" min-width="120" />
                                <el-table-column align="center" label="操作" width="100" fixed="right">
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
                    <!-- 药品报销比例展示 -->
                    <el-col :span="10">
                        <div class="table-main">
                            <div class="table-main-header">当前启用药品类型报销比例</div>
                            <el-card v-for="item in enabledRatios" :key="item.drugType" class="rb-progress-card"
                                shadow="hover">
                                <div class="rb-progress-label">{{ item.drugType }}</div>
                                <el-progress :percentage="item.drugRatio" :text-inside="true" :stroke-width="24"
                                    :color="getDrugTypeColor(item.drugType)" />
                            </el-card>
                            <div v-if="enabledRatios.length === 0" style="color: #999; margin-top: 20px;">暂无启用药品类型</div>
                        </div>
                    </el-col>
                </el-row>
            </el-main>
        </el-container>

        <!-- 新增/编辑弹窗 -->
        <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px" :close-on-click-modal="false">
            <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
                <el-form-item label="药品类型" prop="drugType">
                    <el-select v-model="form.drugType" placeholder="请选择">
                        <el-option label="甲类" value="甲类" />
                        <el-option label="乙类" value="乙类" />
                        <el-option label="丙类" value="丙类" />
                    </el-select>
                </el-form-item>
                <el-form-item label="报销比例(%)" prop="drugRatio">
                    <el-input-number v-model="form.drugRatio" :min="0" :max="100" />
                </el-form-item>
                <el-form-item label="状态" prop="status">
                    <el-select v-model="form.status" placeholder="请选择">
                        <el-option label="启用" value="启用" />
                        <el-option label="停用" value="停用" />
                    </el-select>
                </el-form-item>
                <el-form-item label="生效日期" prop="effectiveDate">
                    <el-date-picker v-model="form.effectiveDate" type="date" value-format="YYYY-MM-DD"
                        placeholder="选择日期" />
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleSubmit">确定</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import {
    getDrugReimbursementRatioPage,
    addDrugReimbursementRatio,
    updateDrugReimbursementRatio,
    removeDrugReimbursementRatio,
} from "@/api/drugReimbursementRatioApi.js";

const queryParams = reactive({
    drugType: "",
    status: "",
});
const page = reactive({
    total: 0,
    pageSize: 6,
    currentPage: 1,
});
const condition = reactive({
    drugType: "",
    status: "",
    page: 1,
});
const ratioList = ref([]);
const dialogVisible = ref(false);
const dialogTitle = ref("新增比例");
const form = reactive({
    id: null,
    drugType: "",
    drugRatio: 0,
    status: "",
    effectiveDate: "",
});
const formRef = ref(null);
const rules = {
    drugType: [{ required: true, message: "请选择药品类型", trigger: "change" }],
    drugRatio: [{ required: true, message: "请输入报销比例", trigger: "blur" }],
    status: [{ required: true, message: "请选择状态", trigger: "change" }],
    effectiveDate: [{ required: false, message: "请选择生效日期", trigger: "change" }],
};

const loadRatioList = async () => {
    condition.drugType = queryParams.drugType;
    condition.status = queryParams.status;
    condition.page = page.currentPage;
    try {
        const res = await getDrugReimbursementRatioPage(condition);
        if (res.flag) {
            ratioList.value = res.data.records;
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
const query = () => {
    page.currentPage = 1;
    loadRatioList();
};
const handleCurrentChange = (curPage) => {
    page.currentPage = curPage;
    loadRatioList();
};
const openAdd = () => {
    dialogTitle.value = "新增比例";
    Object.assign(form, {
        id: null,
        drugType: "",
        drugRatio: 0,
        status: "",
        effectiveDate: "",
    });
    dialogVisible.value = true;
};
const openEdit = (row) => {
    dialogTitle.value = "编辑比例";
    Object.assign(form, row);
    dialogVisible.value = true;
};
const handleSubmit = () => {
    formRef.value.validate(async (valid) => {
        if (!valid) return;
        try {
            let res;
            if (form.id) {
                res = await updateDrugReimbursementRatio(form);
            } else {
                res = await addDrugReimbursementRatio(form);
            }
            if (res.flag) {
                ElMessage.success(res.message || "操作成功");
                dialogVisible.value = false;
                loadRatioList();
            } else {
                ElMessage.error(res.message || "操作失败");
            }
        } catch (err) {
            ElMessage.error("操作失败，请重试");
        }
    });
};
const handleDelete = (row) => {
    ElMessageBox.confirm("确定要删除该比例吗？", "删除确认", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
    })
        .then(async () => {
            try {
                const res = await removeDrugReimbursementRatio({ id: row.id });
                if (res.flag) {
                    ElMessage.success("删除成功");
                    loadRatioList();
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
const enabledRatios = computed(() => {
    // 只取启用状态的三种药品类型
    return ratioList.value.filter(r => r.status === "启用");
});
function getDrugTypeColor(type) {
    // 可自定义不同药品类型的进度条颜色
    if (type === "甲类") return "#67c23a";
    if (type === "乙类") return "#409eff";
    if (type === "丙类") return "#e6a23c";
    return "#909399";
}
onMounted(() => {
    loadRatioList();
});
</script>

<style scoped>
.table-main {
    background: #ffe4ef;
    border-radius: 12px;
    box-shadow: 0 2px 8px #f8bbd0;
    padding: 18px 12px 12px 12px;
    min-height: 500px;
    box-sizing: border-box;
    overflow: hidden;
    /* 不再设置width/min-width，继承父容器宽度 */
}

.table-main-header {
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

.rb-progress-card {
    margin-bottom: 18px;
}

.rb-progress-label {
    font-size: 16px;
    color: #409eff;
    margin-bottom: 8px;
}
</style>