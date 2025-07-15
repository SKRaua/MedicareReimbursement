//导入封装号的axios @等价于/src
import http from '@/request/request.js'

// 查询用户
export function listUserPage(data) {
    return http.get('/user/listPage', { params: data })
}


//用户登录
export function login(data) {
    return http.get('/user/login', { params: data })
}

//添加系统用户信息
export function addUser(data) {
    return http.post('/user/add', data)
}

//修改系统用户信息
export function updateUser(data) {
    return http.post('/user/update', data)
}

//删除系统用户信息
export function removeUser(data) {
   return http.post('/user/remove', data)
}


