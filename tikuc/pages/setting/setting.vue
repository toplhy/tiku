<template>
	<view class="content">
		<view v-if="doSetting">
			<view class="setting-list">
				<view class="setting-list-cell">
					<view class="setting-list-cell-left">
						选择题库
					</view>
					<view class="setting-list-cell-db">
						<picker @change="tikuPickerChange" :value="tikuIndex" :range="tikuList" range-key="name">
							<view class="setting-input">{{tikuList[tikuIndex].name}}</view>
						</picker>
					</view>
				</view>
			</view>

			<view class="setting-list">
				<view class="setting-list-cell">
					<view class="setting-list-cell-left">
						题库范围
					</view>
					<view class="setting-list-cell-db">
						<picker @change="rankPickerChange" :value="rankIndex" :range="rankList" range-key="name">
							<view class="setting-input">{{rankList[rankIndex].name}}</view>
						</picker>
					</view>
				</view>
			</view>
			
			<view class="setting-list">
				<view class="setting-list-cell">
					<view class="setting-list-cell-left">
						练习模式
					</view>
					<view class="setting-list-cell-db">
						<picker @change="modelPickerChange" :value="modelIndex" :range="modelList" range-key="name">
							<view class="setting-input">{{modelList[modelIndex].name}}</view>
						</picker>
					</view>
				</view>
			</view>

			<view class="btn-row">
				<button type="primary" @tap="saveSettings">保存</button>
			</view>
		</view>

		<view v-if="!doSetting">
			<view class="setting-list">
				<view class="setting-list-cell">
					<view class="setting-list-cell-left">
						当前题库
					</view>
					<view class="setting-list-cell-db">
						<view class="setting-input">{{ settings!=null?settings.qbankName:'未设置' }}</view>
					</view>
				</view>
			</view>

			<view class="setting-list">
				<view class="setting-list-cell">
					<view class="setting-list-cell-left">
						题库范围
					</view>
					<view class="setting-list-cell-db">
						<view class="setting-input">{{ settings!=null?settings.rankName:'未设置' }}</view>
					</view>
				</view>
			</view>
			
			<view class="setting-list">
				<view class="setting-list-cell">
					<view class="setting-list-cell-left">
						练习模式
					</view>
					<view class="setting-list-cell-db">
						<view class="setting-input">{{ settings!=null?settings.modelName:'未设置' }}</view>
					</view>
				</view>
			</view>
			
			<view class="btn-row">
				<button v-if="!settings" type="primary" @tap="doSettings">设置</button>
				<button v-if="settings" type="primary" @tap="doSettings">修改</button>
			</view>
		</view>
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
		computed: {
			...mapState(['userId', 'username'])
		},
		data() {
			return {
				doSetting: false,
				settings: null,
				tikuList: [{ id: 0, name: '请选择' }],
				rankList: [{ id: 0, name: '请选择' }],
				modelList: [{ id: 0, name: '请选择' }],
				tikuIndex: 0,
				rankIndex: 0,
				modelIndex: 0
			}
		},
		methods: {
			getSetting() {
				let that = this;
				uni.request({
					url: config.data.base_url + config.data.setting_get_url,
					data: {
						userId: that.userId
					},
					success: (res) => {
						if (res.data.isSetting) {
							that.settings = res.data.settings;
						}
					}
				});
			},
			tikuPickerChange: function(e) {
				this.tikuIndex = e.detail.value;
			},
			rankPickerChange: function(e) {
				this.rankIndex = e.detail.value;
			},
			modelPickerChange: function(e) {
				this.modelIndex = e.detail.value;
			},
			getTikuList() {
				let that = this;
				uni.request({
					url: config.data.base_url + config.data.tiku_list_url,
					data: {
						userId: that.userId
					},
					success: (res) => {
						that.tikuList = that.tikuList.concat(res.data);
					}
				});
			},
			getDicsList(type) {
				let that = this;
				uni.request({
					url: config.data.base_url + config.data.dics_list_url,
					data: {
						type: type
					},
					success: (res) => {
						if(type == 'LXMS'){
							that.modelList = that.modelList.concat(res.data);
						} else if(type == 'TKFW') {
							that.rankList = that.rankList.concat(res.data);
						}else{
							
						}
					}
				});
			},
			doSettings() {
				this.doSetting = true;
				this.getTikuList();
				this.getDicsList('TKFW');
				this.getDicsList('LXMS');
			},
			saveSettings() {
				let that = this;
				if(that.tikuIndex < 1){
					uni.showToast({
						icon: 'none',
						title: '请选择题库'
					});
				} else if(that.rankIndex < 1){
					uni.showToast({
						icon: 'none',
						title: '请选择题库范围'
					})
				} else if(that.modelIndex < 1){
					uni.showToast({
						icon: 'none',
						title: '请选择练习模式'
					})
				} else{
					let data = {
						userId: that.userId,
						settingsId: that.settings!=null?that.settings.id:null,
						qbankId: that.tikuList[that.tikuIndex].id,
						rankId: that.rankList[that.rankIndex].id,
						modelId: that.modelList[that.modelIndex].id
					}
					uni.request({
					    url: config.data.base_url + config.data.setting_save_url, 
						method: 'POST',
						header:{
							'Content-type': 'application/x-www-form-urlencoded'
						},
					    data: data,
					    success: (res) => {
							uni.showToast({
								icon: 'none',
								title: res.data.message
							});
							if(res.data.result){
								that.doSetting = false;
								that.settings = res.data.settings;
							}
					    }
					});
				}		
			}
		},
		onLoad() {
			this.getSetting();
		}
	}
</script>

<style>
	.setting-list {
		background-color: #FFFFFF;
		position: relative;
		width: 100%;
		display: flex;
		flex-direction: column;
	}

	.setting-list:after {
		position: absolute;
		z-index: 10;
		right: 0;
		bottom: 0;
		left: 0;
		height: 1px;
		content: '';
		-webkit-transform: scaleY(.5);
		transform: scaleY(.5);
		background-color: #c8c7cc;
	}

	.setting-list::before {
		position: absolute;
		z-index: 10;
		right: 0;
		top: 0;
		left: 0;
		height: 1px;
		content: '';
		-webkit-transform: scaleY(.5);
		transform: scaleY(.5);
		background-color: #c8c7cc;
	}

	.setting-list-cell {
		position: relative;
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
	}

	.setting-list-cell-left {
		white-space: nowrap;
		font-size: 28rpx;
		padding: 0 30rpx;
	}

	.setting-list-cell-db,
	.setting-list-cell-right {
		flex: 1;
	}

	.setting-input {
		height: 50rpx;
		padding: 15rpx 25rpx;
		line-height: 50rpx;
		font-size: 28rpx;
		background: #FFF;
		flex: 1;
	}
</style>
