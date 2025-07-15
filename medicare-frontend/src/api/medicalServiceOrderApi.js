import http from "@/request/request.js";

// 分页查询医疗服务订单
export function getMedicalServiceOrderPage(data) {
    return http.get("/medicalServiceOrder/info", { params: data });
}

// 添加医疗服务订单
export function addMedicalServiceOrder(data) {
    return http.post("/medicalServiceOrder/add", data);
}

// 修改医疗服务订单
export function updateMedicalServiceOrder(data) {
    return http.post("/medicalServiceOrder/edit", data);
}

// 删除医疗服务订单
export function removeMedicalServiceOrder(data) {
    return http.post("/medicalServiceOrder/remove", data);
}