import http from "@/request/request.js";

// 分页查询
export function getDrugReimbursementRatioPage(data) {
    return http.get("/drugReimbursementRatio/info", { params: data });
}
export function getEnabledDrugRbRatios() {
    return http.get("/drugReimbursementRatio/enabledList");
}
export function addDrugReimbursementRatio(data) {
    return http.post("/drugReimbursementRatio/add", data);
}
export function updateDrugReimbursementRatio(data) {
    return http.post("/drugReimbursementRatio/edit", data);
}
export function removeDrugReimbursementRatio(data) {
    return http.post("/drugReimbursementRatio/remove", data);
}