<template>
	<view class="content">
		<view class="input-group">
			<view class="input-row border">
				<text class="title">账&emsp;&emsp;号：</text>
				<m-input class="m-input" type="text" clearable focus v-model="username" placeholder="请输入账号"></m-input>
			</view>
			<view class="input-row">
				<text class="title">密&emsp;&emsp;码：</text>
				<m-input type="password" displayable v-model="password" placeholder="请输入密码"></m-input>
			</view>
		</view>
		<view class="btn-row">
			<button type="primary" class="primary" @tap="bindLogin">登录</button>
		</view>
		<view class="action-row">
			<navigator :url="regPath">注册账号</navigator>
		</view>
	</view>
</template>

<script>
	import config from '@/common/config.js';
	import { mapMutations } from 'vuex'
	import mInput from '@/components/m-input.vue'

	export default {
		components: {mInput},
		data() {
			return {
				regPath: '../reg/reg',
				username: '',
				password: '',
			}
		},
		methods: {
			...mapMutations(['login']),
			bindLogin() {
				let userInfo = {
					username: this.username,
					password: this.password
				};
				let loginRules = [{
						name: 'username',
						type: 'required',
						errmsg: '请输入账号'
					},
					{
						name: 'password',
						type: 'required',
						errmsg: '请输入密码'
					}
				];
				let valRegRes = this.$validate.validate(userInfo, loginRules)
				if (!valRegRes.isOk) {
					uni.showToast({
						icon: 'none',
						title: valRegRes.errmsg
					})
					return false
				} else{
					uni.request({
					    url: config.data.base_url + config.data.login_url, 
						method: 'POST',
						header:{
							'Content-type': 'application/x-www-form-urlencoded'
						},
					    data: userInfo,
					    success: (res) => {
							if(res.data.result){
								this.login({
									userId: res.data.userId, 
									username: res.data.username,
									userAvatar: res.data.userAvatar
								});
								uni.reLaunch({
									url: '../main/main',
								});
							}else{
								uni.showToast({
									icon: 'none',
									title: res.data.message
								});
							}
					    }
					});
				}
			}
		}
	}
</script>

<style>
	.action-row {
		display: flex;
		flex-direction: row;
		justify-content: center;
	}

	.action-row navigator {
		color: #007aff;
		padding: 0 10px;
	}

	.oauth-row {
		display: flex;
		flex-direction: row;
		justify-content: center;
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
	}

	.oauth-image {
		position: relative;
		width: 50px;
		height: 50px;
		border: 1px solid #dddddd;
		border-radius: 50px;
		margin: 0 20px;
		background-color: #ffffff;
	}

	.oauth-image image {
		width: 30px;
		height: 30px;
		margin: 10px;
	}

	.oauth-image button {
		position: absolute;
		left: 0;
		top: 0;
		width: 100%;
		height: 100%;
		opacity: 0;
	}
</style>
