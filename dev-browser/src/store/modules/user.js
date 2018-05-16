import { login, logout, getInfo } from '@/api/login'
import { getLoginState, setLoginState, removeLoginState } from '@/utils/auth'
import store from "../index";
import { Message } from 'element-ui'

function formatMenus(menus) {
  menus.forEach((menu, index) => {
    delete menu.parent
    menu.meta = {title: menu.title, icon: menu.icon}
    delete menu.icon
    delete menu.createdAt
    delete menu.deletedAt
    if (menu.children) {
      formatMenus(menu.children)
    }
  })
  return menus
}

function sortMenus(menus) {
  menus = menus.sort((a, b) => {
    return a.sortNumber - b.sortNumber
  })


  //封装为处理多级的菜单的方法
  menus.forEach((menu, index) => {
    if(menu.children){
      menu.children = menu.children.sort((a, b) => {
        return a.sortNumber - b.sortNumber
      })
    }
  })
  return menus
}

//得到 list的 menus
function getListMenus(menus) {
  let menusList = [];

  menus.forEach( (value, index) => {

    if( value.children != '{}') {
      value.children.forEach( (childVal,i) => {
        menusList.push(childVal)
      })
    }

  })
  return menusList
}

const user = {
  state: {
    loginState: getLoginState(),
    name: '',
    avatar: '',
    roles: [],
    departments: [],
    menus :[],
    listMenus :[]
  },

  mutations: {
    SET_LOGIN_STATE: (state, loginState) => {
      state.loginState = loginState
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_DEPARTMENTS: (state, departments) => {
      state.departments = departments
    },
    SET_MENUS :(state, menus) => {
      state.menus = menus
    },
    SET_LISTMENUS :(state, menus) => {
      state.listMenus = menus
    }
  },

  actions: {
    // 获取用户信息
    GetInfo({ commit }) {
      return new Promise((resolve, reject) => {
        getInfo().then(response => {
          const data = response.data

          //判断是否是后台用户登陆
          if(data.backend != true) {
            store.dispatch('FedLogOut').then(() => {
              Message.error('验证失败，非后台用户!')
            })
            return false;
          }

          commit('SET_ROLES', data.roles)
          commit('SET_DEPARTMENTS', data.departments)
          commit('SET_NAME', data.name)
          commit('SET_AVATAR', data.avatar)
          commit('SET_MENUS',sortMenus(formatMenus(data.menus)))
          commit('SET_LISTMENUS',getListMenus(data.menus))
          resolve(response)

        }).catch(error => {
          reject(error)
        })
      })
    },

    // 登出
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.loginState).then(() => {
          commit('SET_LOGIN_STATE', '')
          commit('SET_ROLES', '')
          commit('SET_DEPARTMENTS', '')
          removeLoginState()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_LOGIN_STATE', '')
        removeLoginState()
        resolve()
      })
    }
  }
}

export default user
