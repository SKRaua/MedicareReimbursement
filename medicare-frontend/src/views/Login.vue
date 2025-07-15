<template>
  <div class="login-bg">
    <div class="login">
      <div class="message">医保报销管理</div>
      <div id="darkbannerwrap"></div>
      <form>
        <input name="username" v-model="loginForm.username" type="text" placeholder="用户名" />
        <hr class="hr15" />
        <input name="password" v-model="loginForm.password" type="password" placeholder="密码" />
        <hr class="hr15" />

        <!-- 验证码输入框和图片 -->
        <div class="captcha-container">
          <input name="captcha" v-model="loginForm.captcha" type="text" placeholder="请输入验证码" class="captcha-input" />
          <img :src="captchaImage" @click="refreshCaptcha" class="captcha-image" alt="验证码" title="点击刷新验证码" />
        </div>
        <hr class="hr15" />

        <div class="role-select">
          <label>请选择身份:</label>
          <el-radio-group v-model="loginForm.roleId">
            <el-radio :label="0">管理员</el-radio>
            <el-radio :label="1">医院操作员</el-radio>
            <el-radio :label="2">报销管理员</el-radio>
          </el-radio-group>
        </div>
        <hr class="hr15" />
        <input type="button" value="登录" @click="login" style="width: 100%" />
        <hr class="hr20" />
      </form>
    </div>
  </div>
</template>

<script>
import { login } from "../api/userApi.js";
import { setSessionStorage } from "@/utils/common.js";
import axios from "@/request/request.js";

export default {
  data() {
    return {
      loginForm: {
        username: "",
        password: "",
        roleId: null,
        captcha: "",
        captchaId: "", // 验证码ID
      },
      captchaImage: "",
    };
  },
  mounted() {
    // 页面加载时获取验证码
    this.refreshCaptcha();
  },
  methods: {
    // 刷新验证码
    async refreshCaptcha() {
      try {
        const res = await axios.get("/captcha");
        if (res.flag) {
          this.captchaImage = res.data.image;
          this.loginForm.captchaId = res.data.captchaId; // 重要：存储验证码ID
        } else {
          this.$message.error("获取验证码失败");
        }
      } catch (error) {
        this.$message.error("获取验证码失败，请重试");
      }
    },

    login() {
      // 前端校验
      if (!this.loginForm.username) {
        this.$message.error("请输入用户名");
        return;
      }
      if (!this.loginForm.password) {
        this.$message.error("请输入密码");
        return;
      }
      if (!this.loginForm.captcha) {
        this.$message.error("请输入验证码");
        return;
      }
      if (this.loginForm.roleId === null) {
        this.$message.error("请选择登录身份");
        return;
      }

      // 调用登录接口
      login(this.loginForm).then((res) => {
        console.log(res);
        if (res.flag) {
          sessionStorage.setItem("token", res.message);
          setSessionStorage("user", res.data);
          this.$store.commit("addMenus", res.data.menuList);

          const firstPath = res.data.menuList[0].children[0].path;
          this.$router.push(firstPath);
          this.$message.success("登录成功");
        } else {
          this.$message.error(res.message);
          // 登录失败时刷新验证码
          this.refreshCaptcha();
          this.loginForm.captcha = "";
        }
      }).catch((error) => {
        this.$message.error("登录失败，请重试");
        // 登录失败时刷新验证码
        this.refreshCaptcha();
        this.loginForm.captcha = "";
      });
    },
  },
};
</script>

<style scoped>
.login-bg {
  width: 100%;
  height: 100%;
  background: url(../assets/logbg.png) no-repeat center;
  background-size: cover;
  overflow: hidden;
}

.login {
  margin: 200px auto 0 auto;
  min-height: 420px;
  max-width: 420px;
  padding: 40px;
  background-color: #fff;
  border-radius: 4px;
  box-sizing: border-box;
}

.login .message {
  margin: 10px 0 0 -58px;
  padding: 18px 10px 18px 60px;
  background: #ff81ab;
  position: relative;
  color: #fff;
  font-size: 20px;
  font-weight: bolder;
}

.login #darkbannerwrap {
  width: 18px;
  height: 10px;
  margin: 0 0 20px -58px;
  position: relative;
}

/* 验证码容器样式 */
.captcha-container {
  display: flex;
  align-items: center;
  gap: 10px;
}

.captcha-input {
  flex: 1;
  width: auto !important;
}

.captcha-image {
  width: 120px;
  height: 40px;
  cursor: pointer;
  border: 1px solid #ddd;
  border-radius: 3px;
  transition: border-color 0.3s;
}

.captcha-image:hover {
  border-color: #ec407a;
}

.login input[type="email"],
.login input[type="file"],
.login input[type="password"],
.login input[type="text"],
select {
  width: 100%;
  height: 30px;
  border: 1px solid #ddd;
  border-radius: 3px;
  padding: 0 10px;
  box-sizing: border-box;
}

.login input[type="email"]:focus,
.login input[type="file"]:focus,
.login input[type="password"]:focus,
.login input[type="text"]:focus,
select:focus {
  border: 1px solid #ec407a;
}

.login input[type="submit"],
.login input[type="button"] {
  display: inline-block;
  vertical-align: middle;
  padding: 12px, 24px;
  margin: 0;
  font-size: 18px;
  line-height: 24px;
  text-align: center;
  white-space: nowrap;
  cursor: pointer;
  color: #fff;
  background-color: #ff81ab;
  border-radius: 3px;
  border: none;
  appearance: none;
  -webkit-appearance: none;
  outline: 0;
  width: 100%;
}

.login hr {
  background: #fff;
}

.login hr .hr15 {
  height: 15px;
  border: none;
  margin: 0;
  padding: 0;
  width: 100%;
}

.login hr .hr20 {
  height: 20px;
  border: none;
  margin: 0;
  padding: 0;
  width: 100%;
}

.role-select {
  margin-top: 10px;
  color: #333;
}

.el-radio-group {
  margin-top: 8px;
  display: flex;
  gap: 15px;
  /* 减少间距 */
  flex-wrap: wrap;
  /* 允许换行 */
}

.el-radio {
  font-size: 14px;
  white-space: nowrap;
  /* 防止单个选项内换行 */
}

/* 针对小屏幕的响应式设计 */
@media (max-width: 480px) {
  .el-radio-group {
    gap: 10px;
    justify-content: space-between;
  }

  .el-radio {
    font-size: 13px;
    flex: 1;
    text-align: center;
  }
}
</style>
