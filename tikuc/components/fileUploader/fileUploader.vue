<template>
	<view>
		<view v-if="showfile" class="file">
			<label class="filename">{{filename}}</label>
			<label class="del" @tap="delfile">删除</label>
		</view>
		<view ref="input" class="input">

		</view>
		<button v-if="showBtn" class="btn" type="primary" @tap="selectBtn">选择文件</button>
	</view>
</template>

<script>
	export default {
		name: 'FileUploader',
		props: ["uploadfileUrl", "limitsize", "accepttype"],
		data() {
			return {
				showfile: false,
				showBtn: true,
				filename: 'xxx',
				filepath: ''
			};
		},
		// mounted 页面初始化的方法
		mounted() {
			// #ifdef H5
			var input = document.createElement('input')
			input.type = 'file'
			if (this.accepttype) {
				input.accept = this.accepttype
			}
			input.style.display = 'none'
			input.id = 'file'
			input.onchange = (event) => {
				let this_ = this;
				// 上传附件 获取文件信息
				let fileinfo = event.target.files[0];
				let fileSize = fileinfo.size;
				let fileName = fileinfo.name;
				// 转换文件路径
				var reader = new FileReader();
				reader.readAsDataURL(fileinfo);
				reader.onload = function(e) {
					let path = e.target.result;
					this_.checkFile(path, fileName, fileSize);
				}
			}
			this.$refs.input.$el.appendChild(input);
			// #endif
		},
		methods: {
			// 上传附件按钮 绑定file的点击事件
			selectBtn() {
				let this_ = this;
				// #ifdef H5
				document.getElementById("file").click();
				// #endif
				// #ifdef APP-PLUS
				let REQUESTCODE = 1;
				let main = plus.android.runtimeMainActivity();
				let Intent = plus.android.importClass('android.content.Intent');
				let intent = new Intent(Intent.ACTION_GET_CONTENT);

				if (this_.accepttype) {
					intent.setType(this_.accepttype);
				} else {
					intent.setType("*/*"); //设置类型，任意类型  				
				}
				intent.addCategory(Intent.CATEGORY_OPENABLE);

				main.onActivityResult = function(requestCode, resultCode, data) {
					if (REQUESTCODE == requestCode) {
						let context = main;
						plus.android.importClass(data);
						// 获得文件路径  
						let fileData = data.getData();
						plus.android.importClass(fileData);
						let path = fileData.getPath();
						let fileName = path.substring(path.lastIndexOf("\/") + 1);
						let resolver = context.getContentResolver();
						let fileSize = plus.android.invoke(resolver, "getSize", fileData);
						path = 'file://' + path;
						this_.checkFile(path, fileName, fileSize);
					}
				}
				main.startActivityForResult(intent, REQUESTCODE);
				// #endif
			},
			// 校验附件
			checkFile(filePath, fileName, fileSize) {
				let this_ = this;
				// 校验文件大小
				if (this_.limitsize && fileSize && fileSize > this_.limitsize) {
					// #ifdef H5					
					document.getElementById("file").value = '';
					// #endif
					uni.showToast({
						icon: 'none',
						title: '文件过大'
					})
				} else {
					// 显示文件名和删除
					this_.showfile = true;
					this_.showBtn = false;
					this_.filepath = filePath;
					this_.filename = fileName;
				}
			},
			// 删除附件
			delfile() {
				// #ifdef H5
				document.getElementById("file").value = '';
				// #endif
				this.showBtn = true;
				this.showfile = false;
				this.filename = '';
				this.filepath = '';
			},
			// 获取附件路径
			getFilePath() {
				return this.filepath;
			}
		}
	}
</script>

<style>
	.file {
		font-size: 32upx;
		padding: 10upx;
	}

	.filename {
		color: #808080;
	}

	.del {
		color: #0E90CE;
		float: right;
	}

	.btn {
		width: 100%;
		margin: 20upx 0;
		color: #007AFF;
		background-color: #FFFFFF;
		border: #007AFF 2upx solid;
	}
</style>
