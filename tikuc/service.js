
const USERS_KEY = 'userInfo';
const STATE_KEY = 'off';

const getUsers = function() {
	let ret = '';
	ret = uni.getStorageSync(USERS_KEY);
	if (!ret) {
		ret = '[]';
	}
	return JSON.parse(ret);
}

const addUser = function(userId, username) {
	let user = {
		id: userId,
		username: username
	};
	uni.setStorage(USERS_KEY, JSON.stringify(users));
	uni.setStorage(STATE_KEY, 'on');
}

export default {
	getUsers,
	addUser
}
