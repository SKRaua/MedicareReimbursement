import http from "@/request/request.js";

// 药品分页查询
export function getDrugPage(data) {
    return http.get("/drug/info", { params: data });
}
// 新增
export function addDrug(data) {
    return http.post("/drug/add", data);
}
// 编辑
export function updateDrug(data) {
    return http.post("/drug/edit", data);
}
// 删除
export function removeDrug(data) {
    return http.post("/drug/remove", data);
}