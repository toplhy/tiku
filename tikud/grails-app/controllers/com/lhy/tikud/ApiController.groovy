package com.lhy.tikud

import com.lhy.tikud.core.BaseRole
import com.lhy.tikud.core.BaseUser
import com.lhy.tikud.core.BaseUserBaseRole
import com.lhy.tikud.dics.Dictionary
import com.lhy.tikud.poi.ExcelReadBuilder
import com.lhy.tikud.tiku.QOptions
import com.lhy.tikud.tiku.Qbank
import com.lhy.tikud.tiku.Question
import com.lhy.tikud.tiku.QuestionRecord
import com.lhy.tikud.tiku.QuestionStar
import com.lhy.tikud.tiku.Settings
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class ApiController {

    /**
     * 登录接口
     * @return
     */
    def doLogin() {
        def map = [:]
        def user = BaseUser.findByUsername(params?.username)
        if (user) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            if (bCryptPasswordEncoder.matches(params?.password, user?.password)) {
                map.result = true
                map.userId = user?.id
                map.username = user?.username
                map.userAvatar = ""
            } else {
                map.result = false
                map.message = "用户名或密码不正确"
            }
        } else {
            map.result = false
            map.message = "用户不存在"
        }
        render map as JSON
    }

    /**
     * 注册接口
     * @return
     */
    def doRegister() {
        def map = [:]
        if(BaseUser.countByUsername(params?.username)){
            map.result = false
            map.message = "账号已被注册"
        }else{
            def user = new BaseUser(username: params?.username, password: params?.password, mobile: params?.mobile, email: params?.emial)
            if(user.save(flush: true)){
                BaseUserBaseRole.create(user, BaseRole.findByAuthority("ROLE_USER"))
                map.result = true
                map.message = "注册成功"
            }else{
                map.result = false
                map.message = "注册失败"
                println user.errors.allErrors
            }
        }
        render map as JSON
    }

    /**
     * 上传头像接口
     * @return
     */
    def avatarUpload() {
        def map = [:]
        def user = BaseUser.read(params?.userId)
        def avatar = request.getFile('avatarFile')
        if(user){
            if(avatar.size > 1 * 1024 * 1024){
                map.result = false
                map.message = "文件过大"
            }else{
                user.avatar = avatar?.bytes
                if(user.save(flush: true)){
                    map.result = true
                    map.message = "上传成功"
                }else{
                    map.result = false
                    map.message = "上传失败"
                }
            }
        }else{
            map.result = false
            map.message = "用户不存在"
        }
        render map as JSON
    }

    /**
     * 显示头像接口
     * @return
     */
    def avatarShow() {
        def user = BaseUser.read(params?.id);
        response.setContentType("image/jpeg")
        response.setContentLength(user?.avatar?.size())
        OutputStream out = response.outputStream
        out.write(user?.avatar)
        out.close()
    }

    /**
     * 获取题库列表接口
     * @return
     */
    def tikuList() {
        def user = BaseUser.read(params?.userId)
        def qbanks = [], qbankList = []
        if(BaseUserBaseRole.countByBaseUserAndBaseRole(user, BaseRole.findByAuthority("ROLE_ADMIN")) > 1){
            qbanks = Qbank.findAll()
        } else {
            qbanks = Qbank.findAllByIsOpenOrCreater(true, user)
        }
        qbanks.each {Qbank qbank ->
            def one = [:]
            one.id = qbank?.id
            one.name = qbank?.name
            one.description = qbank?.description
            one.isOpen = qbank?.isOpen
            qbankList << one
        }
        render qbankList as JSON
    }

    /**
     * 获取题库范围接口
     * @return
     */
    def dicsList() {
        def ranks = []
        Dictionary.findAllByType(params?.type).each{Dictionary dic->
            def one = [:]
            one.id = dic?.id
            one.name = dic?.name
            ranks << one
        }
        render ranks as JSON
    }

    /**
     * 获取刷题设置接口
     * @return
     */
    def settingGet() {
        def map = [:]
        def user = BaseUser.read(params?.userId)
        if(user){
            def settings = Settings.findBySettingUser(user)
            if(settings){
                map.isSetting = true
                def res = [:]
                res.id = settings?.id
                res.qbankId = settings?.qbank?.id
                res.qbankName = settings?.qbank?.name
                res.rankId = settings?.rank?.id
                res.rankName = settings?.rank?.name
                res.modelId = settings?.model?.id
                res.modelName = settings?.model?.name
                map.settings = res
            }else{
                map.isSetting = false
            }
        }else{
            map.result = false
            map.message = "用户不存在"
        }
        render map as JSON
    }

    /**
     * 刷题设置保存接口
     * @return
     */
    def settingSave() {
        def map = [:]
        def settings = Settings.read(params?.settingsId)
        if(settings && settings?.settingUser?.id?.toString() == params?.userId){
            settings.qbank = Qbank.read(params?.qbankId);
            settings.rank = Dictionary.read(params?.rankId)
            settings.model = Dictionary.read(params?.modelId)
        } else if(settings && settings?.settingUser?.id?.toString() != params?.userId) {
            map.result = false
            map.message = "数据不正确"
            render map as JSON
            return;
        } else{
            settings = new Settings(settingUser: BaseUser.read(params?.userId), qbank: Qbank.read(params?.qbankId),
                    rank: Dictionary.read(params?.rankId), model: Dictionary.read(params?.modelId))
        }
        if(settings.save(flush: true)){
            def res = [:]
            res.id = settings?.id
            res.qbankId = settings?.qbank?.id
            res.qbankName = settings?.qbank?.name
            res.rankId = settings?.rank?.id
            res.rankName = settings?.rank?.name
            res.modelId = settings?.model?.id
            res.modelName = settings?.model?.name
            map.result = true
            map.message = "设置成功"
            map.settings = res
        }else{
            map.result = false
            map.message = "设置失败"
            println settings.errors.allErrors
        }
        render map as JSON
    }

    /**
     * 获取问题列表接口
     * @return
     */
    def questionList() {
        def map = [:]
        def user = BaseUser.read(params?.userId)
        def qbank = Qbank.read(params?.qbankId);
        def rank = Dictionary.read(params?.rankId)
        def model = Dictionary.read(params?.modelId)
        def results = [], questions = []
        if(rank.code == '04'){ // 未做题目

        }else if(rank.code == '03'){ // 做错题目
            questions = QuestionRecord.createCriteria().list {
                createAlias("question", "q")
                eq("isRight", false)
                eq("q.qbank", qbank)
                eq("baseUser", user)
                order("q.id", "asc")
                projections {
                    distinct "question"
                }
            }
        }else if(rank.code == '02'){ // 收藏题目
            questions = QuestionStar.createCriteria().list {
                createAlias("question", "q")
                eq("q.qbank", qbank)
                eq("baseUser", user)
                order("q.id", "asc")
            }?.question
        }else{ // 全部题目
            questions = Question.findAll(sort: "id"){
                qbank == qbank
            }
        }
        if(model?.code == '02'){ // 随机模式
            Collections.shuffle(questions);
        }
        if(model?.code == '03'){ // 背题模式
            map.isReciteModel = true
        }
        questions.each{ Question q->
            def one = [:]
            one.id = q?.id
            one.content = q?.content
            one.type = q?.type?.code
            one.answer = q?.answer
            one.analysis = q?.analysis
            one.hasPic = q?.hasPic
            if(model?.code == '03'){ // 背题
                one.isAnswer = true
            }else{
                one.isAnswer = false
            }
            if(rank?.code == '02'){ // 收藏题目
                one.isStar = true
            }else{
                one.isStar = QuestionStar.countByBaseUserAndQuestion(user, q) > 0
            }
            def options = []
            QOptions.findAllByQuestion(q).each { QOptions qo ->
                def option = [:]
                option.id = qo?.id
                option.oNo = qo?.oNo
                option.content = qo?.content
                option.isDisabled = false
                if(model?.code == '03'){ // 背题
                    option.isDisabled = true
                    if(q.answer.contains(qo?.oNo)){
                        option.isCheck = true
                    }else{
                        option.isCheck = false
                    }
                }else{
                    option.isDisabled = false
                    option.isCheck = false
                }
                options << option
            }
            one.options = options
            results << one
        }
        map.count = questions?.size()
        map.rows = results
        render map as JSON
    }

    /**
     * 记录问题操作
     * @return
     */
    def doQuestionRecord() {
        def map = [:]
        def user = BaseUser.read(params?.userId)
        def question = Question.read(params?.questionId)
        def record = new QuestionRecord(question: question, baseUser: user,
                isRight: Boolean.parseBoolean(params?.isRight), choices: params?.choices)
        if(record.save(flush:true)){
            map.result = true
            map.message = "保存成功"
        }else{
            map.result = false
            map.message = "保存失败"
            record.errors.allErrors
        }
        render map as JSON
    }

    /**
     * 收藏/取消问题操作
     * @return
     */
    def doQuestionStar() {
        def map = [:]
        def user = BaseUser.read(params?.userId)
        def question = Question.read(params?.questionId)
        if(params?.state == 'star'){
            if(QuestionStar.countByBaseUserAndQuestion(user, question) < 1){
                def record = new QuestionStar(question: question, baseUser: user)
                if(record.save(flush:true)){
                    map.result = true
                    map.message = "收藏成功"
                }else{
                    map.result = false
                    map.message = "收藏失败"
                }
            }else{
                map.result = true
                map.message = "已经收藏"
            }
        } else if(params?.state == 'unstar'){
            QuestionStar.where {
                baseUser == user
                question == question
            }.deleteAll()
            map.result = true
            map.message = "取消成功"
        } else {
            map.result = false
            map.message = "参数不正确"
        }
        render map as JSON
    }

    /**
     * 导入题库
     * @return
     */
    @Transactional
    def doQbankSave() {
        def map = [:]
        def user = BaseUser.read(params?.userId)
        def file = request.getFile('excelFile')
        println file
        println file.size
        println params?.qbankName
        if(!params?.qbankName) {
            map.result = false
            map.message = "题库名称不能为空"
        } else if(Qbank.countByCreaterAndName(user, params?.qbankName) > 0) {
            map.result = false
            map.message = "您已创建过同名题库"
        } else if(!file) {
            map.result = false
            map.message = "题库文件不能为空"
        } else if (file.size > 5 * 1024 * 1024) {
            map.result = false
            map.message = '文件大于5M'
        } else {
            def errorList = [], excelList = []
            new ExcelReadBuilder(2007, file.bytes).eachLine([sheet: '题目内容', labels: false]) {
                def msgList = []
                if(!cell(0)){
                    msgList << "第1列：题目内容为空"
                }
                if(!cell(1)){
                    msgList << "第2列：题目答案为空"
                }
                if(!cell(3)){
                    msgList << "第4列：至少填写一个选项"
                }
                if(msgList.isEmpty()){
                    def excelMap = [:]
                    excelMap.questionContent = cell(0)
                    excelMap.questionAnswer = cell(1)
                    excelMap.questionAnalysis = cell(2)
                    excelMap.questionOptions1 = cell(3)
                    excelMap.questionOptions2 = cell(4)
                    excelMap.questionOptions3 = cell(5)
                    excelMap.questionOptions4 = cell(6)
                    excelMap.questionOptions5 = cell(7)
                    excelMap.questionOptions6 = cell(8)
                    excelMap.questionOptions7 = cell(9)
                    excelList << excelMap
                } else {
                    def error = "第${it.rowNum + 1}行：${msgList.join('、')}"
                    errorList << error
                }
            }
            if(errorList.isEmpty()){
                // 保存题库
                def qbank = new Qbank(name: params?.qbankName, description: params?.qbankDesc,
                        isOpen: params?.qbankOpen, creater: user)
                if(qbank.save(flush: true)){
                    def singleType = Dictionary.findByTypeAndCode("WTLX", "01");
                    def multType = Dictionary.findByTypeAndCode("WTLX", "02");
                    // 遍历保存题目
                    excelList.each { data ->
                        def types = data?.questionAnswer?.split(',')
                        def question = new Question(qbank: qbank, content: data?.questionContent,
                                type: types.size() == 1?singleType:multType, answer: data?.questionAnswer,
                                analysis: data.questionAnalysis)
                        question.save(flush: true)
                        // 保存题目选项
                        def finish = false;
                        ['A','B','C','D','E','F','G'].eachWithIndex { oNo, index ->
                            if(data."questionOptions${index+1}" && !finish){
                                def qoption = new QOptions(question: question, oNo: oNo, content: data."questionOptions${index+1}")
                                qoption.save(flush: true)
                                println qoption.errors.allErrors
                            }else{
                                finish = true
                            }
                        }
                    }
                    map.result = true
                    map.message = "导入成功"
                }else{
                    map.result = false
                    map.message = "操作失败"
                    println qbank.errors.allErrors
                }
            } else {
                map.result = false
                map.message = errorList.join("。")
            }
        }
        render map as JSON
    }
}
