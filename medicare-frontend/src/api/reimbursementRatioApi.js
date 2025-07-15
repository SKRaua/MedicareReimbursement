import http from "@/request/request.js";

// 分页查询
export function getReimbursementRatioPage(data) {
    return http.get("/reimbursementRatio/info", { params: data });
}
export function addReimbursementRatio(data) {
    return http.post("/reimbursementRatio/add", data);
}
export function updateReimbursementRatio(data) {
    return http.post("/reimbursementRatio/edit", data);
}
export function removeReimbursementRatio(data) {
    return http.post("/reimbursementRatio/remove", data);
}