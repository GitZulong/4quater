package com.boot.web.sys.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.boot.util.BaseController;
import com.boot.util.CommonEntity;
import com.boot.util.Result;
import com.krm.common.constant.Constant;
import com.boot.util.IPUtils;
import com.boot.util.StringUtil;
import com.boot.util.TreeUtils;
import com.boot.web.sys.model.SysMenu;
import com.boot.web.sys.model.SysUser;
import com.boot.web.sys.security.CaptchaException;
import com.boot.web.sys.security.UsernamePasswordToken;
import com.boot.web.sys.service.SysMenuCategoryService;
import com.boot.web.sys.service.SysMenuService;
import com.boot.web.sys.service.SysUserService;
import com.boot.util.SysUserUtils;

/**
 * @author Administrator
 */
@Controller
public class LoginController extends BaseController {

    @Resource
    private SysMenuService sysMenuService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysMenuCategoryService sysMenuCategoryService;


    /**
     * ????????????
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "index")
    @RequiresPermissions("index")
    public String toIndex(Model model, HttpServletRequest request) {
        // ?????????????????????????????????
        if (SysUserUtils.getSessionLoginUser() == null) {
            return "redirect:/login";
        }
        logger.info("===========================================");
        logger.info("|=============????????????????????????=============|");
        logger.info("===========================================");
//		Map<String, Object> params = Maps.newHashMap();
//		params.put(Constant.FIELD_DEL_FLAG, Constant.DEL_FLAG_NORMAL);
//		List<CommonEntity> list = sysMenuCategoryService.commonList(params);
//		model.addAttribute("menuList", SysUserUtils.getUserMenus());
//		String category = request.getParameter("category");
//		List<CommonEntity> menus = Lists.newArrayList();
        Map<String, Object> params = Maps.newHashMap();
        params.put(Constant.FIELD_DEL_FLAG, Constant.DEL_FLAG_NORMAL);
        List<CommonEntity> categories = sysMenuCategoryService.commonList(params);
//		for (CommonEntity entity : categories) {
//			if(entity.getString("id").equals(category)){
//				menus.addAll(TreeUtils.toTreeNodeListWithRoot(sysMenuService.getAllMenusList(),SysMenu.class, entity.getString("id")));
//			}
//		}
        model.addAttribute("menuCategories", categories);
//		model.addAttribute("menuList", menus);
//		model.addAttribute("menuSize", menus.size());
        return "index";
    }

    @RequestMapping(value = "dashboard")
    public String dashboard(Model model, HttpServletRequest request) {
        return "first";
    }

    /**
     * ?????????????????????
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String toLogin(Model model, HttpServletRequest request, HttpServletResponse response) {
        logger.info("?????????????????????");
        Subject currentUser = SecurityUtils.getSubject();
        String userName = request.getUserPrincipal().toString();
        try {
            if (!currentUser.isAuthenticated()) {
                SysUser user = new SysUser();
                user.setUsername(userName);
                user = sysUserService.selectOne(user);
                UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword().toCharArray(), true, null, null);
                currentUser.login(token);
                currentUser.getSession().setAttribute(Constant.SESSION_LOGIN_USER, user);
                SysUserUtils.setUserAuth();
                return "redirect:/index";
            } else if (currentUser.isAuthenticated()) {
                SecurityUtils.getSubject().checkPermission("index");
            }

            if (SysUserUtils.getSessionLoginUser() != null && SysUserUtils.getCacheLoginUser() != null) {
                return "redirect:/index";
            } else {
                return "login";
            }
            
    		/*logger.info("===========================================");
    		logger.info("|=============????????????????????????=============|");
    		logger.info("===========================================");
    		Map<String, Object> params = Maps.newHashMap();
    		params.put(Constant.FIELD_DEL_FLAG, Constant.DEL_FLAG_NORMAL);
    		List<CommonEntity> categories = sysMenuCategoryService.commonList(params);
    		model.addAttribute("menuCategories", categories);*/


        } catch (Exception e) {
            //          logger.info(username + "???????????????1?????????", ex);
            e.printStackTrace();
        }

        return "login";
    }

    /**
     * ????????????
     *
     * @param username
     * @param password
     * @param code
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public @ResponseBody
    Result checkLogin(String username,
                      String password, String captcha, HttpServletRequest request) {
        logger.info("******************????????????????????????******************");
        String msg = "";
        username = StringUtils.trim(username);
        password = StringUtils.trim(password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray(), true, null, captcha);
        token.setRememberMe(true);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            logger.info("******************??????????????????********************");
            SysUser user = sysUserService.checkUser(token.getUsername(), String.valueOf(token.getPassword()));
            SysUserUtils.getSession().setAttribute(Constant.SESSION_LOGIN_USER, user);
            //????????????
//			SysUserUtils.cacheLoginUser(user);
            //???????????????????????????
            SysUserUtils.setUserAuth();
            //????????????????????????ip???date
            SysUser newUser = new SysUser();
            newUser.setLoginDate(new Date());
            newUser.setLoginIp(subject.getSession().getHost());
            newUser.setId(user.getId());
            sysUserService.updateByPrimaryKeySelective(newUser);
            msg = "???????????????";
        } catch (IncorrectCredentialsException e) {
            msg = "?????????????????????";
            logger.info(msg);
            return new Result(0, msg);
        } catch (CaptchaException e) {
            msg = "???????????????!";
            logger.info(msg);
            return new Result(0, msg);
        } catch (ExcessiveAttemptsException e) {
            msg = "???????????????????????????";
            logger.info(msg);
            return new Result(0, msg);
        } catch (LockedAccountException e) {
            msg = "?????????????????????";
            logger.info(msg);
            return new Result(0, msg);
        } catch (DisabledAccountException e) {
            msg = "??????????????????!";
            logger.info(msg);
            return new Result(0, msg);
        } catch (ExpiredCredentialsException e) {
            msg = "??????????????????";
            logger.info(msg);
            return new Result(0, msg);
        } catch (UnknownAccountException e) {
            msg = "???????????????!";
            logger.info(msg);
            return new Result(0, msg);
        } catch (UnauthorizedException e) {
            msg = "?????????????????????????????????";
            logger.info(msg);
            return new Result(0, msg);
        } catch (AccountException e) {
            msg = "????????????????????????????????????????????????????????????????????????????????????";
            logger.info("?????????" + username + "?????????????????????????????????");
            return new Result(0, msg);
        }
        return new Result(1, msg);
    }


    /**
     * ????????????
     *
     * @param username
     * @param password
     * @param code
     * @return
     */
    @RequestMapping(value = "loginfromplat", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> checkPlatLogin(String username, HttpServletRequest request) {

        Map<String, Object> msg = new HashMap<String, Object>();
        HttpSession session = request.getSession();
        String password = "js2358";
        username = StringUtils.trim(username);
        password = StringUtils.trim(password);
        SysUser user = sysUserService.checkUser(username, password);
        if (null != user) {

            session.setAttribute(Constant.SESSION_LOGIN_USER, user);

            //????????????
//			SysUserUtils.cacheLoginUser(user);

            //???????????????????????????
            SysUserUtils.setUserAuth();

            //????????????????????????ip???date
            SysUser newUser = new SysUser();
            newUser.setLoginDate(new Date());
            newUser.setLoginIp(IPUtils.getClientAddress(request));
            newUser.setId(user.getId());
            sysUserService.updateByPrimaryKeySelective(newUser);
        } else {
            msg.put("error", "????????????????????????");
        }
        return msg;
    }

    /**
     * ????????????
     *
     * @return ?????????????????????
     */
    @RequestMapping("logout")
    public String logout(HttpServletRequest request) {
        SysUserUtils.clearCacheUser(SysUserUtils.getSessionLoginUser().getId());
        request.getSession().invalidate();
        return "redirect:/login";
    }

    /**
     * ??????????????????
     *
     * @return ?????????????????????
     */
    @RequestMapping(value = "forceLogout", method = RequestMethod.POST)
    public String forceLogout(HttpServletRequest request) {
        SysUserUtils.getSession().setAttribute("msg", request.getParameter("msg"));
        return "redirect:/login";
    }

    @RequestMapping("notauth")
    public String notAuth() {
        return "notauth";
    }

    @RequestMapping("notlogin")
    public String notLogin() {
        return "notlogin";
    }

    @Override
    protected String getBaseUrl() {
        return null;
    }

    @Override
    protected String getBasePath() {
        return null;
    }

}
