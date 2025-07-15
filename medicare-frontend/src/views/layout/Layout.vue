<template>
  <el-container style="height: 100%">
    <!-- 左边菜单 -->
    <Aside> </Aside>
    <!-- 右边内容 -->
    <el-container direction="vertical">
      <Header></Header>

      <!-- tab选项栏 -->
      <TabNav> </TabNav>

      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Aside from "@/components/Aside.vue";
import TabNav from "@/components/TabNav.vue";
import Header from "@/components/Header.vue";

export default {
  name: "Layout",
  components: {
    Aside,
    TabNav,
    Header,
  },
};
</script>

<style>
* {
  margin: 0;
  padding: 0;
}

body {
  font-size: 14px;
  color: #333333;
}

li {
  list-style: none;
}

a {
  text-decoration: none;
}

html,
body,
#layout,
.el-container,
#asideNav,
ul.el-menu {
  height: 100%;
}
</style>
