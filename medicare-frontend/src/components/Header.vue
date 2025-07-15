<template>
  <el-header id="header">
    <div>
      <img style="width: 160px; height: 40px; margin: 20px 0 -12px" src="@/assets/welcome.png" />
    </div>
    <el-dropdown split-button type="default">
      <!-- 展示用户昵称，结合实际数据渲染 -->
      {{ user.nickname }}
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item @click="logout">注销</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </el-header>
</template>

<script>
// 引入工具函数，用于操作 sessionStorage
import { getSessionStorage, setSessionStorage } from "@/utils/common.js";

export default {
  name: "Header",
  data() {
    return {
      // 从 sessionStorage 获取用户信息，需确保 getSessionStorage 函数能正确返回对应数据
      user: getSessionStorage("user"),
    };
  },
  methods: {
    logout() {
      // 清除 token 相关会话存储
      sessionStorage.setItem("token", null);
      // 清除用户信息相关会话存储
      setSessionStorage("user", null);
      // 提交 Vuex mutation，清空菜单（需确保 Vuex 中有 addMenus 这个 mutation 定义）
      this.$store.commit("addMenus", []);
      // 提交 Vuex mutation，清空标签（需确保 Vuex 中有 clearTab 这个 mutation 定义）
      this.$store.commit("clearTab", []);
      // 跳转到首页，需确保项目路由配置中有对应的根路径路由
      this.$router.push("/");
    },
  },
};
</script>

<style scoped>
#header {
  max-height: 50px;
  line-height: 50px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

#header>h2 {
  color: #0b67b8;
}
</style>
