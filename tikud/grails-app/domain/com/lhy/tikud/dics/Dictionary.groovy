package com.lhy.tikud.dics

import com.lhy.tikud.annotation.Title


@Title(zh_CN = "字典管理表")
class Dictionary {
    @Title(zh_CN = "字典类型")
    String type
    @Title(zh_CN = "字典名称")
    String name
    @Title(zh_CN = "字典编码")
    String code
    @Title(zh_CN = "排序字段")
    Integer sortNum
    @Title(zh_CN = "是否删除")
    Boolean deleted = false
    @Title(zh_CN = "备注")
    String remark

    @Title(zh_CN = "创建时间")
    Date dateCreated;
    @Title(zh_CN = "更新时间")
    Date lastUpdated;

    static constraints = {
        type nullable: false, blank: false
        code nullable: false, blank: false
        name nullable: false, blank: false
        sortNum nullable: false, blank: false
        deleted nullable: false, blank: false
        remark nullable: true, maxSize: 200

    }

    static mapping = {
        comment '字典表'
        id comment:'主键ID'
        version comment:'版本'
        type comment:'字典类型'
        name comment:'字典名称'
        code comment:'字典编码'
        sortNum comment:'排序字段'
        deleted comment:'是否删除'
        remark comment: '备注'
        dateCreated comment:'创建时间'
        lastUpdated comment:'更新时间'
    }


}
