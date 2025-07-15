<template>
  <div class="user-container">
    <!-- 页面标题 -->
    <div class="page-title">医疗操作人员管理</div>

    <!-- 搜索和新增按钮区域 -->
    <div class="search-bar">
      <el-input 
        v-model="searchParams.username" 
        placeholder="请输入账号搜索" 
        clearable 
        style="width: 240px; margin-right: 10px;"
        @keyup.enter="handleSearch"
        @clear="handleSearch"
      />
      <el-input 
        v-model="searchParams.nickname" 
        placeholder="请输入真实姓名搜索" 
        clearable 
        style="width: 240px; margin-right: 10px;"
        @keyup.enter="handleSearch"
        @clear="handleSearch"
      />
      <el-button type="primary" @click="handleSearch">搜索</el-button>
      <el-button type="success" @click="openAddDialog">新增医疗操作员</el-button>
    </div>

    <!-- 医疗操作人员表格 -->
    <el-table 
      :data="userList" 
      border 
      stripe 
      style="width: 100%; margin-top: 15px;"
      highlight-current-row
    >
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="username" label="系统账号" align="center" />
      <el-table-column prop="nickname" label="真实姓名" align="center" />
      <el-table-column 
        prop="roleId" 
        label="角色" 
        align="center"
        :formatter="formatRole"
      />
      <el-table-column prop="password" label="密码" align="center" />
      <el-table-column label="操作" width="200" align="center">
        <template #default="scope">
          <el-button 
            type="primary" 
            size="small" 
            @click="openEditDialog(scope.row)"
            style="margin-right: 5px;"
          >
            编辑
          </el-button>
          <el-button 
            type="danger" 
            size="small" 
            @click="handleDelete(scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页控件 -->
    <div class="pagination" style="margin-top: 15px; text-align: right;">
      <el-pagination
        v-model:current-page="page.currentPage"
        :page-size="page.pageSize"
        :total="page.total"
        layout="prev, pager, next, total"
        @current-change="handlePageChange"
      />
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog 
      :title="isEdit ? '编辑医疗操作员' : '新增医疗操作员'" 
      v-model="dialogVisible"
      width="500px"
      @close="handleDialogClose"
    >
      <el-form 
        :model="userForm" 
        label-width="100px" 
        :rules="formRules"
        ref="userFormRef"
      >
        <el-form-item label="系统账号" prop="username">
          <el-input 
            v-model="userForm.username" 
            placeholder="请输入系统账号"
          />
        </el-form-item>
        <el-form-item label="真实姓名" prop="nickname">
          <el-input 
            v-model="userForm.nickname" 
            placeholder="请输入真实姓名"
          />
        </el-form-item>
        <el-form-item 
          label="密码" 
          prop="password"
          :rules="isEdit ? editPasswordRules : addPasswordRules"
        >
          <el-input 
            v-model="userForm.password" 
            :type='text'
            placeholder="请输入密码"
          />
        </el-form-item>
        <el-form-item label="角色" prop="roleId" v-if="!isEdit">
          <el-select 
            v-model="userForm.roleId" 
            placeholder="请选择角色"
            disabled 
          >
            <el-option 
              label="医疗操作员" 
              value="1"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button 
          type="primary" 
          @click="handleSubmit"
        >
          {{ isEdit ? '确认修改' : '确认新增' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, getCurrentInstance } from 'vue';
import { 
  listUserPage, 
  addUser, 
  updateUser, 
  removeUser 
} from '@/api/userApi.js';
import { ElMessage, ElMessageBox } from 'element-plus';

// 获取组件实例（用于表单验证）
const { proxy } = getCurrentInstance();

// 表格数据
const userList = ref([]);

// 搜索参数
const searchParams = reactive({
  username: '',
  nickname: '',
  roleId: 1  // 固定查询医疗操作员（role_id=1）
});

// 分页参数
const page = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
});

// 弹窗状态
const dialogVisible = ref(false);
const isEdit = ref(false);  // true:编辑，false:新增

// 表单数据
const userForm = reactive({
  id: null,
  username: '',
  nickname: '',
  password: '',
  roleId: 1  // 强制为医疗操作员
});

// 表单验证规则
const formRules = reactive({
  username: [
    { required: true, message: '请输入系统账号', trigger: 'blur' },
    { min: 3, max: 20, message: '账号长度为3-20个字符', trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度为2-20个字符', trigger: 'blur' }
  ]
});

// 密码验证规则（新增和编辑不同）
const addPasswordRules = [
  { required: true, message: '请输入密码', trigger: 'blur' },
  { min: 6, max: 20, message: '密码长度为6-20个字符', trigger: 'blur' }
];
const editPasswordRules = [
  { required: true, message: '请输入新密码', trigger: 'blur' },
  { min: 6, max: 20, message: '密码长度为6-20个字符', trigger: 'blur' }
];

// 角色格式化（将1转换为"医疗操作员"）
const formatRole = (row) => {
  return row.roleId === 1 ? '医疗操作员' : '未知角色';
};

// 加载医疗操作员列表
const loadUserList = async () => {
  try {
    const params = {
      ...searchParams,
      page: page.currentPage,
      pageSize: page.pageSize
    };
    const res = await listUserPage(params);
    if (res.flag) {
      userList.value = res.data.records;
      console.log("表格数据:",userList.value);
      page.total = res.data.total;
      page.pageSize = res.data.size;
      page.currentPage = res.data.current;
    } else {
      ElMessage.error(res.message || '加载数据失败');
    }
  } catch (err) {
    ElMessage.error('网络错误，请重试');
  }
};

// 搜索
const handleSearch = () => {
  page.currentPage = 1;  // 重置页码
  loadUserList();
};

// 分页切换
const handlePageChange = (currentPage) => {
  page.currentPage = currentPage;
  loadUserList();
};

// 打开新增弹窗
const openAddDialog = () => {
  isEdit.value = false;
  loadUserList().then(()=>{
    // 重置表单
    userForm.id = null;
    userForm.username = '';
    userForm.nickname = '';
    userForm.password = '';
    userForm.roleId = 1;  // 强制为医疗操作员

    dialogVisible.value = true;
    setTimeout(() => {
        if (proxy.$refs.userFormRef) {
        proxy.$refs.userFormRef.resetFields();
        }
    }, 100);
  })
  
  
};

// 打开编辑弹窗
const openEditDialog = (row) => {
  isEdit.value = true;
  //强制清空数据
  userForm.id = null;
  userForm.username = '';
  userForm.nickname = ''; 
  userForm.password = '';
  // 重新填充表单数据
  userForm.id = row.id;
  userForm.username = row.username;
  userForm.nickname = row.nickname;
  userForm.password = row.password;  // 密码为空，编辑时需重新输入新密码
  userForm.roleId = 1;  // 强制为医疗操作员
  dialogVisible.value = true;
  setTimeout(()=>{
     // 重置表单验证状态
    proxy.$refs.userFormRef.resetFields();
  })
};

// 表单提交（新增/编辑）
const handleSubmit = async () => {
  // 表单验证
  const valid = await proxy.$refs.userFormRef.validate();
  if (!valid) return;

  try {
    if (isEdit.value) {
      // 编辑操作
      const res = await updateUser({
        ...userForm,
        password: userForm.password,  // 原密码（此处根据后端实际参数调整，若无需原密码可删除）
        newPassword: userForm.password  // 新密码
      });
      if (res.flag) {
        ElMessage.success('修改成功');
        dialogVisible.value = false;
        loadUserList();
      } else {
        ElMessage.error(res.message || '修改失败');
      }
    } else {
      // 新增操作
      const res = await addUser({
        ...userForm,
        password: userForm.password  // 传递密码（匹配后端add方法的参数）
      });
      if (res.flag) {
        ElMessage.success('新增成功');
        dialogVisible.value = false;
        loadUserList();
      } else {
        ElMessage.error(res.message || '新增失败');
      }
    }
  } catch (err) {
    ElMessage.error('网络错误，请重试');
  }
};

const handleDialogClose = () => { 
    userForm.id=null;
    userForm.username='';
    userForm.nickname='';
    userForm.password='';
    // 重置表单验证状态
    if (proxy.$refs.userFormRef) {
        proxy.$refs.userFormRef.resetFields();
    }
};

// 删除操作
const handleDelete = async (row) => {
  ElMessageBox.confirm(
    `确定要删除账号"${row.username}"吗？`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const res = await removeUser({id: row.id});
      if (res.flag) {
        ElMessage.success('删除成功');
        loadUserList();
      } else {
        ElMessage.error(res.message || '删除失败');
      }
    } catch (err) {
      ElMessage.error('网络错误，请重试');
    }
  }).catch(() => {
    // 取消删除
    ElMessage.info('已取消删除');
  });
};

// 页面加载时获取数据
onMounted(() => {
  loadUserList();
});

</script>

<style scoped>
.user-container {
  padding: 20px;
  background-color: #fff;
  min-height: calc(100vh - 60px);
}

.page-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.search-bar {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.pagination {
  text-align: right;
}
</style>