import http from "@/request/request.js";

// 分页查询药品订单
export function getDrugOrderPage(data) {
    return http.get("/drugOrder/info", { params: data });
}

// 添加开药记录
export function addDrugOrder(data) {
    return http.post("/drugOrder/add", data);
}

// 修改开药记录
export function updateDrugOrder(data) {
    return http.post("/drugOrder/edit", data);
}

// 删除开药记录
export function removeDrugOrder(data) {
    return http.post("/drugOrder/remove", data);
}