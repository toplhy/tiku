package com.lhy.tikud.tiku

import com.lhy.tikud.annotation.Title
import com.lhy.tikud.core.BaseUser

@Title(zh_CN = "练习记录")
class QuestionRecord {

    @Title(zh_CN = "题目")
    Question question
    @Title(zh_CN = "用户")
    BaseUser baseUser
    @Title(zh_CN = "做题时间")
    Date dateCreated
    @Title(zh_CN = "对错")
    Boolean isRight
    @Title(zh_CN = "选择")
    String choices

    static constraints = {
        question nullable: false, blank: false
        baseUser nullable: false, blank: false
        isRight nullable: false, blank: false
        choices nullable: false, blank: false
    }

    static mapping = {
        comment "练习记录"
        id comment: '主键ID'
        version comment: '版本'
        question comment: '题目'
        baseUser comment: '用户'
        dateCreated comment: '做题时间'
        isRight comment: '对错'
        choices comment: '选择'
    }
}
