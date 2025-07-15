import http from "@/request/request.js";

// 分页查询
export function selectInsurederPage(data) {
    return http.get("/insureder/info", { params: data });
}


export function addInsureder(data) {
    return http.post('/insureder/add', data)
}

//修改
export function updateInsureder(data) {
    return http.post('/insureder/update', data)
}

//删除
export function removeInsureder(data) {
    return http.post('/insureder/remove', data)
}


