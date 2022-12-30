<template>
	<view class="content">
		<view class="input-group">
			<view class="input-row border">
				<text class="title">账&emsp;&emsp;号：</text>
				<m-input type="text" focus clearable v-model="username" placeholder="请输入账号"></m-input>
			</view>
			<view class="input-row border">
				<text class="title">密&emsp;&emsp;码：</text>
				<m-input type="password" displayable v-model="password" placeholder="请输入密码"></m-input>
			</view>
			<view class="input-row border">
				<text class="title">手机号码：</text>
				<m-input type="text" clearable v-model="mobile" placeholder="请输入手机号码"></m-input>
			</view>
			<view class="input-row">
				<text class="title">电子邮箱：</text>
				<m-input type="text" clearable v-model="email" placeholder="请输入电子邮箱"></m-input>
			</view>
		</view>
		<view class="btn-row">
			<button type="primary" class="primary" @tap="register">注册</button>
		</view>
	</view>
</template>

<script>
	import config from '@/common/config.js';
	import mInput from '@/components/m-input.vue';

	export default {
		components: {
			mInput
		},
		data() {
			return {
				username: '',
				password: '',
				mobile: '',
				email: ''
			}
		},
		methods: {
			register() {
				let registerRules = [{
						name: 'username',
						type: 'required',
						errmsg: '请输入账号'
					},
					{
						name: 'password',
						type: 'required',
						errmsg: '请输入密码'
					},
					{
						name: 'password',
						type: 'pwd',
						errmsg: '密码须是6～16位字符'
					},
					{
						name: 'mobile',
						type: 'required',
						errmsg: '请输入手机号码'
					},
					{
						name: 'mobile',
						type: 'phone',
						errmsg: '请输入正确的手机号码'
					},
					{
						name: 'email',
						type: 'required',
						errmsg: '请输入电子邮箱'
					},
					{
						name: 'email',
						type: 'email',
						errmsg: '请输入正确的电子邮箱'
					}
				]
				let userInfo = {
					username: this.username,
					password: this.password,
					mobile: this.mobile,
					email: this.email
				}
				let valRegRes = this.$validate.validate(userInfo, registerRules)
				if (!valRegRes.isOk) {
					uni.showToast({
						icon: 'none',
						title: valRegRes.errmsg
					})
					return false
				} else {
					uni.request({
					    url: config.data.base_url + config.data.register_url,
						method: 'POST',
						header:{
							'Content-type': 'application/x-www-form-urlencoded'
						},
					    data: userInfo,
					    success: (res) => {
							uni.showToast({
								icon: 'none',
								title: res.data.message
							});
							if(res.data.result){
								uni.navigateBack({
									delta: 1
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

</style>
