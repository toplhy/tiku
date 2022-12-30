<template>
	<text v-if="text" :class="inverted ? 'uni-badge--' + type + ' uni-badge--' + size + ' uni-badge--' + type + '-inverted' : 'uni-badge--' + type + ' uni-badge--' + size"
	 :style="badgeStyle" class="uni-badge" @click="onClick()">{{ text }}</text>
</template>

<script>
	/**
	 * Badge 数字角标
	 * @description 数字角标一般和其它控件（列表、9宫格等）配合使用，用于进行数量提示，默认为实心灰色背景
	 * @tutorial https://ext.dcloud.net.cn/plugin?id=21
	 * @property {String} text 角标内容
	 * @property {String} type = [default|primary|success|warning|error] 颜色类型
	 * 	@value default 灰色
	 * 	@value primary 蓝色
	 * 	@value success 绿色
	 * 	@value warning 黄色
	 * 	@value error 红色
	 * @property {String} size = [normal|small] Badge 大小
	 * 	@value normal 一般尺寸
	 * 	@value small 小尺寸
	 * @property {String} inverted = [true|false] 是否无需背景颜色
	 * @event {Function} click 点击 Badge 触发事件
	 * @example <uni-badge text="1"></uni-badge>
	 */
	export default {
		name: 'UniBadge',
		props: {
			type: {
				type: String,
				default: 'default'
			},
			inverted: {
				type: Boolean,
				default: false
			},
			text: {
				type: [String, Number],
				default: ''
			},
			size: {
				type: String,
				default: 'normal'
			}
		},
		data() {
			return {
				badgeStyle: ''
			};
		},
		watch: {
			text() {
				this.setStyle()
			}
		},
		mounted() {
			this.setStyle()
		},
		methods: {
			setStyle() {
				this.badgeStyle = `width: ${String(this.text).length * 8 + 12}px`
			},
			onClick() {
				this.$emit('click');
			}
		}
	};
</script>

<style lang="scss" scoped>
	$bage-size: 12px;
	$bage-small: scale(0.8);
	$bage-height: 20px;
	/* 行为相关颜色 */
	$uni-color-primary: #007aff;
	$uni-color-success: #4cd964;
	$uni-color-warning: #f0ad4e;
	$uni-color-error: #dd524d;
	
	/* 文字基本颜色 */
	$uni-text-color:#333;//基本色
	$uni-text-color-inverse:#fff;//反色
	$uni-text-color-grey:#999;//辅助灰色，如加载更多的提示信息
	$uni-text-color-placeholder: #808080;
	$uni-text-color-disable:#c0c0c0;
	
	/* 背景颜色 */
	$uni-bg-color:#ffffff;
	$uni-bg-color-grey:#f8f8f8;
	$uni-bg-color-hover:#f1f1f1;//点击状态颜色
	$uni-bg-color-mask:rgba(0, 0, 0, 0.4);//遮罩颜色
	
	/* 边框颜色 */
	$uni-border-color:#c8c7cc;
	
	/* 尺寸变量 */
	
	/* 文字尺寸 */
	$uni-font-size-sm:24upx;
	$uni-font-size-base:28upx;
	$uni-font-size-lg:32upx;
	
	/* 图片尺寸 */
	$uni-img-size-sm:40upx;
	$uni-img-size-base:52upx;
	$uni-img-size-lg:80upx;
	
	/* Border Radius */
	$uni-border-radius-sm: 4upx;
	$uni-border-radius-base: 6upx;
	$uni-border-radius-lg: 12upx;
	$uni-border-radius-circle: 50%;
	
	/* 水平间距 */
	$uni-spacing-row-sm: 10px;
	$uni-spacing-row-base: 20upx;
	$uni-spacing-row-lg: 30upx;
	
	/* 垂直间距 */
	$uni-spacing-col-sm: 8upx;
	$uni-spacing-col-base: 16upx;
	$uni-spacing-col-lg: 24upx;
	
	/* 透明度 */
	$uni-opacity-disabled: 0.3; // 组件禁用态的透明度
	
	/* 文章场景相关 */
	$uni-color-title: #2C405A; // 文章标题颜色
	$uni-font-size-title:40upx;
	$uni-color-subtitle: #555555; // 二级标题颜色
	$uni-font-size-subtitle:36upx;
	$uni-color-paragraph: #3F536E; // 文章段落颜色
	$uni-font-size-paragraph:30upx;

	.uni-badge {
		/* #ifndef APP-PLUS */
		display: flex;
		/* #endif */
		justify-content: center;
		flex-direction: row;
		height: $bage-height;
		line-height: $bage-height;
		color: $uni-text-color;
		border-radius: 100px;
		background-color: $uni-bg-color-hover;
		background-color: transparent;
		text-align: center;
		font-family: 'Helvetica Neue', Helvetica, sans-serif;
		font-size: $bage-size;
		padding: 0px 6px;
	}

	.uni-badge--inverted {
		padding: 0 5px 0 0;
		color: $uni-bg-color-hover;
	}

	.uni-badge--default {
		color: $uni-text-color;
		background-color: $uni-bg-color-hover;
	}

	.uni-badge--default-inverted {
		color: $uni-text-color-grey;
		background-color: transparent;
	}

	.uni-badge--primary {
		color: $uni-text-color-inverse;
		background-color: $uni-color-primary;
	}

	.uni-badge--primary-inverted {
		color: $uni-color-primary;
		background-color: transparent;
	}

	.uni-badge--success {
		color: $uni-text-color-inverse;
		background-color: $uni-color-success;
	}

	.uni-badge--success-inverted {
		color: $uni-color-success;
		background-color: transparent;
	}

	.uni-badge--warning {
		color: $uni-text-color-inverse;
		background-color: $uni-color-warning;
	}

	.uni-badge--warning-inverted {
		color: $uni-color-warning;
		background-color: transparent;
	}

	.uni-badge--error {
		color: $uni-text-color-inverse;
		background-color: $uni-color-error;
	}

	.uni-badge--error-inverted {
		color: $uni-color-error;
		background-color: transparent;
	}

	.uni-badge--small {
		transform: $bage-small;
		transform-origin: center center;
	}
</style>
