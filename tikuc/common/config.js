const data = {
	// base_url: 'https://toplhy.utools.club/tikud/api/',
	base_url: 'http://localhost:8081/tikud/api/',
	// base_url: 'http://101.200.158.213:8090/tikud/api/',
	// 登录接口
	login_url: 'doLogin',
	// 注册接口
	register_url: 'doRegister',
	// 上传头像接口
	avatar_upload_url: 'avatarUpload',
	// 显示头像接口
	avatar_show_url: 'avatarShow',
	// 获取题库列表接口
	tiku_list_url: 'tikuList',
	// 获取字典接口
	dics_list_url: 'dicsList',
	// 获取刷题设置接口
	setting_get_url: 'settingGet',
	// 保存设置接口
	setting_save_url: 'settingSave',
	// 获取问题列表接口
	questions_list_url: 'questionList',
	// 记录问题操作接口
	questions_record_url: 'doQuestionRecord',
	// 收藏/取消收藏问题操作接口
	questions_star_url: 'doQuestionStar',
	// 保存题库
	qbank_save_url: 'doQbankSave',
}
const item = [{
		name: '开始刷题',
		icon: 'arrowthinright',
		pagePath: '../prac/prac'
	},
	{
		name: '刷题设置',
		icon: 'gear',
		pagePath: '../setting/setting'
	},
	{
		name: '导入题库',
		icon: 'cloud-upload',
		pagePath: '../tiku/tiku'
	},
	{
		name: '我的题库',
		icon: 'list',
		pagePath: '../tlist/tlist'
	}
]

export default {
	data,
	item
}
