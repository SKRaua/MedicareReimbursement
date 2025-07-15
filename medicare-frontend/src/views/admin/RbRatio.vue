<template>
    <div class="common-layout">
        <el-container>
            <el-header>
                <el-row :gutter="30">
                    <el-col :span="7">
                        <el-select 
                            v-model="queryParams.hospitalLevel" 
                            placeholder="请选择医院级别"
                            clearable
                            @change="query"
                         >
                            <el-option label="三级" value="三级" />
                            <el-option label="二级" value="二级" />
                            <el-option label="一级" value="一级" />
                            <el-option label="社区" value="社区" />
                        </el-select>
                    </el-col>
                    <el-col :span="7">
                        <el-select 
                            v-model="queryParams.staffType" 
                            placeholder="请选择人员类型"
                            clearable
                            @change="query"
                        >
                            <el-option label="在职" value="在职" />
                            <el-option label="退休" value="退休" />
                            <el-option label="无业" value="无业" />
                            <el-option label="学生" value="学生" />
                        </el-select>
                    </el-col>
                    <el-col :span="2">
                    <el-button type="primary" @click="query" size="large">
                        <el-icon><Search /></el-icon>
                        查询
                    </el-button>
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
                    <el-col :span="24">
                        <div class="table-main">
                            <div class="table-main-header">总报销比例</div>
                            <el-table :data="ratioList" highlight-current-row size="small"
                                style="width: 100%; color: black" stripe>
                                <el-table-column align="center" fixed type="index" label="序号" width="60"
                                    :index="(i) => (page.currentPage - 1) * page.pageSize + i + 1" />
                                <el-table-column align="center" prop="hospitalLevel" label="医院级别" width="120" />
                                <el-table-column align="center" prop="staffType" label="人员类型" width="100" />
                                <el-table-column align="center" prop="startAmount" label="起付线" width="100" />
                                <el-table-column align="center" prop="endAmount" label="封顶线" width="100" />
                                <el-table-column align="center" prop="ratio" label="报销比例(%)" width="100" />
                                <el-table-column align="center" prop="status" label="状态" width="80">
                                    <template #default="scope">
                                        <el-tag :type="scope.row.status === '启用' ? 'success' : 'info'">
                                            {{ scope.row.status }}
                                        </el-tag>
                                    </template>
                                </el-table-column>
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
            <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
                <el-form-item label="医院级别" prop="hospitalLevel">
                    <el-select v-model="form.hospitalLevel" placeholder="请选择">
                        <el-option label="三级" value="三级" />
                        <el-option label="二级" value="二级" />
                        <el-option label="一级" value="一级" />
                        <el-option label="社区" value="社区" />
                    </el-select>
                </el-form-item>
                <el-form-item label="人员类型" prop="staffType">
                    <el-select v-model="form.staffType" placeholder="请选择">
                        <el-option label="在职" value="在职" />
                        <el-option label="退休" value="退休" />
                        <el-option label="无业" value="无业" />
                        <el-option label="学生" value="学生" />
                    </el-select>
                </el-form-item>
                <el-form-item label="起付线" prop="startAmount">
                    <el-input-number v-model="form.startAmount" :min="0" :precision="2" />
                </el-form-item>
                <el-form-item label="封顶线" prop="endAmount">
                    <el-input-number v-model="form.endAmount" :min="0" :precision="2" />
                </el-form-item>
                <el-form-item label="报销比例(%)" prop="ratio">
                    <el-input-number v-model="form.ratio" :min="0" :max="100" />
                </el-form-item>
                <el-form-item label="状态" prop="status">
                    <el-select v-model="form.status" placeholder="请选择">
                        <el-option label="启用" value="启用" />
                        <el-option label="停用" value="停用" />
                    </el-select>
                </el-form-item>
                <el-form-item label="生效日期" prop="effectiveDate">
                    <el-date-picker 
                        v-model="form.effectiveDate" 
                        type="date" 
                        placeholder="请选择生效日期" 
                        value-format="YYYY-MM-DD"
                    />
                </el-form-item>"
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
import { ref, reactive, onMounted,watch } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import {
    getReimbursementRatioPage,
    addReimbursementRatio,
    updateReimbursementRatio,
    removeReimbursementRatio,
} from "@/api/reimbursementRatioApi.js";

const queryParams = reactive({
    hospitalLevel: "",
    staffType: "",
});
const page = reactive({
    total: 0,
    pageSize: 6,
    currentPage: 1,
});
const ratioList = ref([]);
const dialogVisible = ref(false);
const dialogTitle = ref("新增比例");
const form = reactive({
    id: null,
    hospitalLevel: "",
    staffType: "",
    startAmount: null,
    endAmount: null,
    ratio: 0,
    status: "启用",
    remark: "",
});
const formRef = ref(null);
const rules = {
    hospitalLevel: [{ required: true, message: "请选择医院级别", trigger: "change" }],
    staffType: [{ required: true, message: "请选择人员类型", trigger: "change" }],
    startAmount: [{ required: true, message: "请输入起付线", trigger: "blur" },
        {type: "number",min:0, message: "请输入数字", trigger: "blur"}
    ],
    endAmount: [{ required: true, message: "请输入封顶线", trigger: "blur" },
        {type: "number",min:0, message: "请输入数字", trigger: "blur"},
        {
            validator:(rule,value,callback)=>{
                //若起付线已经填写，且封顶线小于起付线,则报错
                if(form.startAmount!==null && value!==null && value<form.startAmount){
                    callback(new Error("封顶线不能小于起付线"));
                }
                else{
                    callback(); //校验通过
                }
            },
            trigger: "blur"
        }
    ],
    ratio: [{ required: true, message: "请输入报销比例", trigger: "blur" }],
    status: [{ required: true, message: "请选择状态", trigger: "change" }],
    effectiveDate: [{ required: true, message: "请选择生效日期", trigger: "change" }],
};

watch(
    ()=>form.startAmount,
    (newValue)=>{
        if(formRef.value){
            formRef.value.validateField("endAmount");
        }
    }
);

watch(
    ()=>form.endAmount,
    (newValue)=>{
        if(formRef.value){
            formRef.value.validateField("startAmount");
        }
    }
);

const loadRatioList = async () => {
    //构建与后端DTO相应的查询条件
    const ratioDTO={
        hospitalLevel: queryParams.hospitalLevel,
        staffType: queryParams.staffType,
        page: page.currentPage,
    };
    try{
        //调用接口时传递ratioDTO
        const res=await getReimbursementRatioPage(ratioDTO);
        if(res.flag){
            ratioList.value=res.data.records;
            page.total=res.data.total;
            page.pageSize=res.data.size;
            page.currentPage=res.data.current;
            console.log("比例数据表:",ratioList.value);
        }
        else{
            ElMessage.error(res.message||"查询失败");
        }
    }catch(err){
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
        hospitalLevel: "",
        staffType: "",
        startAmount: null,
        endAmount: null,
        ratio: 0,
        status: "启用",
        remark: "",
        effectiveDate: new Date().toISOString().split('T')[0],//默认今天
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
                res = await updateReimbursementRatio(form);
            } else {
                res = await addReimbursementRatio(form);
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
                const res = await removeReimbursementRatio({ id: row.id });
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

onMounted(() => {
    loadRatioList();
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