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
                        <el-col :span="2">
                            <el-button type="primary" @click="openAdd" size="large">
                                <el-icon>
                                    <Plus />
                                </el-icon>
                                添加客户
                            </el-button>
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
                <el-form-item label="姓名" prop="name">
                    <el-input v-model="form.name" />
                </el-form-item>
                <el-form-item label="身份证号" prop="idCard">
                    <el-input v-model="form.idCard" />
                </el-form-item>
                <el-form-item label="性别" prop="gender">
                    <el-select v-model="form.gender" placeholder="请选择">
                        <el-option label="女" :value="0" />
                        <el-option label="男" :value="1" />
                        <el-option label="未知" :value="2" />
                    </el-select>
                </el-form-item>
                <el-form-item label="出生日期" prop="birthDate">
                    <el-date-picker v-model="form.birthDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期"
                        style="width: 100%" />
                </el-form-item>
                <el-form-item label="住院号" prop="inpatientNo">
                    <el-input v-model="form.inpatientNo" placeholder="请输入住院号" />
                </el-form-item>
                <el-form-item label="结算类型" prop="settlementType">
                    <el-select v-model="form.settlementType" placeholder="请选择结算类型" clearable style="width: 100%">
                        <el-option label="医保" value="医保" />
                        <el-option label="自费" value="自费" />
                    </el-select>
                </el-form-item>
                <el-form-item label="入院时间" prop="admissionTime">
                    <el-date-picker v-model="form.admissionTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss"
                        placeholder="选择入院时间" style="width: 100%" />
                </el-form-item>
                <el-form-item label="出院时间" prop="dischargeTime">
                    <el-date-picker v-model="form.dischargeTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss"
                        placeholder="选择出院时间（未出院可留空）" style="width: 100%" />
                </el-form-item>
                <el-form-item label="紧急联系人" prop="emergencyContact">
                    <el-input v-model="form.emergencyContact" placeholder="请输入紧急联系人姓名" />
                </el-form-item>
                <el-form-item label="家庭地址" prop="address">
                    <el-input v-model="form.address" />
                </el-form-item>
                <el-form-item label="工作状态" prop="workStatus">
                    <el-select v-model="form.workStatus" placeholder="请选择工作状态" clearable :disabled="!dialogVisible"
                        style="width:100%">
                        <el-option label="在职" value="在职" />
                        <el-option label="退休" value="退休" />
                        <el-option label="无业" value="无业" />
                        <el-option label="学生" value="学生" />
                    </el-select>
                </el-form-item>
                <el-form-item label="联系电话" prop="contactPhone">
                    <el-input v-model="form.contactPhone" />
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
    selectInsurederPage,
    addInsureder,
    updateInsureder,
    removeInsureder,
} from "@/api/insurederApi.js";

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
    name: "",//客户名
    page: 1,
});

// 数据列表
const customerInfoList = ref([]);

// 弹窗表单（新增InsurederVo的属性）
const dialogVisible = ref(false);
const dialogTitle = ref("新增客户");
const form = reactive({
    id: null,
    name: "",
    idCard: "",
    gender: 2,
    birthDate: "",
    age: null, // 年龄通常由出生日期计算，前端可留空或后端返回
    inpatientNo: "", // 新增：住院号
    settlementType: "", // 新增：结算类型
    admissionTime: "", // 新增：入院时间
    dischargeTime: "", // 新增：出院时间
    emergencyContact: "", // 新增：紧急联系人
    address: "",
    workStatus: "",
    contactPhone: "",
});
const formRef = ref(null);
const rules = {
    name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
    idCard: [{ required: true, message: "请输入身份证号", trigger: "blur" }],
    gender: [{ required: true, message: "请选择性别", trigger: "change" }],
    birthDate: [{ required: true, message: "请选择出生日期", trigger: "change" }],
    inpatientNo: [{ required: false, message: "请输入住院号", trigger: "blur" }],
    workStatus: [
        { required: true, message: "请选择员工状态", trigger: "change" },
        {
            validator: (rule, value, callback) => {
                const validValues = ['在职', '退休', '无业', '学生'];
                if (!validValues.includes(value)) {
                    callback(new Error('请选择正确的工作状态'));
                }
                else {
                    callback();
                }
            },
            trigger: 'change'
        }
    ],
    settlementType: [
        { required: true, message: '请选择结算类型', trigger: 'change' },
        {
            validator: (rule, value, callback) => {
                const validStatus = ['医保', '自费'];
                if (!validStatus.includes(value)) {
                    callback(new Error('请选择正确的结算类型'));
                }
                else {
                    callback();
                }
            },
            trigger: 'change'
        }
    ],
};

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

// 打开新增弹窗（初始化新增属性）
const openAdd = () => {
    dialogTitle.value = "新增客户";
    Object.assign(form, {
        id: null,
        name: "",
        idCard: "",
        gender: 2,
        birthDate: "",
        inpatientNo: "",
        settlementType: "自费",
        admissionTime: "",
        dischargeTime: "",
        emergencyContact: "",
        address: "",
        workStatus: "无业",
        contactPhone: "",
    });
    if ('id' in form) delete form.id;
    dialogVisible.value = true;
};

// 打开编辑弹窗（同步新增的属性）
const openEdit = (row) => {
    dialogTitle.value = "编辑客户";

    const validWorkStatus = ['在职', '退休', '无业', '学生'].includes(row.workStatus)
        ? row.workStatus
        : '无业';

    const validSettleType = ['自费', '医保'].includes(row.settlementType)
        ? row.settlementType
        : '自费';

    Object.assign(form, {
        ...row,
        workStatus: validWorkStatus,
        settlementType: validSettleType,
    });

    dialogVisible.value = true;
};


// 提交表单（自动包含新增属性）
const handleSubmit = () => {
    formRef.value.validate(async (valid) => {
        if (!valid) return;
        try {
            let res;
            if (form.id) {
                // 编辑：提交完整表单（包含新增属性）
                res = await updateInsureder(form);
            } else {
                // 新增：提交表单（包含新增属性）
                res = await addInsureder(form);
            }
            if (res.flag) {
                ElMessage.success(res.message || "操作成功");
                dialogVisible.value = false;
                loadCustomerList();
            } else {
                ElMessage.error(res.message || "操作失败");
            }
        } catch (err) {
            ElMessage.error("操作失败，请重试");
        }
    });
};

// 删除（逻辑不变，保持原有）
const handleDelete = (row) => {
    ElMessageBox.confirm("确定要删除该客户吗？", "删除确认", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
    })
        .then(async () => {
            try {
                const res = await removeInsureder({ id: row.id }); // 传入id而非整个对象，更规范
                if (res.flag) {
                    ElMessage.success("删除成功");
                    loadCustomerList();
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
    loadCustomerList();
});
</script>

<style scoped>
/* 样式保持不变，与原有风格一致 */
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