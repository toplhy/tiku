<template>
	<view class="content">
		<view class="uni-common-mt">
			<view class="uni-form-item uni-column">
				<view class="title">题库名称</view>
				<input class="uni-input" focus placeholder="题库名称" v-model="qbankName" />
			</view>
			<view class="uni-form-item uni-column">
				<view class="title">题库描述</view>
				<view class="uni-textarea">
					<textarea auto-height placeholder="题库描述" maxlength="400" v-model="qbankDesc" />
					</view>
			</view>
			 <view class="uni-list">
				<view class="uni-list-cell uni-list-cell-pd">
					<view class="uni-list-cell-db">是否公开</view>
					<switch checked @change="switchChange"/>
				</view>
			</view>
			<view class="uni-form-item uni-column">
				<view class="title">
					题库文件
					<text class="title-yaoqiu"@click="showPopup">格式要求</text>
				</view>
				<view class="uni-textarea">
					<file-uploader ref="uploader" :limitsize="1024000" accepttype="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"></file-uploader>
				</view>
			</view>
			<view class="btn-row">
				<button type="primary" class="primary" @tap="onUpload">提交</button>
			</view>
		</view>
		<view>
			<uni-popup ref="popup" type="bottom">
				<scroll-view class="popup-area">
					<view>1、只支持Excel2007，文件后缀为*.xlsx；</view>
					<view>2、请将数据所在的Sheet名称修改为“题库内容”；</view>
					<view>3、每行一题，仅支持单选、多选题，判断题请转换为单选题；</view>
					<view>4、第1列为题目内容；</view>
					<view>5、第2列为题目答案，多选答案请以英文逗号分割；</view>
					<view>6、第3列为答案解析；</view>
					<view>7、第4列开始依次为A-G的选项内容，请顺序填写，不要跳格。</view>
				</scroll-view>
			</uni-popup>
		</view>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex'
	import config from '@/common/config.js';
	import FileUploader from '@/components/fileUploader/fileUploader.vue';
	import uniPopup from "@/components/uni-popup/uni-popup.vue";
	
	export default {
		components: {FileUploader, uniPopup},
		computed: {
			...mapState(['userId', 'username'])
		},
		data() {
			return {
				qbankName: '',
				qbankDesc: '',
				qbankOpen: true,
				filepath: ''
			}
		},
		methods: {
			switchChange: function (e) {
				this.qbankOpen = e.target.value;
			},
			showPopup() {
				this.$refs.popup.open();
			},
			onUpload() {
				let that = this;
				that.filepath = that.$refs.uploader.getFilePath();
				let qbankInfo = {
					qbankName: that.qbankName,
					qbankDesc: that.qbankDesc,
					qbankOpen: that.qbankOpen,
					filepath: that.filepath,
					userId: that.userId
				}
				let qbankRules = [
					{name: 'qbankName',type: 'required',errmsg: '请输入题库名称'},
					{name: 'filepath',type: 'required',errmsg: '请选择题库文件'},
				];
				let valQbankRes = that.$validate.validate(qbankInfo, qbankRules)
				if (!valQbankRes.isOk) {
					uni.showToast({
						icon: 'none',
						title: valQbankRes.errmsg
					})
					return false
				} else{
					delete qbankInfo['filepath']
					const uploadTask = uni.uploadFile({
						url : config.data.base_url + config.data.qbank_save_url, 
						filePath: that.filepath,
						name: 'excelFile',
						formData: qbankInfo,
						success: function (res) {
							console.log("这里没法显示", res.data);
							uni.showToast({
								icon: 'none',
								title: res.data.message
							});
							if(res.data.result){
								uni.redirectTo({
								    url: '../tlist/tlist'
								});
							}
						}
					});
				}
			},
		}
	}
</script>

<style>
	.title-yaoqiu{
		margin-left: 10px;
		color: #0E90CE;
		font-size: 13px;
	}
	.popup-area{
		background-color: #FFFFFF;
		padding: 10px;
		height: 300px;
	}
</style>
