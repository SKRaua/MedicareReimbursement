import http from "@/request/request.js";

// 分页查询
export function selectPatientPage(data) {
    return http.get("/customer/info", { params: data });
}


export function addCustomer(data) {
    return http.post('/customer/add', data)
}

//修改
export function updateCustomer(data) {
    return http.post('/customer/update', data)
}

//删除
export function removeCustomer(data) {
    return http.post('/customer/remove', data)
}


