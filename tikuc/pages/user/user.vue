<template>
	<view class="content">
		<view class="user_view">
			<view class="avatar" @click="chooseImg">
				<image class="ava_img" :src="`${userAvatar}/${userId}`"></image>
				<text>{{username}}</text>
			</view>
		</view>
		<view>
			<uni-list>
			    <uni-list-item title="账号" :show-badge="true" :show-arrow="false" :rightText="username"></uni-list-item>
			    <uni-list-item title="手机号码" :show-badge="true" :show-arrow="false"></uni-list-item>
			    <uni-list-item title="电子邮箱" :show-badge="true" :show-arrow="false"></uni-list-item>
			</uni-list>
		</view>
		<view class="btn-row">
			<button type="default" @tap="bindLogout">退出登录</button>
		</view>
	</view>
</template>

<script>
	import {mapState,mapMutations} from 'vuex'
	import config from '@/common/config.js';
	import uniList from "@/components/uni-list/uni-list.vue"
	import uniListItem from "@/components/uni-list-item/uni-list-item.vue"
	
	export default {
		components: {uniList,uniListItem},
		computed: {
			...mapState(['userId', 'username'])
		},
		data() {
			return {
				userAvatar: config.data.base_url + config.data.avatar_show_url,
			}
		},
		methods: {
			...mapMutations(['logout']),
			bindLogout() {
				this.logout();
				uni.reLaunch({
					url: '../main/main',
				});
			},
			chooseImg() {
				let that = this;
				uni.chooseImage({
				    count: 1, //默认9
				    sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
				    sourceType: ['album', 'camera'], //从相册选择
				    success: function (res) {
				        const tempFilePaths = res.tempFilePaths;
						const uploadTask = uni.uploadFile({
							url : config.data.base_url + config.data.avatar_upload_url, 
							filePath: tempFilePaths[0],
							name: 'avatarFile',
							formData: {
								'userId': that.userId
							},
							success: function (res) {
								uni.showToast({
									icon: 'none',
									title: res.data.message
								});
							}
						});
				    }
				});
			}
		}
	}
</script>

<style>
	
</style>
