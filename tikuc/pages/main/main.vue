<template>
	<view class="content">
		<view class="user_view">
			<view v-if="hasLogin" class="avatar" @click="gotoPage(userPath)">
				<image class="ava_img" :src="`${userAvatar}/${userId}`"></image>
				<text>{{username}}</text>
			</view>
			<view v-if="!hasLogin" class="avatar" @click="gotoPage(loginPath)">
				<image class="ava_img" src="/static/img/avatar.png"></image>
				<text>未登录</text>
			</view>
		</view>	
		<uni-grid :column="3" @change="gridItemClick">
			<uni-grid-item class="grid_item" v-for="(item,index) in itemList" :key="item.name" :index="index">
				<uni-icons class="item_icon" :type="item.icon" size="50"></uni-icons>
				<text class="item_text">{{item.name}}</text>
			</uni-grid-item>
		</uni-grid>
	</view>
</template>

<script>
	import { mapState } from 'vuex'
	import config from '@/common/config.js';
	import uniGrid from "@/components/uni-grid/uni-grid.vue"
	import uniGridItem from "@/components/uni-grid-item/uni-grid-item.vue"
	import uniIcons from "@/components/uni-icons/uni-icons.vue"

	export default {
		components: {uniGrid,uniGridItem,uniIcons},
		computed: mapState([ 'hasLogin', 'username', 'userId']),
		data() {
			return {
				loginPath: '../login/login',
				userPath: '../user/user',
				userAvatar: config.data.base_url + config.data.avatar_show_url,
				itemList: config.item
			}
		},
		methods:{
			gotoPage(path) {
				if (!this.hasLogin) {
					path = this.$data.loginPath					
				}
				uni.getNetworkType({
					success(res) {
						console.log(res);
					}
				})
				uni.navigateTo({
					url: path
				});
			},
			gridItemClick(e){
				let index = e.detail.index;
				this.gotoPage(this.$data.itemList[index].pagePath);
			}
		}
	}
</script>

<style>
	.grid_item{
		text-align: center;
	}
	/* .item_icon{
		margin: 15px auto;
	} */
	.item_text{
		font-size: 13px;
	}
</style>
