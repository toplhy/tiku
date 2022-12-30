import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
	state: {
		forcedLogin: false,
		hasLogin: false,
		userId: '',
		username: '',
	},
	mutations: {
		login(state, userInfo) {
			state.userId = userInfo.userId;
			state.username = userInfo.username;
			state.hasLogin = true;
			uni.setStorage({
				key: 'USER_INFO',
				data: userInfo
			});
		},
		logout(state) {
			state.userId = '';
			state.username = '';
			state.hasLogin = false;
			uni.removeStorage({
				key: 'USER_INFO',
			});
		}
	}
})

export default store
