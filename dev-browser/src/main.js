import Vue from 'vue'

import 'normalize.css/normalize.css'// A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import 'font-awesome/css/font-awesome.css'

import '@/styles/index.scss' // global css
import '@/styles/common.scss' //公共样式
import i18n from './lang'
import App from './App'
import router from './router'
import store from './store'
import axios from 'axios'

import 'font-awesome/css/font-awesome.min.css'
import '@/permission' // permission control
Vue.use(ElementUI)

Vue.config.productionTip = false
Vue.prototype.$axios = axios;
new Vue({
	el: '#app',
	router,
	store,
	template: '<App/>',
	components: { App }
})
