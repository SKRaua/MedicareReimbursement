<template>
    <div class="common-layout">
        <el-container>
            <el-header>
                <el-row :gutter="30">
                    <el-col :span="7">
                        <el-input placeholder="请输入药品名称查询" v-model="queryParams.drugName" @clear="query" clearable
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
                            添加药品
                        </el-button>
                    </el-col>
                </el-row>
            </el-header>
            <el-divider style="margin: 0"></el-divider>
            <el-main>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <div class="table-main">
                            <div class="table-main-header">药品信息</div>
                            <el-table :data="drugList" highlight-current-row size="small"
                                style="width: 100%; color: black" stripe>
                                <el-table-column align="center" fixed type="index" label="序号" width="60"
                                    :index="(i) => (page.currentPage - 1) * page.pageSize + i + 1" />
                                <el-table-column align="center" prop="drugName" label="通用名称" width="120" />
                                <el-table-column align="center" prop="tradeName" label="商品名称" width="120" />
                                <el-table-column align="center" prop="specification" label="规格" width="100" />
                                <el-table-column align="center" prop="unit" label="计价单位" width="80" />
                                <el-table-column align="center" prop="manufacturer" label="生产企业" width="120" />
                                <el-table-column align="center" prop="price" label="单价" width="80" />
                                <el-table-column align="center" prop="drugType" label="药品类型" width="80" />
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
                <el-form-item label="通用名称" prop="drugName">
                    <el-input v-model="form.drugName" />
                </el-form-item>
                <el-form-item label="商品名称" prop="tradeName">
                    <el-input v-model="form.tradeName" />
                </el-form-item>
                <el-form-item label="规格" prop="specification">
                    <el-input v-model="form.specification" />
                </el-form-item>
                <el-form-item label="计价单位" prop="unit">
                    <el-input v-model="form.unit" />
                </el-form-item>
                <el-form-item label="生产企业" prop="manufacturer">
                    <el-input v-model="form.manufacturer" />
                </el-form-item>
                <el-form-item label="单价" prop="price">
                    <el-input-number v-model="form.price" :min="0" />
                </el-form-item>
                <el-form-item label="药品类型" prop="drugType">
                    <el-select v-model="form.drugType" placeholder="请选择">
                        <el-option label="甲类" value="甲类" />
                        <el-option label="乙类" value="乙类" />
                        <el-option label="丙类" value="丙类" />
                    </el-select>
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
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import {
    getDrugPage,
    addDrug,
    updateDrug,
    removeDrug,
} from "@/api/drugApi.js";

// 查询参数
const queryParams = reactive({
    drugName: "",
});

// 分页和条件
const page = reactive({
    total: 0,
    pageSize: 6,
    currentPage: 1,
});
const condition = reactive({
    drugName: "",
    page: 1,
});

// 数据列表
const drugList = ref([]);

// 弹窗表单
const dialogVisible = ref(false);
const dialogTitle = ref("新增药品");
const form = reactive({
    id: null,
    drugName: "",
    tradeName: "",
    specification: "",
    unit: "",
    manufacturer: "",
    price: 0,
    drugType: "",
    remark: "",
});
const formRef = ref(null);
const rules = {
    drugName: [{ required: true, message: "请输入通用名称", trigger: "blur" }],
    tradeName: [{ required: true, message: "请输入商品名称", trigger: "blur" }],
    specification: [{ required: true, message: "请输入规格", trigger: "blur" }],
    unit: [{ required: true, message: "请输入计价单位", trigger: "blur" }],
    manufacturer: [{ required: true, message: "请输入生产企业", trigger: "blur" }],
    price: [{ required: true, message: "请输入单价", trigger: "blur" }],
    drugType: [{ required: true, message: "请选择药品类型", trigger: "change" }], // 修改为 drugType
};

// 加载数据
const loadDrugList = async () => {
    condition.drugName = queryParams.drugName;
    condition.page = page.currentPage;
    try {
        const res = await getDrugPage(condition);
        if (res.flag) {
            drugList.value = res.data.records;
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
    loadDrugList();
};

// 分页
const handleCurrentChange = (curPage) => {
    page.currentPage = curPage;
    loadDrugList();
};

// 打开新增弹窗
const openAdd = () => {
    dialogTitle.value = "新增药品";
    Object.assign(form, {
        id: null,
        drugName: "",
        tradeName: "",
        specification: "",
        unit: "",
        manufacturer: "",
        price: 0,
        drugType: "",
        remark: "",
    });
    dialogVisible.value = true;
};

// 打开编辑弹窗
const openEdit = (row) => {
    dialogTitle.value = "编辑药品";
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
                res = await updateDrug(form);
            } else {
                res = await addDrug(form);
            }
            if (res.flag) {
                ElMessage.success(res.message || "操作成功");
                dialogVisible.value = false;
                loadDrugList();
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
    ElMessageBox.confirm("确定要删除该药品吗？", "删除确认", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
    })
        .then(async () => {
            try {
                const res = await removeDrug({ id: row.id });
                if (res.flag) {
                    ElMessage.success("删除成功");
                    loadDrugList();
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

// 初始化
onMounted(() => {
    loadDrugList();
});
</script>

<style scoped>
/* 复用投保人维护页面样式即可 */
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