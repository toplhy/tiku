<template>
	<view class="content">
		<scroll-view class="tiku-list">
			<!-- <uni-card is-full="true" :title="tiku.name" note="note" v-for="(tiku,index) in tikuList" :key="tiku.id" :index="index">
			    <text class="card-warning">{{tiku.isOpen?'公开':'未公开'}}</text>
				<text v-if="tiku.description">{{tiku.description}}</text>
			    <template v-slot:footer>
			        <view class="footer-box">
			            <view>
							<label class="del" @tap="delfile">删除</label>
							<label class="del" @tap="delfile">删除</label>
							<label class="del" @tap="delfile">删除</label>
						</view>
			        </view>
			    </template>
			</uni-card> -->
			<uni-list>
			    <uni-list-item :title="tiku.name" :show-arrow="true" :rightText="tiku.isOpen?'公开':'未公开'"
				 v-for="(tiku,index) in tikuList" :key="tiku.id" :index="index" @click="showDetail"></uni-list-item>
			</uni-list>
		</scroll-view>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex'
	import config from '@/common/config.js';
	import uniList from "@/components/uni-list/uni-list.vue"
	import uniListItem from "@/components/uni-list-item/uni-list-item.vue"
	
	export default {
		components: {uniList, uniListItem},
		computed: {
			...mapState(['userId', 'username'])
		},
		data() {
			return {
				tikuList: []
			}
		},
		methods: {
			getMyQbanks(){
				let that = this;
				uni.request({
				    url: config.data.base_url + config.data.tiku_list_url, 
					method: 'POST',
					header:{
						'Content-type': 'application/x-www-form-urlencoded'
					},
				    data: {
						userId: that.userId
					},
				    success: (res) => {
						that.tikuList = res.data;
				    }
				});
			},
			showDetail() {
				uni.showToast({
					icon: 'none',
					title: '还没有做'
				})
			}
		},
		onLoad() {
			this.getMyQbanks();
		}
	}
</script>

<style>
	.tiku-list{
		height: 100%;
	}
	.card-warning{
		margin-right: 10px;
		color: orange;
	}
</style>
