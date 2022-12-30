package com.lhy.tikud.core

import com.lhy.tikud.annotation.Title
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@EqualsAndHashCode(includes='authority')
@ToString(includes='authority', includeNames=true, includePackage=false)
class BaseRole implements Serializable {

	private static final long serialVersionUID = 1

	@Title(zh_CN = "角色名称")
	String name
	@Title(zh_CN = "角色权限")
	String authority
	@Title(zh_CN = "描述")
	String description

	static constraints = {
		authority nullable: false, blank: false, unique: true
	}

	static mapping = {
		cache true
		comment "角色"
		name comment:"角色名称"
		authority comment:"角色权限"
		description comment:"描述"
	}
}
