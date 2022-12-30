<template>
	<view class="content">
		<view v-if="msg">
			<view class="tit-row" v-if="msg == 'noSettings'">
				<text>请先进行刷题设置</text>
			</view>
			<view class="tit-row" v-if="msg == 'noQuestions'">
				<text>未找到符合条件的题目</text>
			</view>
		</view>
		<view class="body-content" v-if="!msg">
			<swiper class="body-content" :current="currentIndex" @change="swiperChange">
				<swiper-item v-for="(question,index) in questionList" :key="question.id" :index="index">
					<scroll-view scroll-y="true" :style="{height:scrollHeight}" >
						<view class="question-row">
							<text class="q-number">[{{index+1}}/{{questionCount}}]</text>
							<text class="q-content">{{question.content}}</text>
							<view v-if="question.hasPic">
								<!-- 替换问题图片 -->
								<image class="q-pic" src="../../static/img/avatar.png"></image>
							</view>
						</view>
						<view class="option-list">
							<radio-group v-if="question.type == '01'" @change="radioChange">
								<label class="option-list-cell" v-for="(option, index1) in question.options" :key="option.id">
									<view><radio :value="`${index}-${index1}-${option.oNo}`" :checked="option.isCheck" :disabled="option.isDisabled" :color="option.color"/></view>
									<view>{{option.oNo}}.{{option.content}}</view>
								</label>
							</radio-group>
							<checkbox-group v-if="question.type == '02'" @change="checkboxChange">
								<label class="option-list-cell" v-for="(option, index1) in question.options" :key="option.id">
									<view><checkbox :value="`${index}-${index1}-${option.oNo}`" :checked="option.isCheck" :disabled="option.isDisabled" :color="option.color"/></view>
									<view>{{option.oNo}}.{{option.content}}</view>
								</label>
							</checkbox-group>
						</view>
						<view v-if="question.isAnswer">
							<view class="answer-row">
								<text class="a-text">正确答案：</text>{{question.answer}}
							</view>
							<view class="analysis-row">
								<view class="analysis-row a-text">解析：</view>
								<view class="analysis-text">{{question.analysis}}</view>
							</view>
						</view>
						<view v-if="question.type == '02' && !question.isAnswer">
							<button type="primary" class="primary" @tap="doMultSelect">确定</button>
						</view>
					</scroll-view>
					<view class="btn-group">
						<button v-if="!question.isStar" size="mini" @tap="doStar(index, 'star')">收藏</button>
						<button v-if="question.isStar" size="mini" @tap="doStar(index, 'unstar')">取消收藏</button>
						<button size="mini" @tap="openTab">概览</button>
					</view>
				</swiper-item>
			</swiper>
		</view>
		<view>
			<uni-popup ref="popup" type="bottom">
				<view class="popup-area">
					<uni-badge class="popup-item" v-for="(question,index) in questionList" :key="question.id" :index="index"
					 :text="index+1" :type="question.isRight" @click="toQuestion(index)"></uni-badge>
				</view>
			</uni-popup>
		</view>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex'
	import config from '@/common/config.js';
	import uniPopup from "@/components/uni-popup/uni-popup.vue";
	import uniBadge from "@/components/uni-badge/uni-badge.vue";


	export default {
		components: {uniPopup, uniBadge},
		computed: {
			...mapState(['userId', 'username'])
		},
		data() {
			return {
				msg: '',
				settings: null,
				currentIndex: 0,
				questionCount: 0,
				scrollHeight: '600px',
				questionList: [],
				multSelect: []
			}
		},
		methods: {
			getUserSettings() {
				let that = this;
				uni.request({
					url: config.data.base_url + config.data.setting_get_url,
					data: {
						userId: that.userId
					},
					success: (res) => {
						if (res.data.isSetting) {
							that.settings = res.data.settings;
							that.getTikuQuestions();
						} else {
							that.msg = 'noSettings';
						}
					}
				});
			},
			getTikuQuestions() {
				let that = this;
				uni.request({
					url: config.data.base_url + config.data.questions_list_url,
					data: {
						qbankId: that.settings.qbankId,
						rankId: that.settings.rankId,
						modelId: that.settings.modelId,
						userId: that.userId
					},
					success: (res) => {
						if (res.data.count < 1) {
							that.msg = 'noQuestions';
						} else {
							that.questionCount = res.data.count;
							that.questionList = res.data.rows;
						}
					}
				});
			},
			swiperChange(e) {
				if(e.detail.current == (this.questionCount-1)){
					uni.showToast({
						icon: 'none',
						title: '已经是最后一题了'
					});
				}
			},
			radioChange: function(evt) {
				let checked = evt.detail.value.split('-');
				let question = this.questionList[checked[0]];
				// 设置已经回答
				question.isAnswer = true;
				// 选项禁用
				for(let index in question.options) { 
					let option = question.options[index];
					option.isDisabled = true;
					if(index == checked[1]) {
						option.isCheck = true;
					}
				}; 
				// 判断对错
				if(question.answer == checked[2]) {
					question.options[checked[1]].color = 'green';
					question.isRight = 'success';
				} else{
					question.options[checked[1]].color = 'red';
					question.isRight = 'error';
				}
				// 进行记录
				this.doRecord(question, checked[2]);
			},
			checkboxChange: function(evt) {
				this.multSelect = evt.detail.value;
			},
			doMultSelect() {
				if(this.multSelect.length == 0){
					uni.showToast({
						icon: 'none',
						title: '请选择答案'
					});
					return;
				}
				let qIndex = this.multSelect[0].split('-')[0];
				let oIndex = [], selects = [];
				for(let i in this.multSelect) {
					let arr = this.multSelect[i].split('-');
					oIndex.push(arr[1]);
					selects.push(arr[2]);
				}; 
				// 设置已回答
				let question = this.questionList[qIndex];
				question.isAnswer = true;
				// 选项禁用
				for(let index in question.options) { 
					let option = question.options[index];
					option.isDisabled = true;
					if(oIndex.indexOf(index) != -1) {
						option.isCheck = true;
					}
				}; 
				// 判断对错
				for(let index in selects) {
					let ss = selects[index];
					if(question.answer.indexOf(ss) != -1) {
						question.options[oIndex[index]].color = 'green';
					} else{
						question.options[oIndex[index]].color = 'red';
					}
				}; 
				if(selects.join(',') == question.answer){
					question.isRight = 'success';
				}else{
					question.isRight = 'error';
				}
				// 进行记录
				this.doRecord(question, selects.join(','));
			},
			doRecord(question, choices) {
				let that = this;
				uni.request({
					url: config.data.base_url + config.data.questions_record_url,
					data: {
						questionId: question.id,
						userId: that.userId,
						choices: choices,
						isRight: question.isRight=='success'?true:false
					}
				});
			},
			doStar(index, state) {
				let that = this;
				uni.request({
					url: config.data.base_url + config.data.questions_star_url,
					data: {
						questionId: that.questionList[index].id,
						userId: that.userId,
						state: state,
					},
					success: (res) => {
						uni.showToast({
							icon: 'none',
							title: res.data.message
						});
						if(res.data.result){
							if(state == 'star'){
								that.questionList[index].isStar = true;
							}else{
								that.questionList[index].isStar = false;
							}
						}
					}
				});
			},
			openTab() {
				this.$refs.popup.open();
			},
			toQuestion(index) {
				this.currentIndex = index;
				this.$refs.popup.close();
			}
		},
		onLoad() {
			let that = this;
			this.getUserSettings();
			uni.getSystemInfo({
				success(res) {
					that.scrollHeight = (res.windowHeight - 60) + 'px';
				}
			})
		}
	}
</script>

<style>
	.tit-row {
		min-height: 100px;
		text-align: center;
		margin: 50px 0;
	}

	.body-content {
		height: 100%;
	}

	.scroll-content {
		height: 600px;
	}

	.question-row {
		margin-bottom: 20px;
	}

	.question-row .q-number {
		color: orange;
		margin-right: 5px;
	}

	.question-row .q-content {
		font-size: 15px;
		line-height: 25px;
	}
	
	.question-row .q-pic {
		max-weight: 100%;
		max-height: 200px;
	}

	.option-list {
		position: relative;
		width: 100%;
		display: flex;
		flex-direction: column;
		margin-bottom: 15px;
	}

	.option-list-cell {
		position: relative;
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 10px;
		justify-content: flex-start
	}
	.answer-row{
		font-size: 15px;
		margin: 15px 0;
	}
	.analysis-row{
		font-size: 15px;
		margin-bottom: 15px;
	}
	.a-text{
		color: orange;
	}
	.analysis-text{
		line-height: 25px;
	}
	.btn-group{
		text-align: center;
	}
	.popup-area{
		background-color: #FFFFFF;
		padding: 10px;
		flex-direction: column;
		-webkit-flex-direction: column;
	}
	.uni-badge{
		display: initial;
		margin-right: 10px;
		margin-bottom: 5px;
	}
</style>
