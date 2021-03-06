package com.boot.web.sys.security;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.krm.common.constant.Constant;
import com.boot.web.sys.model.SysUser;
import com.boot.web.sys.service.SysMenuService;
import com.boot.web.sys.service.SysRoleService;
import com.boot.web.sys.service.SysUserService;
import com.boot.util.SysUserUtils;

public class ShiroCaseRealm extends AuthorizingRealm {
    private Logger log = LoggerFactory.getLogger(ShiroCaseRealm.class);
    @Autowired
    private SessionDAO sessionDAO;
    @Resource
    private SysMenuService sysMenuService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysRoleService sysRoleService;
    private String casServerUrlPrefix;
    private String casService;

    public String getCasServerUrlPrefix() {
        return casServerUrlPrefix;
    }

    public void setCasServerUrlPrefix(String casServerUrlPrefix) {
        this.casServerUrlPrefix = casServerUrlPrefix;
    }

    public String getCasService() {
        return casService;
    }

    public void setCasService(String casService) {
        this.casService = casService;
    }


    /**
     * ????????????????????????, ?????????????????????????????????????????????????????????,????????????????????????????????????????????????????????????
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUser sessionUser = SysUserUtils.getSessionLoginUser();
        if (sessionUser != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            if (sessionUser.isAdmin()) {
                info.addStringPermission("*");
            } else {
                info.addStringPermission("index");
                info.addStringPermissions(sysRoleService.getUserPermissions());
            }
            return info;
        }
        return null;
    }

    /**
     * ????????????????????????????????????????????????????????????
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        DefaultWebSessionManager sessionManager = (DefaultWebSessionManager) securityManager.getSessionManager();
        Subject currentUser = SecurityUtils.getSubject();
        //??????1????????????   ????????????if??????  ???????????????????????????????????????  
//        if((System.currentTimeMillis()-currentUser.getSession().getStartTimestamp().getTime())>=currentUser.getSession().getTimeout()-1000){
//            ThreadContext.remove(ThreadContext.SUBJECT_KEY);//?????????????????????subject
//            sessionDAO.delete(currentUser.getSession());//???????????????session
//            currentUser = SecurityUtils.getSubject();//????????????subject
//        }
        // ????????????????????????
//        Collection<Session> sessions = sessionDAO.getActiveSessions();
//        for (Session session : sessions) {
//            if (token.getUsername().equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {
//                session.setTimeout(0);// ??????session????????????????????????????????????
//                break;
//            }
//        }
        //??????????????????
        SysUser user = new SysUser();
        user.setUsername(token.getUsername());
        SysUser currUser = sysUserService.selectOne(user);
        //????????????
        if (currUser != null) {
            //currentUser.getSession().setAttribute(Constant.SESSION_LOGIN_USER, currUser);
            if (currentUser.getSession() != null) {
                log.info("Session?????????????????????[" + currentUser.getSession().getTimeout() + "]??????");
            }
            return new SimpleAuthenticationInfo(currUser.getUsername(), currUser.getPassword(), this.getName());
        }
        return null;

    }


    /**
     * ?????????????????????Hash?????????????????????
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        setCredentialsMatcher(new WebCredentialsMatcher());
    }

    private void setSession(Object key, Object value) {
        Subject currentUser = SecurityUtils.getSubject();
        if (null != currentUser) {
            Session session = currentUser.getSession();
            System.out.println("Session?????????????????????[" + session.getTimeout() + "]??????");
            if (null != session) {
                session.setAttribute(key, value);
            }
        }
    }


}
