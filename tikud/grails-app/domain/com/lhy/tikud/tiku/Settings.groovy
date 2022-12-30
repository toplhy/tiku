package com.lhy.tikud.tiku

import com.lhy.tikud.annotation.Title
import com.lhy.tikud.core.BaseUser
import com.lhy.tikud.dics.Dictionary

@Title(zh_CN = "刷题设置")
class Settings {

    @Title(zh_CN = "设置人")
    BaseUser settingUser
    @Title(zh_CN = "当前题库")
    Qbank qbank
    @Title(zh_CN = "题库范围")
    Dictionary rank
    @Title(zh_CN = "练习模式")
    Dictionary model

    static constraints = {
        settingUser nullable: false, blank: false
        qbank nullable: false, blank: false
        rank nullable: false, blank: false
        model nullable: false, blank: false
    }

    static mapping = {
        comment '刷题设置表'
        id comment:'主键ID'
        version comment:'版本'
        settingUser comment:'设置人'
        qbank comment:'当前题库'
        rank comment:'题库范围'
        model comment:'练习模式'
    }
}
