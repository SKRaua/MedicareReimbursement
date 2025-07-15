import http from "@/request/request.js";

// 分页查询诊疗项目
export function getTreatmentItemPage(data) {
    return http.get("/treatmentItem/info", { params: data });
}
export function addTreatmentItem(data) {
    return http.post("/treatmentItem/add", data);
}
export function updateTreatmentItem(data) {
    return http.post("/treatmentItem/edit", data);
}
export function removeTreatmentItem(data) {
    return http.post("/treatmentItem/remove", data);
}