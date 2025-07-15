<template>
  <el-aside id="asideNav" width="200px">
    <div class="logo-name">
      <p>医保报销管理</p>
      <img src="../assets/logo.png" class="logo-png"
        style="width: 100px;; height: 100px; margin: 10px auto 0 auto; display: block;" />
    </div>
    <el-menu active-text-color=" #ffd04b" background-color="#ff71a0" :default-active="$route.path" :router="true"
      unique-opened text-color="#fff">
      <template v-for="menu in $store.getters.menus">
        <el-sub-menu :index="menu.menusIndex">
          <template #title>
            <el-icon>
              <!-- 动态获取图标 -->
              <component :is="menu.icon" />
            </el-icon>
            <span>{{ menu.title }}</span>
          </template>
          <template v-for="subMenu in menu.children">
            <el-menu-item :index="subMenu.path">
              <el-icon>
                <component :is="subMenu.icon" />
              </el-icon>
              <span>{{ subMenu.title }}</span>
            </el-menu-item>
          </template>
        </el-sub-menu>
      </template>
    </el-menu>
  </el-aside>
</template>
<script></script>

<style scoped>
#asideNav {
  display: flex;
  flex-direction: column;
}

#asideNav .logo-name {
  width: 100%;
  height: 150px;
  background-color: #ff71a0;
}

#asideNav .logo-name .logo-png {
  width: 165px;
  height: 165px;
  margin: -30px 0 0 10px;
}

#asideNav .logo-name p {
  font-weight: 800;
  height: 50px;
  line-height: 50px;
  text-align: center;
  font-size: 25px;
  margin: 0px;
  color: #fff;
}

#asideNav .el-menu {
  /* font-weight: 550; */
  flex: 1;
  border-right: none;
}
</style>
