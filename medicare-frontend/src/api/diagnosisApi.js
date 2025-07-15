import http from "@/request/request.js";

// 分页查询诊断
export function getDiagnosisPage(data) {
    return http.get("/diagnosis/info", { params: data });
}

// 添加诊断
export function addDiagnosis(data) {
    return http.post("/diagnosis/add", data);
}

// 修改诊断
export function updateDiagnosis(data) {
    return http.post("/diagnosis/edit", data);
}

export function removeDiagnosis(data) {
    return http.post("/diagnosis/remove", data);
}