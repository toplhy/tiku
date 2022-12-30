import Vue from 'vue'
import App from './App'

import store from '@/common/store'
import validate from '@/common/ys-validate.js'

Vue.config.productionTip = false

Vue.prototype.$store = store
Vue.prototype.$validate = validate

App.mpType = 'app'

const app = new Vue({
	validate,
	store,
	...App
})
app.$mount()
