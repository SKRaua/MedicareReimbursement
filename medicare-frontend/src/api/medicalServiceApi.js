import http from "@/request/request.js";

// 分页查询医疗服务
export function getMedicalServicePage(data) {
    return http.get("/medicalService/info", { params: data });
}
// 添加
export function addMedicalService(data) {
    return http.post("/medicalService/add", data);
}
// 修改
export function updateMedicalService(data) {
    return http.post("/medicalService/edit", data);
}
// 删除
export function removeMedicalService(data) {
    return http.post("/medicalService/remove", data);
}