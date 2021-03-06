package com.boot.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import com.boot.util.CommonEntity;
import com.boot.util.beetl.utils.BeetlUtils;
import com.krm.common.constant.Constant;
import com.boot.util.SpringContextHolder;
import com.boot.util.CacheUtils;
import com.boot.util.TreeUtils;
import com.boot.web.sys.model.SysDict;
import com.boot.web.sys.model.SysMenu;
import com.boot.web.sys.model.SysOrgan;
import com.boot.web.sys.model.SysRole;
import com.boot.web.sys.model.SysUser;
import com.boot.web.sys.security.UsernamePasswordToken;
import com.boot.web.sys.service.SysDictService;
import com.boot.web.sys.service.SysMenuCategoryService;
import com.boot.web.sys.service.SysMenuService;
import com.boot.web.sys.service.SysOrganService;
import com.boot.web.sys.service.SysRoleService;
import com.boot.web.sys.service.SysUserService;

/**
 * @author Parker
 */
public class SysUserUtils {

    private static final Logger logger = LoggerFactory.getLogger(SysUserUtils.class);

    static SysMenuService sysMenuService = SpringContextHolder.getBean("sysMenuService");
    static SysUserService sysUserService = SpringContextHolder.getBean("sysUserService");
    static SysRoleService sysRoleService = SpringContextHolder.getBean("sysRoleService");
    static SysOrganService sysOrganService = SpringContextHolder.getBean("sysOrganService");
    static SysDictService sysDictService = SpringContextHolder.getBean("sysDictService");
    static SysMenuCategoryService sysMenuCategoryService = SpringContextHolder.getBean("sysMenuCategoryService");
    static DefaultWebSessionManager sessionManager = SpringContextHolder.getBean("shiroSessionManager");

    /**
     * ?????????????????????
     */
    public static void setUserAuth() {
        //?????????
        getUserMenus();
        //????????????
        getUserRoles();
        //??????????????????
        getUserOrgan();
        //???????????????????????????
        getUserDataScope();
    }

    /**
     * ???????????????????????????
     *
     * @return
     */
    public static Map<String, SysMenu> getUserResources() {
        SysUser sysUser = getCacheLoginUser();
        Map<String, SysMenu> userMenus = CacheUtils.get(
                Constant.CACHE_SYS_MENU, Constant.CACHE_USER_MENU
                        + sysUser.getId());
        if (userMenus == null) {
            if (sysUser.isAdmin()) {
                userMenus = BeetlUtils.getBeetlSharedVars(Constant.CACHE_ALL_MENU);
            } else {
                List<SysMenu> userRes = sysMenuService.findUserMenuByUserId(sysUser);
                userMenus = new LinkedHashMap<String, SysMenu>();
                for (SysMenu res : userRes) {
                    if (StringUtils.isBlank(res.getUrl())) {
                        userMenus.put(res.getId().toString(), res);
                    } else {
                        userMenus.put(res.getUrl(), res);
                    }
                }
            }
            CacheUtils.put(Constant.CACHE_SYS_MENU,
                    Constant.CACHE_USER_MENU + sysUser.getId(),
                    userMenus);
        }
        return userMenus;
    }

    /**
     * ??????????????????
     */
    public static List<CommonEntity> getUserMenus() {
        SysUser sysUser = getCacheLoginUser();
        List<CommonEntity> userMenus = null;
//		List<CommonEntity> userMenus = CacheUtils.get(
//				Constant.CACHE_SYS_MENU,
//				Constant.CACHE_USER_MENU_TREE + sysUser.getId());
        if (userMenus == null || userMenus.size() == 0) {
            Map<String, SysMenu> userResources = getUserResources();
            List<SysMenu> menus = Lists.newArrayList();
//			List<CommonEntity> list = sysMenuCategoryService.commonList(params);
            for (SysMenu res : userResources.values()) {
                if (Constant.MENU_TYPE.equals(res.getType())) {
                    menus.add(res);
                }
            }
            userMenus = TreeUtils.toTreeNodeList(menus, SysMenu.class);
            CacheUtils.put(Constant.CACHE_SYS_MENU,
                    Constant.CACHE_USER_MENU_TREE + sysUser.getId(), userMenus);
        }
        return userMenus;
    }

    /**
     * ?????????????????????
     */
    public static List<SysRole> getUserRoles() {
        SysUser sysUser = getCacheLoginUser();
        List<SysRole> userRoles = CacheUtils.get(
                Constant.CACHE_SYS_ROLE,
                Constant.CACHE_USER_ROLE + sysUser.getId());
        if (userRoles == null) {
            if (sysUser.isAdmin()) {
                userRoles = sysRoleService.select(new SysRole());
            } else {
                userRoles = sysRoleService.findUserRoleListByUserId(sysUser.getId());
            }
            CacheUtils.put(Constant.CACHE_SYS_ROLE,
                    Constant.CACHE_USER_ROLE + sysUser.getId(), userRoles);
        }
        return userRoles;
    }


    /**
     * ?????????????????????map??????
     */
    public static Map<String, SysRole> getUserRolesMap() {
        List<SysRole> list = SysUserUtils.getUserRoles();
        Map<String, SysRole> userRolesMap = Maps.uniqueIndex(list, new Function<SysRole, String>() {
            @Override
            public String apply(SysRole sysRole) {
                return sysRole.getId();
            }
        });
        return userRolesMap;
    }


    /**
     * ?????????????????????
     *
     * @return
     */
    public static List<SysOrgan> getUserOrgan() {
        SysUser sysUser = getCacheLoginUser();
        List<SysOrgan> userOrgans = CacheUtils.get(
                Constant.CACHE_SYS_ORGAN,
                Constant.CACHE_USER_ORGAN + sysUser.getId());
        if (userOrgans == null) {
            SysOrgan organ = new SysOrgan();
            if (sysUser.isAdmin()) {
                userOrgans = sysOrganService.select(organ);
            } else {
                organ.setUserDataScope(SysUserUtils.dataScopeFilterString(null, null));
                userOrgans = sysOrganService.findEntityListByDataScope(organ);
            }
            CacheUtils.put(Constant.CACHE_SYS_ORGAN,
                    Constant.CACHE_USER_ORGAN + sysUser.getId(), userOrgans);
        }
        return userOrgans;
    }


    /**
     * ????????????
     *
     * @return
     */
    public static List<SysDict> getDictList() {
        List<SysDict> sysDicts = CacheUtils.get(Constant.CACHE_SYS_DICT, Constant.CACHE_SYS_DICT);
        if (sysDicts == null) {
            SysDict dict = new SysDict();
            sysDicts = sysDictService.select(dict);
            CacheUtils.put(Constant.CACHE_SYS_DICT,
                    Constant.CACHE_SYS_DICT, sysDicts);
        }
        return sysDicts;
    }

    /**
     * ???????????????????????????,????????????????????????
     */
    public static List<String> getUserDataScope() {
        SysUser sysUser = getCacheLoginUser();
        List<String> dataScope = CacheUtils.get(Constant.CACHE_SYS_ORGAN,
                Constant.CACHE_USER_DATASCOPE + sysUser.getId());
        if (dataScope == null) {
            dataScope = Lists.newArrayList();
            if (!sysUser.isAdmin()) {
                List<Integer> dc = Lists.transform(getUserRoles(), new Function<SysRole, Integer>() {
                    @Override
                    public Integer apply(SysRole sysRole) {
                        return Integer.parseInt(sysRole.getDataScope());
                    }
                });
                int[] dataScopes = Ints.toArray(dc);
                if (dataScopes.length == 0) return dataScope;
                int min = Ints.min(dataScopes);
                for (int i = min, len = Integer.parseInt(Constant.DATA_SCOPE_CUSTOM); i <= len; i++) {
                    dataScope.add(i + "");
                }
            } else {
                dataScope = Constant.DATA_SCOPE_ADMIN;
            }
            CacheUtils.put(Constant.CACHE_SYS_ORGAN,
                    Constant.CACHE_USER_DATASCOPE + sysUser.getId(), dataScope);
        }
        return dataScope;
    }

    //???????????????????????????????????????,????????????????????????????????????
    public static String autoAddOrganToRole() {
        List<String> userScope = getUserDataScope();
        int count = 0;
        for (String s : userScope) {
            if (StringUtils.equals(s, Constant.DATA_SCOPE_CUSTOM)) {
                count++;
            }
        }
        return count == userScope.size() ? getUserRoles().get(0).getId() : null;
    }

    /**
     * ??????????????????
     *
     * @param user       ??????????????????
     * @param organAlias ???????????????
     * @param userAlias  ?????????????????????????????????????????????
     * @param field      field[0] ?????????id???????????? ???????????????????????????
     * @return (so.organ id = ... or .. or)
     */
    public static String dataScopeFilterString(String organAlias, String userAlias, String... field) {
        SysUser sysUser = getCacheLoginUser();
        if (StringUtils.isBlank(organAlias)) organAlias = "sys_organ";
        //?????????????????????
        List<SysRole> userRoles = getUserRoles();
        //??????sql??????
        StringBuilder tempSql = new StringBuilder();
        //???????????????sql
        String dataScopeSql = "";
        if (!sysUser.isAdmin()) {
            for (SysRole sr : userRoles) {
                if (StringUtils.isNotBlank(organAlias)) {
                    boolean isDataScopeAll = false;
                    if (Constant.DATA_SCOPE_ALL.equals(sr.getDataScope())) {
                        isDataScopeAll = true;
                    } else if (Constant.DATA_SCOPE_ORGAN_AND_CHILD.equals(sr.getDataScope())) {
                        //so.code=1 or so.parentIds like '0,1,%'
                        tempSql.append(" or " + organAlias + ".code = '" + sysUser.getOrganId() + "'");
                        SysOrgan sysOrgan = sysOrganService.getOrganByCode(sysUser.getOrganId());
                        tempSql.append(" or " + organAlias + ".parent_ids like '" + sysOrgan.getParentIds() + sysOrgan.getCode() + ",%'");
                    } else if (Constant.DATA_SCOPE_ORGAN.equals(sr.getDataScope())) {
                        //or so.code=1 or (so.parent_id=1 and so.type=2)
                        tempSql.append(" or " + organAlias + ".code = '" + sysUser.getOrganId() + "'");
                        tempSql.append(" or (" + organAlias + ".parent_id = '" + sysUser.getOrganId() + "'");
                        tempSql.append(" and " + organAlias + ".type=2)");
                    } else if (Constant.DATA_SCOPE_DEPT_AND_CHILD.equals(sr.getDataScope())) {
                        //or so.code=5 or so.parentIds like '0,1,5,%'
                        tempSql.append(" or " + organAlias + ".code = " + sysUser.getDeptId() + "'");
                        SysOrgan sysOrgan = sysOrganService.getOrganByCode(sysUser.getDeptId());
                        tempSql.append(" or " + organAlias + ".parent_ids like '" + sysOrgan.getParentIds() + sysOrgan.getCode() + ",%'");
                    } else if (Constant.DATA_SCOPE_DEPT.equals(sr.getDataScope())) {
                        //or so.code=5
                        tempSql.append(" or " + organAlias + ".code = '" + sysUser.getDeptId() + "'");
                    } else if (Constant.DATA_SCOPE_CUSTOM.equals(sr.getDataScope())) {
                        //or so.code in (1,2,3,4,5)
                        List<String> organs = sysOrganService.findUserDataScopeByUserId(sysUser.getId());
                        if (organs.size() == 0) organs.add("-1");
                        tempSql.append(" or " + organAlias + ".code in (" + StringUtils.join(organs, ",") + ")");
                    }
                    if (!isDataScopeAll) {
                        if (StringUtils.isNotBlank(userAlias)) {
                            // or su.id=22
                            if (field == null || field.length == 0) field[0] = "id";
                            tempSql.append(" or " + userAlias + "." + field[0] + " = '" + sysUser.getId() + "'");
                        } else {
                            tempSql.append(" or " + organAlias + ".code is null");
                        }
                    } else {
                        // ????????????????????????????????????????????????????????????????????????????????????
                        tempSql.delete(0, tempSql.length());
                        break;
                    }
                }
            }// for end

            if (StringUtils.isNotBlank(tempSql)) {
                dataScopeSql = "(" + tempSql.substring(tempSql.indexOf("or") + 2, tempSql.length()) + ")";
            }
        }
        return dataScopeSql;
    }

    /**
     * ??????????????????
     *
     * @param user       ??????????????????
     * @param organAlias ???????????????
     * @param userAlias  ?????????????????????????????????????????????
     * @param field      field[0] ?????????id???????????? ???????????????????????????
     * @return (so.organ code = ... or .. or)
     */
    public static String dataScopeFilterString1(String organAlias, String userAlias, String menuUrl, String... field) {
        SysUser sysUser = getCacheLoginUser();
        Map<String, SysMenu> map = getUserResources();
        //???????????????????????????
        SysMenu resource = map.get(menuUrl);
//		if(StringUtils.isBlank(organAlias)) organAlias = "sys_organ";
        //?????????????????????
        List<SysRole> userRoles = getUserRoles();
        //??????sql??????
        StringBuilder tempSql = new StringBuilder();
        //???????????????sql
        String dataScopeSql = "";
        if (!sysUser.isAdmin()) {
            for (SysRole sr : userRoles) {
                //????????????????????????id
                List<String> resourceIds = sysRoleService.findMenuIdsByRoleId(sr.getId());
                if (resourceIds.contains(resource.getId())) {
                    boolean isDataScopeAll = false;
                    //??????????????????
                    if (Constant.DATA_SCOPE_ALL.equals(sr.getDataScope())) {
                        isDataScopeAll = true;
                    }
                    if (StringUtils.isNotBlank(organAlias)) {
                        //???????????????????????????
                        if (Constant.DATA_SCOPE_ORGAN_AND_CHILD.equals(sr.getDataScope())) {
                            //so.code=1 or so.parentIds like '0,1,%'
                            tempSql.append(" or " + organAlias + ".code = '" + sysUser.getOrganId() + "'");
                            SysOrgan sysOrgan = sysOrganService.getOrganByCode(sysUser.getOrganId());
                            tempSql.append(" or " + organAlias + ".parent_ids like '" + sysOrgan.getParentIds() + sysOrgan.getCode() + ",%'");
                        }
                        //????????????
                        else if (Constant.DATA_SCOPE_ORGAN.equals(sr.getDataScope())) {
                            //or so.code=1 or (so.parent_id=1 and so.type=2)
                            tempSql.append(" or " + organAlias + ".code = '" + sysUser.getOrganId() + "'");
                            tempSql.append(" or (" + organAlias + ".parent_id = '" + sysUser.getOrganId() + "'");
                            tempSql.append(" and " + organAlias + ".type=2)");
                        }
                        //?????????????????????
                        else if (Constant.DATA_SCOPE_DEPT_AND_CHILD.equals(sr.getDataScope())) {
                            //or so.code=5 or so.parentIds like '0,1,5,%'
                            tempSql.append(" or " + organAlias + ".code = '" + sysUser.getDeptId() + "'");
                            SysOrgan sysOrgan = sysOrganService.getOrganByCode(sysUser.getDeptId());
                            tempSql.append(" or " + organAlias + ".parent_ids like '" + sysOrgan.getParentIds() + sysOrgan.getCode() + ",%'");
                        }
                        //????????????
                        else if (Constant.DATA_SCOPE_DEPT.equals(sr.getDataScope())) {
                            //or so.code=5
                            tempSql.append(" or " + organAlias + ".code = '" + sysUser.getDeptId() + "'");
                        }
                        //??????
                        else if (Constant.DATA_SCOPE_CUSTOM.equals(sr.getDataScope())) {
                            //or so.code in (1,2,3,4,5)
                            List<String> organs = sysOrganService.findUserDataScopeByUserId(sysUser.getId());
                            if (organs.size() == 0) organs.add("-1");
                            tempSql.append(" or " + organAlias + ".code in (" + StringUtils.join(organs, ",") + ")");
                            if (StringUtils.isNotBlank(userAlias)) {
                                // or su.code=22
                                if (field == null || field.length == 0) field[0] = "id";
                                tempSql.append(" or " + userAlias + "." + field[0] + " = '" + sysUser.getId() + "'");
                            }
                        }
//						if (!isDataScopeAll){
//						}else{
//							// ????????????????????????????????????????????????????????????????????????????????????
//							tempSql.delete(0, tempSql.length());
//							break;
//						}
                    }
                    //??????????????????????????????????????????????????????????????????
                    if (StringUtils.isNotBlank(userAlias)) {
                        if (Constant.DATA_SCOPE_SELF.equals(sr.getDataScope())) {
                            if (field == null || field.length == 0) field[0] = "id";
                            tempSql.append(" " + userAlias + "." + field[0] + " = '" + sysUser.getId() + "'");
                        }
                    }
                    if (StringUtils.isNotBlank(tempSql)) {
                        dataScopeSql = "(" + tempSql.substring(tempSql.indexOf("or") + 2, tempSql.length()) + ")";
                    }
                }// for end
            }
        }
        return dataScopeSql;
    }

    /**
     * ???????????????????????????
     */
    public static void clearAllCachedAuthorizationInfo(List<String> userIds) {
        if (CollectionUtils.isNotEmpty(userIds)) {
            for (String userId : userIds) {
                boolean evictRes = CacheUtils.remove(Constant.CACHE_SYS_MENU,
                        Constant.CACHE_USER_MENU + userId);

                boolean evictMenu = CacheUtils.remove(Constant.CACHE_SYS_MENU,
                        Constant.CACHE_USER_MENU_TREE + userId);

                boolean evictRole = CacheUtils.remove(Constant.CACHE_SYS_ROLE,
                        Constant.CACHE_USER_ROLE + userId);

                boolean evictOrgan = CacheUtils.remove(Constant.CACHE_SYS_ORGAN,
                        Constant.CACHE_USER_ORGAN + userId);

                boolean evictScope = CacheUtils.remove(Constant.CACHE_SYS_ORGAN,
                        Constant.CACHE_USER_DATASCOPE + userId);

                if (evictRes && evictMenu && evictRole && evictOrgan && evictScope) {
                    logger.debug("??????" + userId + "?????????????????????????????????????????????????????????????????????");
                }
            }
        }
    }

    public static void clearCacheMenu() {
        CacheUtils.clear(Constant.CACHE_SYS_MENU);
    }

    /**
     * ????????????????????????
     */
    public static void clearCacheUser(String userId) {
        CacheUtils.evict(Constant.CACHE_SYS_USER, userId.toString());
    }

    /**
     * ??????????????????
     */
    public static void clearCacheOrgan(List<String> userIds) {
        for (String userId : userIds) {
            CacheUtils.evict(Constant.CACHE_SYS_ORGAN,
                    Constant.CACHE_USER_ORGAN + userId);
        }
    }

    /**
     * ??????????????????,???????????????????????????20??????,???session?????????????????????
     */
//	public static void cacheLoginUser(SysUser sysUser){
//		CacheUtils.put(Constant.CACHE_SYS_USER, sysUser.getId().toString(), 
//				sysUser,new Long(getSession().getTimeout()).intValue());
//	}

    /**
     * ??????????????????????????????
     */
    public static SysUser getCacheLoginUser() {
		try {
			if (getSessionLoginUser() != null) {
				return CacheUtils.get(Constant.CACHE_SYS_USER,
						getSessionLoginUser().getId().toString());
			}
		} catch (Exception e) {
		}
        return getSessionLoginUser();
    }

    /**
     * ????????????session
     */
    public static Session getSession() {
        Session session = SecurityUtils.getSubject().getSession();
        return session;
    }

    /**
     * ????????????session
     */
    public static HttpSession getHttpSession() {
        HttpSession session = getCurRequest().getSession();
        return session;
    }

    /**
     * session????????????
     */
    public static SysUser getSessionLoginUser() {
        return (SysUser) getSession().getAttribute(Constant.SESSION_LOGIN_USER);
    }

    /**
     * @Title: getCurRequest
     * @Description:(???????????????request)
     * @param:@return
     * @return:HttpServletRequest
     */
    public static HttpServletRequest getCurRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            return servletRequestAttributes.getRequest();
        }
        return null;
    }


    public static boolean setUserSession(String username, HttpServletRequest request) {

        SysUser sysuser = (SysUser) request.getSession().getAttribute(Constant.SESSION_LOGIN_USER);
        if (null == sysuser) {
            sysuser = new SysUser();
            sysuser.setUsername(username);
            List<SysUser> listuser = sysUserService.select(sysuser);
            if (listuser.size() > 0) {
                sysuser = listuser.get(0);
                UsernamePasswordToken token = new UsernamePasswordToken(username, sysuser.getPassword().toCharArray(), true, null, "");
                token.setRememberMe(true);
                Subject subject = SecurityUtils.getSubject();
//	           Session session=subject.getSession();
//	         long out_time=   System.currentTimeMillis()  -  subject.getSession().getLastAccessTime().getTime();
//	           if(out_time>=session.getTimeout()){
//	        	   ThreadContext.remove(ThreadContext.SUBJECT_KEY);//?????????????????????subject
//	                sessionDAO.delete(subject.getSession());//???????????????session 
//	                subject = new Subject.Builder().buildSubject(); 
//	           }
                Session session = subject.getSession(false);
                if (session != null) {
                    System.out.println("?????????????????????session??????=====" + session.getId());
                    SessionDAO sessiond = sessionManager.getSessionDAO();
                    sessiond.delete(session);

                }
                //   subject.login(token);
                request.getSession().setAttribute(Constant.SESSION_LOGIN_USER, sysuser);
                //setUserAuth();
            } else {
                return false;
            }
        }

        return true;

    }


}
