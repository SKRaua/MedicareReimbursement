import { createStore } from 'vuex'
import { setSessionStorage, getSessionStorage } from '@/utils/common.js'
// , removeLocalStorage, removeSessionStorage, getNowDate, setLocalStorage, getLocalStorage 

export default createStore({
  state: {
    tabs: [

    ],

    // 获取菜单列表
    menus: getSessionStorage('menusList') || [],
  },
  getters: {
    // 获取所有的按钮
    tabs(state) {
      return state.tabs
    },

    // 获取所有的菜单
    menus(state) {
      return state.menus
    },
  },

  // 修改state状态
  mutations: {
    addMenus(state, param) {
      state.menus = param;
      setSessionStorage('menusList', param);
    },
    // 删除指定索引位置的第一个标签
    deleteTabByIndex(state, index) {
      state.tabs.splice(index, 1);
    },
    // 重置标签页
    clearTab(state, param) {
      state.tabs = param;
    },
    // 添加标签页
    addTab(state, payload) {
      let path = payload.path;
      if (path) {
        // 检查当前tabs中是否已经存在相同的标签页
        let result = state.tabs.filter((item) => {
          return item.path == path;
        });
        // 判断是否存在，如果不存在就需要加入到tab中
        if (result.length == 0) {
          if (state.tabs.length == 10) {
            // 删除第二个标签
            state.tabs.splice(1, 1);
          }
          // 再将新的标签页添加到tabs中
          state.tabs.push(payload);
        }
      }
    },
  },

  // 用于异步请求
  actions: {

  },
  modules: {}

})
