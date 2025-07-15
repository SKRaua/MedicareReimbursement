import http from "@/request/request.js";

// 获取计算报销金额
export function calculateRb(data) {
    return http.get("/reimbursementRecord/calculateRb", { params: data });
}

// 确认报销
export function confirmRb(data) {
    return http.post("/reimbursementRecord/confirmRb", data);
}

