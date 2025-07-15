import http from "@/request/request.js";

// 分页查询疾病
export function getDiseasePage(data) {
    return http.get("/disease/info", { params: data });
}

// 新增疾病
export function addDisease(data) {
    return http.post("/disease/add", data);
}

// 修改疾病
export function updateDisease(data) {
    return http.post("/disease/update", data);
}

// 删除疾病
export function removeDisease(data) {
    return http.post("/disease/delete", data);
}