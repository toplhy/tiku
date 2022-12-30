package tikud

import com.lhy.tikud.core.BaseRole
import com.lhy.tikud.core.BaseUser
import com.lhy.tikud.core.BaseUserBaseRole
import com.lhy.tikud.core.Requestmap
import com.lhy.tikud.dics.Dictionary

class BootStrap {

    def springSecurityService

    def init = { servletContext ->
        createDefaultRoles();
        createDefaultUsers();
        createRequestMap();
        createDictionary();
    }
    def destroy = {
    }

    private void createDefaultRoles() {
        if (!BaseRole.findByAuthority('ROLE_ADMIN')){
            new BaseRole(authority:'ROLE_ADMIN', name: 'ROLE_ADMIN', description: '管理员').save(flush: true)
        }
        if(!BaseRole.findByAuthority('ROLE_USER')){
            new BaseRole(authority:'ROLE_USER', name: 'ROLE_USER', description: '用户').save(flush: true)
        }
    }

    private void createDefaultUsers() {
        if (!BaseUser.findByUsername('admin')) {
            def admin = new BaseUser(username: 'admin', password: '123456')
            admin.save(flush: true)
            BaseUserBaseRole.create(admin, BaseRole.findByAuthority('ROLE_ADMIN'), true);
        }
    }

    private void createRequestMap() {
        if (Requestmap.count() == 0) {
            for (String url in [
                    '/error', '/index', '/index.gsp', '/**/favicon.ico', '/shutdown',
                    '/assets/**', '/**/js/**', '/**/css/**', '/**/images/**',
                    '/login', '/login.*', '/login/*',
                    '/logout', '/logout.*', '/logout/*']) {
                new Requestmap(url: url, configAttribute: 'permitAll').save()
            }

            //controller
            new Requestmap(url: '/api/**', configAttribute: "permitAll").save(flush: true)
            new Requestmap(url: '/baseUser/**', configAttribute: "isFullyAuthenticated()").save(flush: true)
            new Requestmap(url: '/baseRole/**', configAttribute: 'isFullyAuthenticated()').save(flush: true)
            new Requestmap(url: '/requestmap/**', configAttribute: 'isFullyAuthenticated()').save(flush: true)
            new Requestmap(url: '/workspace/**', configAttribute: 'isFullyAuthenticated()').save(flush: true)
            new Requestmap(url: '/**', configAttribute: 'isFullyAuthenticated()').save(flush: true)
            springSecurityService.clearCachedRequestmaps()
        }
    }

    private createDictionary() {
        if(Dictionary.count() == 0) {
            new Dictionary(type: 'LXMS', name: '顺序模式', code: '01', sortNum: 1, remark: '练习模式').save(flush: true)
            new Dictionary(type: 'LXMS', name: '随机模式', code: '02', sortNum: 2, remark: '练习模式').save(flush: true)
            new Dictionary(type: 'LXMS', name: '背题模式', code: '03', sortNum: 3, remark: '练习模式').save(flush: true)
            new Dictionary(type: 'TKFW', name: '全部题目', code: '01', sortNum: 1, remark: '题库范围').save(flush: true)
            new Dictionary(type: 'TKFW', name: '收藏题目', code: '02', sortNum: 2, remark: '题库范围').save(flush: true)
            new Dictionary(type: 'TKFW', name: '做错题目', code: '03', sortNum: 3, remark: '题库范围').save(flush: true)
            new Dictionary(type: 'TKFW', name: '未做题目', code: '04', sortNum: 4, remark: '题库范围').save(flush: true)
            new Dictionary(type: 'WTLX', name: '单选题', code: '01', sortNum: 1, remark: '问题类型').save(flush: true)
            new Dictionary(type: 'WTLX', name: '多选题', code: '02', sortNum: 2, remark: '问题类型').save(flush: true)
        }
    }
}
