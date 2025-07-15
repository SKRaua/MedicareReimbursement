import http from "@/request/request.js";

// 分页查询诊疗项目订单
export function getTreatmentItemOrderPage(data) {
    return http.get("/treatmentItemOrder/info", { params: data });
}
export function addTreatmentItemOrder(data) {
    return http.post("/treatmentItemOrder/add", data);
}
export function updateTreatmentItemOrder(data) {
    return http.post("/treatmentItemOrder/edit", data);
}
export function removeTreatmentItemOrder(data) {
    return http.post("/treatmentItemOrder/remove", data);
}