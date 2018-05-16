import Vue from 'vue'
import Router from 'vue-router'

// in development-env not use lazy-loading, because lazy-loading too many pages will cause webpack hot update too slow. so only in production use lazy-loading;
// detail: https://panjiachen.github.io/vue-element-admin-site/#/lazy-loading

Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'

/**
 * hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
 *                                if not set alwaysShow, only more than one route under the children
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noredirect           if `redirect:noredirect` will no redirct in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
 **/
export const constantRouterMap = [
  {path: '/login', component: () => import('@/views/login/index'), hidden: true},
  {path: '/404', component: () => import('@/views/404'), hidden: true},

  {
    path: '',
    component: Layout,
    redirect: 'dashboard',
    children: [{
      path: 'dashboard',
      component: () => import('@/views/dashboard/index'),
      name: 'dashboard',
      meta: {title: '首页', noCache: true}
    }]
  },
  {
    path: '/businessManagement',
    component: Layout,
    name: 'businessManagement',
    children: [
      {
        path: 'businessLists',
        name: 'businessLists',
        component: () => import('@/views/table/index'),
        meta: {title: '企业列表', noCache: true}
      },
      {
        path: 'enterpriseAudit',
        name: 'enterpriseAudit',
        component: () => import('@/views/businessManagement/enterpriseAudit'),
        meta: {title: '企业审核', noCache: true}
      },
    ]
  },
  {
    path: '/authorityManagement',
    name: 'authorityManagement',
    redirect: 'noredirect',
    component: Layout,
    meta: {
      title: '权限管理'
    },
    children: [
      {
        path: 'user',
        name: 'authorityManagementUser',
        component: () => import('@/views/authorityManagement/user'),
        meta: {title: '后台用户管理', noCache: true}
      }, {
        path: 'roleManagement',
        name: 'roleManagement',
        component: () => import('@/views/authorityManagement/roleManagement'),
        meta: {title: '角色管理', noCache: true}
      }, {
        path: 'departmentManagement',
        name: 'departmentManagement',
        component: () => import('@/views/authorityManagement/departmentManagement'),
        meta: {title: '部门管理', noCache: true}
      }, {
        path: 'menuManagement',
        name: 'menuManagement',
        component: () => import('@/views/authorityManagement/menuManagement'),
        meta: {title: '菜单管理', noCache: true}
      }
    ]
  },
  {
    path: '/organizationalManagenment',
    name: 'organizationalManagenment',
    component: Layout,
    redirect: 'noredirect',
    meta: {
      title: '机构管理'
    },
    children: [
      {
        path: 'mechanismList',
        name: 'mechanismList',
        component: () => import('@/views/organizationalManagenment/mechanismList'),
        meta: {title: '机构列表', noCache: true}
      }, {
        path: 'institutionalReview',
        name: 'institutionalReview',
        component: () => import('@/views/organizationalManagenment/institutionalReview'),
        meta: {title: '机构审核', noCache: true}
      },
      {
        path: 'account',
        name: 'account',
        component: () => import('@/views/organizationalManagenment/account'),
        meta: {title: '机构账户', noCache: true}
      }
    ]
  },
  {
    path: '/sowingMap',
    name: 'sowingMap',
    component: Layout,
    redirect: 'noredirect',
    meta: {
      title: '广告管理'
    },
    children: [
      {
        path: 'sowingMapList',
        name: 'sowingMapList',
        component: () => import('@/views/sowingMap/sowingMapList'),
        meta: {title: '轮播管理', noCache: true}
      }
    ]
  },
  {
    path: '/notice',
    name: 'notice',
    component: Layout,
    redirect: 'noredirect',
    meta: {
      title: '通知'
    },
    children: [
      {
        path: 'noticeManage',
        name: 'noticeManage',
        component: () => import('@/views/notice/noticeManage'),
        meta: {title: '公告管理', noCache: true}
      },
      {
        path: 'pushModel',
        name: 'pushModel',
        component: () => import('@/views/notice/pushModel'),
        meta: {title: '推送模板', noCache: true}
      }
    ]
  },
  {
    path: '/userManagement',
    name: 'userManagement',
    redirect: 'noredirect',
    meta: {
      title: '用户管理'
    },
    component: Layout,
    children: [
      {
        path: 'userList',
        name: 'userList',
        component: () => import('@/views/userManagement/userList'),
        meta: {title: '用户列表', noCache: true}
      }, {
        path: 'reportList',
        name: 'reportList',
        component: () => import('@/views/userManagement/reportList'),
        meta: {title: '报告列表', noCache: true}
      }
    ]
  },
  {
    path: '/productManagement',
    name: 'productManagement',
    redirect: 'noredirect',
    meta: {
      title: '产品管理'
    },
    component: Layout,
    children: [
      {
        path: 'supplyManagement',
        name: 'supplyManagement',
        component: () => import('@/views/productManagement/supplyManagement'),
        meta: {title: '接口管理', noCache: true}
      },
      {
        path: 'productList',
        name: 'productList',
        component: () => import('@/views/productManagement/productList'),
        meta: {title: '产品列表', noCache: true}
      },
      {
        path: 'moduleList',
        name: 'moduleList',
        component: () => import('@/views/productManagement/moduleList'),
        meta: {title: '模块管理', noCache: true}
      }
    ]
  },
  {
    path: '/parameter',
    component: Layout,
    children: [{
      path: '',
      name: 'parameter',
      meta: {
        title: '参数配置'
      },
      component: () => import('@/views/parameter/index')
    }]
  },
  {path: '*', redirect: '/404', hidden: true}
]

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({y: 0}),
  routes: constantRouterMap
})


// organizationalManagenment
