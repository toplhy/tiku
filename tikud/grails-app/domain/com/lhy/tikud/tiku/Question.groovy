package com.lhy.tikud.tiku

import com.lhy.tikud.annotation.Title
import com.lhy.tikud.dics.Dictionary

@Title(zh_CN = "问题")
class Question {

    @Title(zh_CN = "所属题库")
    Qbank qbank
    @Title(zh_CN = "问题内容")
    String content
    @Title(zh_CN = "问题类型")
    Dictionary type
    @Title(zh_CN = "问题答案")
    String answer
    @Title(zh_CN = "问题解析")
    String analysis
    @Title(zh_CN = "包含图片")
    Boolean hasPic = false
    @Title(zh_CN = "图片内容")
    byte[] picContent

    static constraints = {
        qbank nullable: false, blank: false
        content nullable: false, blank: false, size: 0..500
        type nullable: false, blank: false
        answer nullable: false, blank: false
        analysis nullable: true, blank: true, size: 0..1000
        hasPic nullable: false, blank: false
        picContent nullable: true, blank: true, size: 0..1024*1024*2
    }

    static mapping = {
        comment "问题表"
        id comment: '主键ID'
        version comment: '版本'
        qbank comment: '所属题库'
        content comment: '问题内容'
        type comment: '问题类型'
        answer comment: '问题答案'
        analysis comment: '问题解析'
        hasPic comment: '包含图片'
        picContent comment: '图片内容'
    }
}
