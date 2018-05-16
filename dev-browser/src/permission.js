import router from './router'
import store from './store'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css'// Progress 进度条样式
import { Message } from 'element-ui'
import { getLoginState } from "./utils/auth"

const whiteList = ['/login','/logout'] // 不重定向白名单

router.beforeEach((to, from, next) => {

  NProgress.start()

  if(getLoginState() === 'true'){

    // var menusList = store.getters.listMenus;
    //
    // var path =  to.path.split('').reverse();
    // var a = '/';
    // var index = path.indexOf(a);
    // var newPath = path.splice(0,index).reverse().join('');
    //
    // menusList.forEach((menu, index) => {
    //   if( newPath == menu.path) {
    //     store.dispatch('getButtons',menu.buttons)
    //   }
    // })







    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else {
      if (!store.getters.roles.length > 0) {
        store.dispatch('GetInfo').then(res => { // 拉取用户信息
          window.myinfo = res.data
          next()
        }).catch((error) => {
          console.log(error)
          store.dispatch('FedLogOut').then(() => {
            Message.error('验证失败，请重新登录!')
            next({ path: '/login' })
          })
        })
      } else {
        next()
      }
    }

  }else{
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next('/login')
      NProgress.done()
    }
  }

})

router.afterEach(() => {
  NProgress.done() // 结束Progress
})
