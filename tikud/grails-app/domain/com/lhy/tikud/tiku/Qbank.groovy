package com.lhy.tikud.tiku

import com.lhy.tikud.annotation.Title
import com.lhy.tikud.core.BaseUser

class Qbank {

    @Title(zh_CN = "题库名称")
    String name
    @Title(zh_CN = "题库描述")
    String description
    @Title(zh_CN = "是否公开")
    Boolean isOpen = true
    @Title(zh_CN = "创建人")
    BaseUser creater
    @Title(zh_CN = "创建时间")
    Date dateCreated
    @Title(zh_CN = "更新时间")
    Date lastUpdated

    static constraints = {
        name nullable: false, blank: false
        description nullable: true, blank: true, size: 0..500
        creater nullable: false, blank: false
    }

    static mapping = {
        comment "题库"
        name comment: '题库名称'
        description comment: '题库描述'
        isOpen comment: '是否公开'
        creater comment: '创建人'
        dateCreated comment: '创建时间'
        lastUpdated comment: '更新时间'
    }

}
