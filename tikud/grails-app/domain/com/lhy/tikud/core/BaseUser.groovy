package com.lhy.tikud.core

import com.lhy.tikud.annotation.Title
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class BaseUser implements Serializable {

    private static final long serialVersionUID = 1

    @Title(zh_CN = "用户名")
    String username
    @Title(zh_CN = "用户密码")
    String password
    @Title(zh_CN = "是否禁用")
    boolean enabled = true
    @Title(zh_CN = "账号过期")
    boolean accountExpired
    @Title(zh_CN = "账号锁定")
    boolean accountLocked
    @Title(zh_CN = "密码过期")
    boolean passwordExpired
    @Title(zh_CN = "手机号码")
    String mobile
    @Title(zh_CN = "电子邮箱")
    String email
    @Title(zh_CN = "头像文件")
    byte[] avatar

    Set<BaseRole> getAuthorities() {
        (BaseUserBaseRole.findAllByBaseUser(this) as List<BaseUserBaseRole>)*.baseRole as Set<BaseRole>
    }

    static constraints = {
        password nullable: false, blank: false, password: true
        username nullable: false, blank: false, unique: true
        mobile nullable: true, blank: true
        email nullable: true, blank: true
        avatar nullable: true, size: 0..1024*1024*2
    }

    static mapping = {
	    password column: '`password`', comment: '用户密码'
        comment "用户"
        username comment: '用户名'
        enabled comment: '是否禁用'
        accountExpired comment: '账号过期'
        accountLocked comment: '账号锁定'
        passwordExpired comment: '密码过期'
        mobile comment: '手机号码'
        email comment: '电子邮箱'
        avatar comment: '头像文件'
    }
}
