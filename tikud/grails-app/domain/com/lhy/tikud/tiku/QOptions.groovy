package com.lhy.tikud.tiku

import com.lhy.tikud.annotation.Title

@Title(zh_CN = "问题选项")
class QOptions {

    @Title(zh_CN = "问题")
    Question question
    @Title(zh_CN = "选项编号")
    String oNo
    @Title(zh_CN = "选项内容")
    String content

    static constraints = {
        question nullable: false, blank: false
        oNo nullable: false, blank: false, size: 0..10
        content nullable: false, blank: false, size: 0..500
    }

    static mapping = {
        comment "问题选项表"
        id comment: '主键ID'
        version comment: '版本'
        question comment: '问题'
        oNo comment: '选项编号'
        content comment: '选项内容'
    }
}
