package com.lhy.tikud.tiku

import com.lhy.tikud.annotation.Title
import com.lhy.tikud.core.BaseUser

@Title(zh_CN = "问题收藏")
class QuestionStar {

    @Title(zh_CN = "题目")
    Question question
    @Title(zh_CN = "用户")
    BaseUser baseUser
    @Title(zh_CN = "收藏时间")
    Date dateCreated

    static constraints = {
        question nullable: false, blank: false
        baseUser nullable: false, blank: false
    }

    static mapping = {
        comment "问题收藏"
        id comment: '主键ID'
        version comment: '版本'
        question comment: '题目'
        baseUser comment: '用户'
        dateCreated comment: '收藏时间'
    }
}
