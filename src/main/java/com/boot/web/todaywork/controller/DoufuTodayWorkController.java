package com.boot.web.todaywork.controller;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.boot.util.BaseController;
import com.boot.util.CommonEntity;
import com.boot.util.Result;
import com.boot.util.DateUtils;
import com.boot.util.StringConvert;
import com.boot.util.StringUtil;
import com.boot.util.excel.ExportExcel;
import com.boot.util.excel.ImportExcel;
import com.boot.util.qq.weixin.mp.aes.DayReportUtil;
import com.boot.util.qq.weixin.mp.aes.WeiXinUtil;
import com.boot.web.todaywork.model.DoufuTodayWork;
import com.boot.web.todaywork.service.DoufuTodayWorkService;
import com.boot.util.SysUserUtils;
import com.krm.common.constant.Constant;
import com.boot.web.sys.model.SysUser;
import java.net.URLDecoder;
import com.boot.util.JsonHelper;
import java.util.ArrayList;
import java.util.UUID;
import com.boot.util.HttpUtil;
import java.io.UnsupportedEncodingException;

/**
 * 
 * @author ????????? ???????????????????????????????????? 2019-09-07
 */
@Controller
@RequestMapping("todaywork/doufuTodayWork")
public class DoufuTodayWorkController extends BaseController {

	public static final String BASE_URL = "todaywork/doufuTodayWork";
	private static final String BASE_PATH = "todaywork/";

	@Resource
	private DoufuTodayWorkService doufuTodayWorkService;

	@Override
	protected String getBaseUrl() {
		return BASE_URL;
	}

	@Override
	protected String getBasePath() {
		return BASE_PATH;
	}

	@Override
	protected String getBasePermission() {
		return "todaywork:doufuTodayWork";
	}

	/**
	 * ?????????????????????
	 * 
	 * @param model
	 * @return ??????html
	 */
	@RequestMapping("mainpage")
	public String toDoufuTodayWork(Model model) {
		logger.info("??????????????????????????????????????????(" + getBasePath() + "doufuTodayWork-list)");
		// checkPermission("query");
		return getBasePath() + "doufuTodayWork-list";
	}

	/**
	 * ????????????
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.POST)
	@ResponseBody
	public PageInfo<CommonEntity> list(@RequestParam Map<String, Object> params, Model model) {
		logger.info("???????????????????????????????????????????????????" + params.toString());
		// checkPermission("query");
		// ????????????
		params.put("dynamicSQL", SysUserUtils.dataScopeFilterString1("o", "u", getBaseUrl(), "id"));
		if (params.containsKey("sortC")) {
			// ??????????????????????????????????????????????????????????????????????????????
			params.put("sortC", StringConvert.camelhumpToUnderline(params.get("sortC").toString()));
		}
		PageInfo<CommonEntity> page = doufuTodayWorkService.queryPageInfo(params);
		return page;
	}

	/**
	 * ???????????????
	 * 
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "wxworksave", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	@Transactional
	public Result wxWorkSave(@RequestBody JSONObject jsonParam, HttpServletRequest request)
			throws UnsupportedEncodingException {
		Result result = new Result();
		logger.info("???????????????????????????????????????");
		int count = 1;
		if (jsonParam.isEmpty()) {
			result.setMsg("????????????????????????????????????");
			return result.errorResult();
		}

		DayReportUtil dayReportUtil = new DayReportUtil();

		String data = jsonParam.toJSONString();
		logger.info("???????????????data??????" + data + "???");

		String strSaveMsg = json2String(data);

		if (!"".equals(strSaveMsg)) {
			// dealDayReportInsert(HttpServletRequest request, String strMsgContent, String
			// strFromUser)
			JSONObject jsonUserInfo = jsonParam.getJSONObject("userIfno");
			String strUserCode = jsonUserInfo.getString("userCode");

			WeiXinUtil weiXinUtil = new WeiXinUtil();

			logger.info("??????????????????????????????????????????" + json2String(data) + "???");
			// String strUserID = weiXinUtil.getTencentUserInfo(strUserCode);
			logger.info("??????code???????????????????????????" + strUserCode + "???");
			if ("".equals(strUserCode)) {
				result.setMsg("?????????????????????code??????");
				return result.errorResult();
			}

			weiXinUtil.userInit(request, strUserCode);
			String strRtnMsg = dayReportUtil.dealDayReportInsert(request, strSaveMsg, strUserCode);
			result.setMsg(strRtnMsg);
			return result;
		}

		/*
		 * JSONObject jsonSubmit = jsonParam.getJSONObject("submit"); JSONObject
		 * jsonToday = jsonParam.getJSONObject("today"); JSONObject jsonTomorrow =
		 * jsonParam.getJSONObject("tomorrow");
		 * 
		 * Set<String> keySet =jsonToday.keySet();
		 * 
		 * 
		 * for (String key : keySet) { logger.info("???????????????jsonToday????????????"+key+"???"); }
		 * 
		 * 
		 * if (!"".equals(data)) {
		 * logger.info("???????????????jsonToday??????"+jsonToday.toJSONString()+"???");
		 * logger.info("???????????????jsonTomorrow??????"+jsonTomorrow.toJSONString()+"???");
		 * logger.info("???????????????jsonSubmit??????"+jsonSubmit.toJSONString()+"???");
		 * 
		 * }
		 */
		if (count > 0) {
			logger.info("??????????????????????????????????????????");
			return Result.successResult();
		}
		return Result.errorResult();
	}

	public Set<String> getProjectCounts(JSONObject obj) {
		Set<String> result = new HashSet<String>();
		Iterator<String> iter = obj.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			int lastIndex = key.lastIndexOf("_");
			if (lastIndex == -1) {
				continue;
			}
			result.add(key.substring(lastIndex + 1));
		}
		return result;
	}

	/**
	 * json????????????
	 * 
	 * @param json
	 * @return
	 */
	public String json2String(String json) {
		StringBuffer result = new StringBuffer();
		JSONObject jsonObject = JSONObject.parseObject(json);
		result.append("[??????]\r\n");

		// today
		JSONObject todayObj = jsonObject.getJSONObject("today");
		String todayWorktype = todayObj.getString("worktype");// ???????????????????????????...
		result.append("??????\r\n");
		Set<String> projectNums = getProjectCounts(todayObj);
		Iterator<String> iter = projectNums.iterator();
		while (iter.hasNext()) {
			String projectNum = iter.next();
			result.append("[" + todayObj.getString("projectName_" + projectNum) + "]\r\n");// projectName_x
			result.append(todayObj.getString("projectMessage_" + projectNum));// projectMessage_x
			result.append("[" + todayObj.getString("finishPercent_" + projectNum) + "%]\r\n");// finishPercent_x
		}

		// today
		JSONObject tomorrowObj = jsonObject.getJSONObject("tomorrow");
		String tomorrowWorktype = tomorrowObj.getString("worktype");// ???????????????????????????...
		result.append("??????\r\n");
		projectNums = getProjectCounts(tomorrowObj);
		iter = projectNums.iterator();
		while (iter.hasNext()) {
			String projectNum = iter.next();
			result.append("[" + tomorrowObj.getString("tomorrow_projectName_" + projectNum) + "]\r\n");// tomorrow_projectName_x
			result.append(tomorrowObj.getString("tomorrow_projectMessage_" + projectNum) + "\r\n");// tomorrow_projectMessage_x
		}

		// submit
		JSONObject submitObj = jsonObject.getJSONObject("submit");
		String submitObjWorktype = submitObj.getString("worktype");// ???????????????????????????...
		result.append("??????\r\n");
		result.append(submitObj.getString("projectSummary") + "\r\n");// projectSummary

		return result.toString();
	}

	/**
	 * ???????????????
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ResponseBody
	public Result save(@ModelAttribute DoufuTodayWork entry, MultipartHttpServletRequest request) {
		logger.info("???????????????????????????????????????");
		int count = 0;
		if (StringUtil.isEmpty(entry.getId())) {
			checkPermission("add");
			entry.setId(doufuTodayWorkService.generateId());
			count = doufuTodayWorkService.save(entry);
		} else {
			checkPermission("update");
			count = doufuTodayWorkService.update(entry);
		}
		if (count > 0) {
			logger.info("??????????????????????????????????????????");
			return Result.successResult();
		}
		return Result.errorResult();
	}

	/**
	 * ??????
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Result del(String id, @RequestParam Map<String, Object> params) {
		logger.info("???????????????????????????????????????????????????" + id);
		checkPermission("delete");
		int count = doufuTodayWorkService.deleteDoufuTodayWork(id);
		if (count > 0) {
			logger.info("??????????????????????????????????????????");
			return Result.successResult();
		}
		logger.info("??????????????????????????????????????????");
		return Result.errorResult();
	}

	/**
	 * ????????????
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "deletes", method = RequestMethod.POST)
	@ResponseBody
	public Result dels(@RequestParam(value = "ids[]") String[] ids) {
		logger.info("?????????????????????????????????????????????????????????" + ids);
		// checkPermission("delete");
		if (ids.length == 0) {
			return new Result(0, "????????????????????????????????????????????????");
		}

		int count = doufuTodayWorkService.deleteDoufuTodayWork(ids);
		if (count > 0) {
			logger.info("??????????????????????????????????????????");
			return Result.successResult();
		}
		logger.info("??????????????????????????????????????????");
		return Result.errorResult();
	}

	/**
	 * ????????????
	 * 
	 * @param params {"mode":"1.add 2.edit 3.detail}
	 * @return
	 */
	@RequestMapping(value = "{mode}/showlayer", method = RequestMethod.POST)
	public String layer(String id, @RequestParam Map<String, Object> params, @PathVariable String mode, Model model) {
		DoufuTodayWork entry = null;
		if (StringUtils.equals("add", mode)) {
			logger.info("?????????????????????????????????????????????????????????(" + getBasePath() + "doufuTodayWork-add)");
			checkPermission("add");
			return getBasePath() + "doufuTodayWork-add";
		} else if (StringUtils.equals("edit", mode)) {
			logger.info("?????????????????????????????????????????????????????????(" + getBasePath() + "doufuTodayWork-update)");
			checkPermission("update");
			params.put("id", id);
			entry = doufuTodayWorkService.queryOne(params);
			model.addAttribute("entry", entry);
			return getBasePath() + "doufuTodayWork-update";
		} else if (StringUtils.equals("detail", mode)) {
			logger.info("?????????????????????????????????????????????????????????(" + getBasePath() + "doufuTodayWork-detail)");
			checkPermission("query");
			params.put("id", id);
			CommonEntity entity = doufuTodayWorkService.queryOneCommon(params);
			model.addAttribute("entry", entity);
		} else if (StringUtils.equals("import", mode)) {
			logger.info("?????????????????????????????????????????????Excel????????????(" + getBasePath() + "doufuTodayWork-import)");
			checkPermission("import");
			return getBasePath() + "doufuTodayWork-import";
		}
		return getBasePath() + "doufuTodayWork-detail";
	}

	/**
	 * ???????????????????????????Excel????????????
	 * 
	 * @param response
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("import/template/download")
	public void importDoufuTodayWorkTemplate(HttpServletResponse response) throws Exception {
		logger.info("???????????????????????????????????????Excel????????????");
		checkPermission("import");
		String fileName = "???????????????????????????Excel????????????.xlsx";
		List<DoufuTodayWork> list = Lists.newArrayList();
		list.add(new DoufuTodayWork());
		new ExportExcel("???????????????????????????", DoufuTodayWork.class, 2).setDataList(list).write(response, fileName).dispose();
	}

	/**
	 * ???????????????????????????????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "import", method = RequestMethod.POST)
	@ResponseBody
	public Result importFile(@RequestParam("file") MultipartFile fileList[], HttpServletResponse response,
			HttpServletRequest request) throws Exception {
		logger.info("?????????????????????????????????????????????");
		checkPermission("import");
		Long start = System.currentTimeMillis();
		int successNum = 0;
		int failureNum = 0;
		SysUser sysuser = (SysUser) request.getSession().getAttribute(Constant.SESSION_LOGIN_USER);
		for (MultipartFile file : fileList) {
			ImportExcel ei;
			StringBuilder failureMsg = new StringBuilder();
			ei = new ImportExcel(file, 1, 0);
			List<DoufuTodayWork> list = ei.getDataList(DoufuTodayWork.class);
			for (DoufuTodayWork entry : list) {
				entry.setId(doufuTodayWorkService.generateId());
				// entry.setCreateBy(SysUserUtils.getCacheLoginUser().getId());
				entry.setCreateBy(sysuser.getId());
				entry.setCreateDate(new Date());
				entry.setDelFlag(Constant.DEL_FLAG_NORMAL);
			}
			successNum = doufuTodayWorkService.insertBatch(list);
			if (failureNum > 0) {
				failureMsg.insert(0, "??????????????? " + failureNum + " ????????????????????????????????????????????????????????????");
			}
			Long end = System.currentTimeMillis();
			DecimalFormat df = new DecimalFormat("######0.00");
			logger.info("????????????" + df.format((double) (end - start) / (double) 1000) + "???");
		}
		return new Result(1, "??????????????????????????????" + successNum + "??????????????????" + failureNum + "???");
	}

	/**
	 * ?????????????????????????????????excel
	 */
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public void export(@RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
		logger.info("?????????????????????????????????????????????");
		checkPermission("export");
		String fileName = "???????????????????????????" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		// ????????????
		params.put("dynamicSQL", SysUserUtils.dataScopeFilterString1("o", "u", getBaseUrl(), "id"));
		try {
			for (String key : params.keySet()) { // ??????????????????
				String paramsTrans = new String(((String) params.get(key)).getBytes("ISO-8859-1"), "UTF-8");
				paramsTrans = java.net.URLDecoder.decode(paramsTrans, "UTF-8");
				params.put(key, paramsTrans.trim());
			}
		} catch (Exception e) {
		}
		List<DoufuTodayWork> list = doufuTodayWorkService.entityList(params);
		new ExportExcel("???????????????????????????", DoufuTodayWork.class).setDataList(list).write(response, fileName).dispose();
	}

	/**
	 * ????????????????????????????????????
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "queryPageList", method = RequestMethod.POST)
	@ResponseBody
	PageInfo<DoufuTodayWork> queryPageList(HttpServletRequest request) throws Exception {

		HttpUtil httpUtil = new HttpUtil();
		int pageIndex = Integer.parseInt(request.getParameter("pageIndex")) + 1;
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		String sortField = request.getParameter("sortField").toString();
		String sortOrder = request.getParameter("sortOrder").toString();
		String reportDate = request.getParameter("reportDate").toString();
		Map<String, Object> params = httpUtil.getParameterMap(request);
		SysUser sysuser = (SysUser) request.getSession().getAttribute(Constant.SESSION_LOGIN_USER);
		params.put("pageNum", pageIndex);
		params.put("pageSize", pageSize);
		params.put("sortC", StringConvert.camelhumpToUnderline(sortField));
		params.put("order", sortOrder);
		params.put("reportDate", reportDate);
		params.put("reporterName", sysuser.getUsername());
		logger.info("???????????????????????????????????????????????????" + params.toString());
		PageInfo<DoufuTodayWork> page = doufuTodayWorkService.queryPageInfoEntity(params);
		return page;

	}

	/**
	 * ????????????????????????
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "queryList", method = RequestMethod.POST)
	@ResponseBody
	List<DoufuTodayWork> queryList(HttpServletRequest request) throws Exception {
		SysUser sysuser = (SysUser) request.getSession().getAttribute(Constant.SESSION_LOGIN_USER);
		HttpUtil httpUtil = new HttpUtil();
		Map<String, Object> params = httpUtil.getParameterMap(request);
	
		String reportDate = request.getParameter("reportDate").toString();
		String sortField = request.getParameter("sortField").toString();
		String sortOrder = request.getParameter("sortOrder").toString();
		params.put("sortC", StringConvert.camelhumpToUnderline(sortField));
		params.put("order", sortOrder);
		params.put("reportDate", reportDate);
		params.put("reporterName", sysuser.getUsername());

		List<DoufuTodayWork> lstRtn = doufuTodayWorkService.entityList(params);
		return lstRtn;

	}

	/**
	 * ???????????????
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "saveBatch", method = RequestMethod.POST)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Result saveBatch(HttpServletRequest request) throws Exception {
		String data = request.getParameter("data") == null ? "" : request.getParameter("data");
		String report_date = request.getParameter("reportDate") == null ? "" : request.getParameter("reportDate");
		if (data == "[]") {
			return new Result(0, "??????????????????????????????????????????????????????");
		}
		if (report_date == "") {
			return new Result(0, "????????????????????????????????????????????????");
		}
		data = URLDecoder.decode(URLDecoder.decode(data, "utf-8"), "utf-8");
		StringBuilder failureMsg = new StringBuilder();
		logger.info("?????????????????????????????????????????????");
		// checkPermission("savebatch");
		Long start = System.currentTimeMillis();
		int successNumInsert = 0;
		int failureNumInsert = 0;
		int successNumUpdate = 0;
		int failureNumUpdate = 0;
		SysUser sysuser = (SysUser) request.getSession().getAttribute(Constant.SESSION_LOGIN_USER);
		if (!"".equals(data)) {
			List<Map<String, Object>> list = (List<Map<String, Object>>) JsonHelper.decode(data);
			List<DoufuTodayWork> listEntyInsert = new ArrayList<DoufuTodayWork>();
			List<DoufuTodayWork> listEntyUpdate = new ArrayList<DoufuTodayWork>();
			for (int i = list.size() - 1; i >= 0; i--) {
				Map<String, Object> row = list.get(i);
				String vid = row.get("id") != null ? row.get("id").toString() : "";
				String state = row.get("_state") != null ? row.get("_state").toString() : "";
				DoufuTodayWork entryDoufuTodayWork = new DoufuTodayWork(row);
				if (vid == "" || vid == null) {
					String uuid = UUID.randomUUID().toString();
					entryDoufuTodayWork.setId(uuid);
					entryDoufuTodayWork.setProjectGroupId(sysuser.getProjectGroupId());
					entryDoufuTodayWork.setProjectId(sysuser.getProjectGroupId());
					entryDoufuTodayWork.setCreateDate(new Date());
					entryDoufuTodayWork.setUpdateDate(new Date());
					entryDoufuTodayWork.setDelFlag(Constant.DEL_FLAG_NORMAL);
					entryDoufuTodayWork.setStatus(Constant.DEL_FLAG_NORMAL);
					entryDoufuTodayWork.setInstId(sysuser.getOrganId());
					entryDoufuTodayWork.setLoginIp(sysuser.getLoginIp());
					entryDoufuTodayWork.setLoginDate(new Date());
					entryDoufuTodayWork.setCreateBy(sysuser.getId());
					entryDoufuTodayWork.setUpdateBy(sysuser.getId());
					entryDoufuTodayWork.setReportDate(report_date);
					entryDoufuTodayWork.setReporterName(sysuser.getUsername());
					entryDoufuTodayWork.setIsAfter("0");
					entryDoufuTodayWork.setReporterId(sysuser.getId());
					listEntyInsert.add(entryDoufuTodayWork);
				} else {
					entryDoufuTodayWork.setUpdateDate(new Date());
					entryDoufuTodayWork.setUpdateBy(sysuser.getId());
					entryDoufuTodayWork.setLoginIp(sysuser.getLoginIp());
					listEntyUpdate.add(entryDoufuTodayWork);
				}
			}
			if (listEntyInsert != null && listEntyInsert.size() > 0) {
				successNumInsert = doufuTodayWorkService.insertBatch(listEntyInsert);
			}
			if (listEntyUpdate != null && listEntyUpdate.size() > 0) {
				successNumUpdate = doufuTodayWorkService.updateBatch(listEntyUpdate);
			}

			if (failureNumInsert > 0) {
				failureMsg.insert(0, "???????????????,??????????????? " + failureNumInsert + " ???????????????????????????????????????");
			}
			if (failureNumUpdate > 0) {
				failureMsg.insert(0, "???????????????,??????????????? " + failureNumUpdate + " ???????????????????????????????????????");
			}
			Long end = System.currentTimeMillis();
			DecimalFormat df = new DecimalFormat("######0.00");
			logger.info("?????????????????????" + df.format((double) (end - start) / (double) 1000) + "???");
		}
		int failureNum = 0;
		int successNum = 0;
		failureNum = failureNumInsert + failureNumUpdate;
		successNum = successNumInsert + successNumUpdate;
		return new Result(1, "??????????????????????????????" + successNum + "??????????????????" + failureNum + "???");
	}

	/**
	 * ????????????????????????
	 * 
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */

	@RequestMapping(value = "wxWorkQuery", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	@Transactional
	public String wxWorkQuery(@RequestBody JSONObject jsonParam, HttpServletRequest request)
			throws UnsupportedEncodingException, ParseException {

		JSONObject jsonValue = new JSONObject();
		String returnMsg = "";
		if (jsonParam.isEmpty()) {
			returnMsg = "????????????????????????????????????";
			jsonValue.put("code", 0);
			jsonValue.put("msg", returnMsg);
			jsonValue.put("data", "{}");
			return jsonValue.toString();
		}

		DayReportUtil dayReportUtil = new DayReportUtil();

		String data = jsonParam.toJSONString();
		logger.info("???????????????data??????" + data + "???");

		String strUserCode = jsonParam.getString("userCode");
		String queyrStartDate = jsonParam.getString("startdate");
		String queyrEndDate = jsonParam.getString("enddate");
		String queyrName = jsonParam.getString("username");

		if (null == strUserCode || "".equals(strUserCode)) {
			returnMsg = "???????????????????????????";

			jsonValue.put("code", 0);
			jsonValue.put("msg", returnMsg);
			jsonValue.put("count", 0);
			jsonValue.put("data", "{}");
			return jsonValue.toString();
		}

		if ("".equals(queyrStartDate) || null == queyrStartDate) {
			queyrStartDate = DateUtils.DateToStr8(new Date());
		}

		if ("".equals(queyrEndDate) || null == queyrEndDate) {
			queyrEndDate = DateUtils.DateToStr8(new Date());
		}

		WeiXinUtil weiXinUtil = new WeiXinUtil();
		weiXinUtil.userInit(request, strUserCode);

		if (!"".equals(data)) {
			Map<String, Object> queryMap = new HashMap();
			queryMap.put("query_start_date", queyrStartDate);
			queryMap.put("query_end_date", queyrEndDate);
			queryMap.put("query_name", queyrName);
			queryMap.put("pageNum", jsonParam.getString("page"));
			queryMap.put("pageSize", jsonParam.getString("limit"));
			returnMsg = "??????????????????";
			PageInfo<CommonEntity> pageQuery = queryWebMsgList(request, queryMap);
			jsonValue.put("code", 0);
			jsonValue.put("msg", returnMsg);
			jsonValue.put("count", pageQuery.getTotal());
			jsonValue.put("data", JSON.toJSON(pageQuery.getList()));
			return jsonValue.toString();

		}
		return jsonValue.toString();
	}
	
	
	public PageInfo<CommonEntity>  queryWebMsgList(HttpServletRequest request, Map<String, Object> map)
			throws ParseException {
		String queryResult = null;
		String dynamicSQL = "";

		SysUser sysuser = (SysUser) request.getSession().getAttribute(Constant.SESSION_LOGIN_USER);

		
		
		Map<String, Object> queryMap = new HashMap<String, Object>();
		if ("".equals(map.get("query_start_date")) || null == map.get("query_start_date")) {
			return null;
		} 
		if ("".equals(map.get("query_end_date")) || null == map.get("query_end_date")) {
			return null;
		} 

		if ("".equals(map.get("query_end_date")) || null == map.get("query_end_date")) {
			dynamicSQL = "t.REPORT_DATE ='" + map.get("query_start_date") + "'";
		} else {
			if (DateUtils.daysBetween(map.get("query_start_date").toString(), map.get("query_end_date").toString()) > 5)
				logger.info("???????????????????????????????????????????????????????????????????????????");
			dynamicSQL = "t.REPORT_DATE >='" + map.get("query_start_date") + "' and t.REPORT_DATE <='"
					+map.get("query_end_date") + "'";
		}
		String queryUser = "";

		if ("".equals(map.get("query_name")) || null == map.get("query_name")) {
			queryUser = sysuser.getUsername();
		} else {
			queryUser = map.get("query_name").toString();
			WeiXinUtil weiXinUtil= new WeiXinUtil();
			SysUser sysUserQuery = weiXinUtil.getUserInfo(queryUser);
			queryUser = sysUserQuery.getUsername();
		}

		logger.info("???????????????????????????"+queryUser+"???");
		queryMap.put("reporterName", queryUser);
		queryMap.put("pageNum",  map.get("pageNum"));
		queryMap.put("pageSize",  map.get("pageSize"));
		queryMap.put("dynamicSQL", dynamicSQL);
		queryMap.put("sortC", "create_date,input_order,PRODUCT_NAME");
		queryMap.put("order", "asc");
		PageInfo<CommonEntity> page = doufuTodayWorkService.queryPageInfo(queryMap);
		
		return page;

	}

	/**
	 * ????????????????????????
	 * 
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	@RequestMapping(value = "wxgetRptDList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	@Transactional
	public String wxgetRptDList(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
		
		JSONObject jsonValue = new JSONObject();
		JSONObject jsonValueRtn = new JSONObject();
		String returnMsg = "";
		if (jsonParam.isEmpty()) {
			returnMsg = "????????????????????????";
			jsonValue.put("reporterId", 0);
			jsonValue.put("date", "??????");
			jsonValueRtn.put("lists", jsonValue.toString());
			return jsonValueRtn.toString();
		}
		
		String strUserCode = jsonParam.getString("userCode");

		
		if (null == strUserCode || "".equals(strUserCode)) {
			returnMsg = "???????????????????????????";
			jsonValue.put("reporterId", 0);
			jsonValue.put("date", "??????");
			jsonValueRtn.put("lists", jsonValue.toString());
			return jsonValueRtn.toString();
		}
		WeiXinUtil weiXinUtil = new WeiXinUtil();
		weiXinUtil.userInit(request, strUserCode);		
		
		Map<String, Object> queryMap = new HashMap();
		queryMap.put("reporterName", strUserCode);
		List<CommonEntity>  lstRptDList = doufuTodayWorkService.getReportDateList(queryMap);
		
		if(lstRptDList==null||lstRptDList.size()==0) {
			returnMsg = "????????????????????????";
			jsonValue.put("reporterId", 0);
			jsonValue.put("date", "??????");
			jsonValueRtn.put("lists", jsonValue.toString());
			return jsonValueRtn.toString();	
		}else {
			   List 	ListRptDateList = new ArrayList<>();
			jsonValue.put("reporterId", 0);
			jsonValue.put("date", "??????");
			ListRptDateList.add(jsonValue.toString());
			int i=1;
	        for (Iterator<CommonEntity> iterator = lstRptDList.iterator();iterator.hasNext();) {
	        	
				jsonValue.put("reporterId", i++);
				jsonValue.put("date", iterator.next().get("report_date").toString());
				ListRptDateList.add(jsonValue.toString());
	        }
	        jsonValueRtn.put("lists", JSON.toJSON(ListRptDateList));
			
		}
		
		return jsonValueRtn.toString();
	}
	
	/**
	 * ?????????????????????????????????????????????
	 *
	 * 
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	@RequestMapping(value = "wxqueryRptList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	@Transactional
	public String wxqueryRptList(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
		JSONObject jsonValue = new JSONObject();
		JSONObject jsonValueRtn = new JSONObject();
		String returnMsg="";
		List   lstJson = new ArrayList<String>();
		if (jsonParam.isEmpty()) {
			returnMsg = "????????????????????????";
			jsonValue.put("searchId", 0);
			jsonValue.put("productName", "");
			jsonValue.put("workDetail", "");
			jsonValue.put("returnMsg", returnMsg);
			lstJson.add(jsonValue.toString());
	        jsonValueRtn.put("lists", JSON.toJSON(lstJson));
			return jsonValueRtn.toString();
		}
		String strUserCode = jsonParam.getString("userCode");
		String strqueryDate = jsonParam.getString("queryDate");
		
		if (null == strUserCode || "".equals(strUserCode)) {
			returnMsg = "???????????????????????????";
			jsonValue.put("searchId", 0);
			jsonValue.put("productName", "");
			jsonValue.put("workDetail", "");
			jsonValueRtn.put("returnMsg", returnMsg);
			lstJson.add(jsonValue.toString());
	        jsonValueRtn.put("lists", JSON.toJSON(lstJson));
			return jsonValueRtn.toString();
		}
		if (null == strqueryDate || "".equals(strqueryDate)) {
			returnMsg = "??????????????????????????????";
			jsonValue.put("searchId", 0);
			jsonValue.put("productName", "");
			jsonValue.put("workDetail", "");
			jsonValueRtn.put("returnMsg", returnMsg);
			lstJson.add(jsonValue.toString());
	        jsonValueRtn.put("lists", JSON.toJSON(lstJson));
			return jsonValueRtn.toString();
		}
		if ("??????".equals(strqueryDate)){
			strqueryDate = DateUtils.DateToStr8(new Date());
		}
		WeiXinUtil weiXinUtil = new WeiXinUtil();
		weiXinUtil.userInit(request, strUserCode);		
		
		Map<String, Object> queryMap = new HashMap();
		queryMap.put("reporterName", strUserCode);
		queryMap.put("reportDate", strqueryDate);
		
		queryMap.put("sortC", "report_date,input_order");
		List<DoufuTodayWork> lstRtn = doufuTodayWorkService.entityList(queryMap);
		
		if(lstRtn==null||lstRtn.size()==0) {
			returnMsg = "????????????????????????";
			jsonValue.put("searchId", 0);
			jsonValue.put("productName", "");
			jsonValue.put("workDetail", "");
			jsonValueRtn.put("returnMsg", returnMsg);
			lstJson.add(jsonValue.toString());
	        jsonValueRtn.put("lists", JSON.toJSON(lstJson));
			return jsonValueRtn.toString();	
		}else {

			int i=1;

	        for (int ii=0;ii<lstRtn.size();ii++) {
				jsonValue.put("searchId", i++);
				if(null !=lstRtn.get(ii)) {
					DoufuTodayWork doufuTodayWork = lstRtn.get(ii);
				String productName = doufuTodayWork.getProductName();
				String workDetail = doufuTodayWork.getWorkDetail();
				if(null == productName ||"".equals(productName)) {
					productName = "????????????????????????????????????";
				}
				if(null == workDetail ||"".equals(workDetail)) {
					workDetail = "????????????????????????????????????";
				}
				jsonValue.put("productName", productName);
				jsonValue.put("workDetail", workDetail);
				lstJson.add(jsonValue.toString());
	        }
	        }
	        jsonValueRtn.put("lists", JSON.toJSON(lstJson));
	        return jsonValueRtn.toString();
		}
		
	}
	
}