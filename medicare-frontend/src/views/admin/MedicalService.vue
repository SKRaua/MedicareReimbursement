<template>
    <div class="common-layout">
        <el-container>
            <el-header>
                <el-row :gutter="30">
                    <el-col :span="7">
                        <el-input placeholder="请输入服务名称查询" v-model="queryParams.itemName" @clear="query" clearable
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
                            添加服务
                        </el-button>
                    </el-col>
                </el-row>
            </el-header>
            <el-divider style="margin: 0"></el-divider>
            <el-main>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <div class="table-main">
                            <div class="table-main-header">医疗服务信息</div>
                            <el-table :data="serviceList" highlight-current-row size="small"
                                style="width: 100%; color: black" stripe>
                                <el-table-column align="center" fixed type="index" label="序号" width="60"
                                    :index="(i) => (page.currentPage - 1) * page.pageSize + i + 1" />
                                <el-table-column align="center" prop="itemName" label="服务名称" width="120" />
                                <el-table-column align="center" prop="itemCode" label="服务编码" width="100" />
                                <el-table-column align="center" prop="nationalCode" label="国家统一编码" width="120" />
                                <el-table-column align="center" prop="description" label="服务详情说明" width="150" />
                                <el-table-column align="center" prop="exclusion" label="除外内容" width="100" />
                                <el-table-column align="center" prop="unit" label="计价单位" width="80" />
                                <el-table-column align="center" prop="price" label="单价" width="80" />
                                <el-table-column align="center" prop="itemType" label="分类" width="100" />
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
        </el-container>

        <!-- 新增/编辑弹窗 -->
        <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px" :close-on-click-modal="false">
            <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
                <el-form-item label="服务名称" prop="itemName">
                    <el-input v-model="form.itemName" />
                </el-form-item>
                <el-form-item label="服务编码" prop="itemCode">
                    <el-input v-model="form.itemCode" />
                </el-form-item>
                <el-form-item label="国家统一编码" prop="nationalCode">
                    <el-input v-model="form.nationalCode" />
                </el-form-item>
                <el-form-item label="服务详情说明" prop="description">
                    <el-input v-model="form.description" type="textarea" />
                </el-form-item>
                <el-form-item label="除外内容" prop="exclusion">
                    <el-input v-model="form.exclusion" type="textarea" />
                </el-form-item>
                <el-form-item label="计价单位" prop="unit">
                    <el-input v-model="form.unit" />
                </el-form-item>
                <el-form-item label="单价" prop="price">
                    <el-input-number v-model="form.price" :min="0" />
                </el-form-item>
                <el-form-item label="分类" prop="itemType">
                    <el-input v-model="form.itemType" />
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input v-model="form.remark" type="textarea" />
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
import { ref, reactive, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import {
    getMedicalServicePage,
    addMedicalService,
    updateMedicalService,
    removeMedicalService,
} from "@/api/medicalServiceApi.js";

const queryParams = reactive({
    itemName: "",
});
const page = reactive({
    total: 0,
    pageSize: 6,
    currentPage: 1,
});
const condition = reactive({
    itemName: "",
    page: 1,
});
const serviceList = ref([]);
const dialogVisible = ref(false);
const dialogTitle = ref("新增服务");
const form = reactive({
    id: null,
    itemName: "",
    itemCode: "",
    nationalCode: "",
    description: "",
    exclusion: "",
    unit: "",
    price: 0,
    itemType: "",
    remark: "",
});
const formRef = ref(null);
const rules = {
    itemName: [{ required: true, message: "请输入服务名称", trigger: "blur" }],
    price: [{ required: true, message: "请输入单价", trigger: "blur" }],
};

const loadServiceList = async () => {
    condition.itemName = queryParams.itemName;
    condition.page = page.currentPage;
    try {
        const res = await getMedicalServicePage(condition);
        if (res.flag) {
            serviceList.value = res.data.records;
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
    loadServiceList();
};
const handleCurrentChange = (curPage) => {
    page.currentPage = curPage;
    loadServiceList();
};
const openAdd = () => {
    dialogTitle.value = "新增服务";
    Object.assign(form, {
        id: null,
        itemName: "",
        itemCode: "",
        nationalCode: "",
        description: "",
        exclusion: "",
        unit: "",
        price: 0,
        itemType: "",
        remark: "",
    });
    dialogVisible.value = true;
};
const openEdit = (row) => {
    dialogTitle.value = "编辑服务";
    Object.assign(form, row);
    dialogVisible.value = true;
};
const handleSubmit = () => {
    formRef.value.validate(async (valid) => {
        if (!valid) return;
        try {
            let res;
            if (form.id) {
                res = await updateMedicalService(form);
            } else {
                res = await addMedicalService(form);
            }
            if (res.flag) {
                ElMessage.success(res.message || "操作成功");
                dialogVisible.value = false;
                loadServiceList();
            } else {
                ElMessage.error(res.message || "操作失败");
            }
        } catch (err) {
            ElMessage.error("操作失败，请重试");
        }
    });
};
const handleDelete = (row) => {
    ElMessageBox.confirm("确定要删除该服务吗？", "删除确认", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
    })
        .then(async () => {
            try {
                const res = await removeMedicalService({ id: row.id });
                if (res.flag) {
                    ElMessage.success("删除成功");
                    loadServiceList();
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
onMounted(() => {
    loadServiceList();
});
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
</style>