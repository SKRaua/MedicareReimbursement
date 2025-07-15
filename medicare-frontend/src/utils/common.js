// 获取当前日期
export function getNowDate() {
    let now = new Date();
    let year = now.getFullYear(); //得到年份
    let month = now.getMonth(); //得到月份
    let date = now.getDate(); //得到日期

    // 小于10的月份补0
    month = month < 10 ? "0" + month : month;
    // 小于10的日期补0
    date = date < 10 ? "0" + date : date;

    return year + "-" + month + "-" + date;
}

//向localStorage中存储一个json对象
export function setLocalStorage(key, value) {
    localStorage.setItem(key, JSON.stringify(value));
}

//向sessionStorage中存储一个json对象
export function setSessionStorage(key, value) {
    sessionStorage.setItem(key, JSON.stringify(value));
}

//从localStorage中获取一个json对象
export function getLocalStorage(key) {
    let str = JSON.parse(localStorage.getItem(key));
    if (str == "" || str == null || str == "null" || str == undefined) {
        return null;
    } else {
        return str;
    }
}

//从sessionStorage中获取一个json对象
export function getSessionStorage(key) {
    let str = JSON.parse(sessionStorage.getItem(key));
    if (str == "" || str == null || str == "null" || str == undefined) {
        return null;
    } else {
        return str;
    }
}

//在localStorage中删除一个json对象
export function removeLocalStorage(key) {
    localStorage.removeItem(key);
}

//在sessionStorage中删除一个json对象
export function removeSessionStorage(key) {
    sessionStorage.removeItem(key);
}

