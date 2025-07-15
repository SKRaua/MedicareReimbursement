import axios from 'axios'
import qs from 'qs'
import router from '../router'
import { ElMessage } from 'element-plus'

const instance = axios.create({
    baseURL: "http://localhost:9999/",
});

export default instance;


// 拦截代码 携带token进行访问的代码
// Add a request interceptor
instance.interceptors.request.use(function (config) {

    // 如果 token 存在
    // 让每个请求携带自定义 token 请根据实际情况自行修改 ['token']为自定义key 请根据实际情况自行修改
    let token = sessionStorage.getItem("token")
    if (token) {
        config.headers['token'] = token;
    }
    // 定义需要post请求方式为application/json的api集
    let jsonQueryParamsPath = ['/customernurseitem/addItemToCustomer']
    // 设置post请求参数为
    if (config.method == 'post' && jsonQueryParamsPath.indexOf(config.url) > 0) {
        config.data = qs.stringify(config.data);
    }

    return config;
}, function (error) {
    // Do something with request error
    return Promise.reject(error);
});
// Add a response interceptor
instance.interceptors.response.use(function (response) {
    console.log(response)
    // 如果是token出现异常，返回登录页面
    if (!response.data.flag && (response.data.data == "token_error" || response.data.message.includes("token") || response.data.message.includes("权限"))) {
        ElMessage.error(response.data.message || "登录状态已过期，请重新登录")
        // 清除本地存储
        sessionStorage.removeItem("token")
        sessionStorage.removeItem("user")
        router.push("/login")
    }
    return response.data;
}, function (error) {
    return Promise.reject(error);
});