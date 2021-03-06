package com.boot.util.qq.weixin.mp.aes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.alibaba.fastjson.JSON;
import com.boot.util.CommonEntity;
import com.boot.util.DateUtils;
import com.boot.util.FileUtils;
import com.boot.util.Result;
import com.boot.util.SpringContextHolder;
import com.boot.util.excel.ExportExcel;
import com.boot.web.sys.model.ReportTmplate;
import com.boot.web.sys.model.SysUser;
import com.boot.web.sys.service.SysUserService;
import com.boot.web.todaywork.model.DoufuTodayWork;
import com.boot.web.todaywork.service.DoufuTodayWorkService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.krm.common.constant.Constant;

import net.sf.json.JSONObject;

import com.boot.util.excel.ExportExcel;
import java.util.TreeMap;

public class DayReportUtil {

	SysUserService sysUserService = SpringContextHolder.getBean("sysUserService");

	DoufuTodayWorkService doufuTodayWorkService = SpringContextHolder.getBean("doufuTodayWorkService");
	// static WeiXinUtil weiXinUtil = new WeiXinUtil();

	Logger logger = LoggerFactory.getLogger(this.getClass());
	private String reportError = "0";
	private String report_date = DateUtils.DateToStr8(new Date());
	private String next_date = DateUtils.getNextDateByDate8(report_date);
	private String query_start_date = "";
	private String query_end_date = "";
	private String by_date = "";
	private int iorder = 0;

	public String getReportError() {
		return reportError;
	}

	/**
	 * 
	 * ?????????????????????????????????
	 * 
	 * @param text
	 * @return
	 */

	public String dealDayReportInsert(HttpServletRequest request, String strMsgContent, String strFromUser) {
		// String strRtnMsgContent = "???,????????????????????????,????????????";
		String strRtnMsgContent = "";

		// ???????????????????????????Map???
		Map<String, Map<String, List<String>>> work = (Map<String, Map<String, List<String>>>) siftWorkContent1(
				strMsgContent);
		if (reportError.equals("0")) {
			// ??????????????????????????????????????????
			ReportTmplate reportTmplate = initTemp(work);
			// ??????????????????????????????????????????
//			weiXinUtil.userInit(request, strFromUser);
			// ???????????????????????????????????????
			Result result = saveMsg(request, reportTmplate);
			if (result.getCode() == 1) {
				strRtnMsgContent = "???,?????????????????????????????????????????????,????????????\n" + result.getMsg();
			}

		} else {
			strRtnMsgContent = WeiXinParamesUtil.dayReportFormat;
		}
		return strRtnMsgContent;
	}

	/**
	 * 
	 * ???????????????????????????
	 * 
	 * @param text
	 * @return
	 */
	public String dealDayReportAdd(HttpServletRequest request, String strMsgContent, String strFromUser) {
		// String strRtnMsgContent = "??????????????????????????????????????????!";
		String strRtnMsgContent = "";
		by_date = "";
		// siftWorkContent2????????????????????????????????????????????????
		Map<String, Map<String, List<String>>> work = (Map<String, Map<String, List<String>>>) siftWorkContent2(
				strMsgContent);
		if (!justMsgDateFormat(strMsgContent)) {
			reportError = "1";
		}
		if (reportError.equals("0")) {

			ReportTmplate reportTmplate = initTemp2(work);
//			weiXinUtil.userInit(request, strFromUser);
			Result result = saveMsg(request, reportTmplate);
			if (result.getCode() == 1) {
				strRtnMsgContent = "???,?????????????????????????????????????????????,????????????\n" + result.getMsg();
			}
		} else {
			strRtnMsgContent = WeiXinParamesUtil.dayReportFormatAdd;
		}
		return strRtnMsgContent;
	}

	/**
	 * 
	 * ???????????????????????????
	 * 
	 * @param text
	 * @return
	 * @throws ParseException
	 */
	public String dealDayReportQuery(HttpServletRequest request, String strMsgContent, String strFromUser)
			throws ParseException {
		String strRtnMsgContent = "?????????????????????????????????????????????????????????????????????";
		query_start_date = "";
		query_end_date = "";
		// siftWorkContent2????????????????????????????????????????????????
		Map<String, Object> queryDate = siftWorkContent3(strMsgContent);

		if (null == queryDate) {
			reportError = "1";
		} else {

			query_start_date = queryDate.get("query_start_date").toString();
			query_end_date = queryDate.get("query_end_date").toString();
			if ("".equals(query_end_date)) {

			} else {
				int daybetween = DateUtils.daysBetween(query_start_date, query_end_date);
				if (daybetween > 5) {
					return "????????????????????????????????????????????????????????????????????????";
				}
			}

		}
		if (reportError.equals("0")) {
			try {
				strRtnMsgContent = queryMsg(request, queryDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			strRtnMsgContent = WeiXinParamesUtil.dayReportFormatQuery;
		}
		if (null == strRtnMsgContent) {
			strRtnMsgContent = "?????????????????????????????????????????????????????????????????????";
		}
		return strRtnMsgContent;
	}

	/**
	 * 
	 * ?????????????????????
	 * 
	 * @param text
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ParseException
	 */
	public String dealDayReportDownLoad(HttpServletRequest request, String strMsgContent, String strFromUser)
			throws FileNotFoundException, IOException, ParseException {
		String strRtnMsgContent = "";
		query_start_date = "";
		query_end_date = "";
		SysUser sysuser = (SysUser) request.getSession().getAttribute(Constant.SESSION_LOGIN_USER);
		String username = sysuser.getName();
		// siftWorkContent2????????????????????????????????????????????????
		Map<String, Object> queryDate = siftWorkContent3(strMsgContent);

		if (null == queryDate) {
			reportError = "1";
		}

		if (reportError.equals("0")) {
			List<String> lstHeader = getHeaderListDayReport();
			List<Map<String, Object>> lstData = getDataListDayReport(request, queryDate);
			String filename = getDownLoadFilneName(username);
			String createFlag = exportExcel(filename, lstHeader, lstData, username);
			if ("sucuss".equals(createFlag)) {
				String type = "file";
				/*
				 * String accessToken = WeiXinUtil .getAccessToken(WeiXinParamesUtil.corpId,
				 * WeiXinParamesUtil.contactsSecret).getToken();
				 */
				WeiXinUtil weiXinUtil = new WeiXinUtil();
				String accessToken = "";

				// 2.????????????????????????????????????
				JSONObject upload = WeiXinUtil.uploadTempMaterial(type, filename);
				String media_id = upload.getString("media_id");
				String errmsg = upload.getString("errmsg");
				if ("ok".equals(errmsg)) {
					WeiXinUtil.SendFileMessage(media_id, type, accessToken, strFromUser);
				} else {
					strRtnMsgContent = "???????????????????????????" + errmsg + "???";
				}

			}

		} else {
			strRtnMsgContent = WeiXinParamesUtil.dayReportFormatDownload;
		}
		/*
		 * if (null == strRtnMsgContent) { strRtnMsgContent =
		 * "????????????????????????????????????????????????????????????????????????"; }
		 */

		return strRtnMsgContent;
	}

	/**
	 * 
	 * ?????????????????????
	 * 
	 * @param text
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ParseException
	 */
	public String dealQueryDownLoad(HttpServletRequest request, String strMsgContent, String strFromUser)
			throws FileNotFoundException, IOException, ParseException {
		String strRtnMsgContent = "";
		query_start_date = "";
		query_end_date = "";

		// siftWorkContent2????????????????????????????????????????????????
		Map<String, Object> queryParam = siftWorkContent4(strMsgContent);

		if (null == queryParam) {
			reportError = "1";
		}

		if (reportError.equals("0")) {
			List<String> lstHeader = getHeaderListDayReport();
			List<Map<String, Object>> lstData = getDataListDayReport(request, queryParam);
			String username = queryParam.get("query_name").toString();
			String filename = getDownLoadFilneName(username);
			String createFlag = exportExcel(filename, lstHeader, lstData, username);
			if ("sucuss".equals(createFlag)) {
				String type = "file";
				/*
				 * String accessToken = WeiXinUtil .getAccessToken(WeiXinParamesUtil.corpId,
				 * WeiXinParamesUtil.contactsSecret).getToken();
				 */
				WeiXinUtil weiXinUtil = new WeiXinUtil();
				String accessToken = "";

				// 2.????????????????????????????????????
				JSONObject upload = WeiXinUtil.uploadTempMaterial( type, filename);
				
				String errmsg = upload.getString("errmsg");
				if ("ok".equals(errmsg)) {
					String media_id = upload.getString("media_id");
					WeiXinUtil.SendFileMessage(media_id, type, accessToken, strFromUser);
				} else {
					strRtnMsgContent = "???????????????????????????" + errmsg + "???";
				}

			}

		} else {
			strRtnMsgContent = WeiXinParamesUtil.dayReportQueryDownload;
		}
		/*
		 * if (null == strRtnMsgContent) { strRtnMsgContent =
		 * "????????????????????????????????????????????????????????????????????????"; }
		 */

		return strRtnMsgContent;
	}

	public String getDownLoadFilneName(String strFromUser) {
		String file_name = null;
		String file_path = System.getProperty("user.dir") + "/download/";
		file_name = file_path + strFromUser + "__" + DateUtils.DateToTimestamp(new Date()) + ".xls";
		logger.info("file_name???" + file_name + "???");
		return file_name;
	}

	public String exportExcel(String fileName, List<String> lstHeader, List<Map<String, Object>> lstData,
			String strFromUser) throws FileNotFoundException, IOException {

		// ???????????????????????????

		// String realPath = SpringContextHolder.getRootRealPath();
		FileUtils.createFile(fileName);
		// ExportExcel ee = new ExportExcel("????????????", lstHeader);
		ExportExcel ee = new ExportExcel("????????????", lstHeader, strFromUser);
		for (int i = 0; i < lstData.size(); i++) {
			Row row = ee.addRow();
			Map<String, Object> mapcolu = lstData.get(i);
			ee.addCell(row, 0, mapcolu.get("order"));
			ee.addCell(row, 1, mapcolu.get("reportDate"));
			ee.addCell(row, 2, mapcolu.get("productName"));
			ee.addCell(row, 3, mapcolu.get("workContents"));
			/*
			 * for (int j = 0; j < lstData.get(i).size(); j++) { Map<String,Object> mapcolu
			 * = lstData.get(i); ee.addCell(row, j, lstData.get(i).get(j)); }
			 */
		}
		ee.writeFile(fileName);
		ee.dispose();
		return "sucuss";
	}

	/*
	 * ?????????????????????
	 * 
	 */
	public List<String> getHeaderListDayReport() {
		List<String> headerList = Lists.newArrayList();

		headerList.add("??????");
		headerList.add("??????");
		headerList.add("????????????");
		headerList.add("????????????");
		return headerList;
	}

	/*
	 * ????????????????????????????????? ?????????????????????????????? 1???header ??? posX,posY,starX,startY,data,label,cellstyle
	 * 2???detail ??? 3???summary??? 4???footer ??? ??????EXCEL????????????????????????????????????
	 */
	public List<Map<String, Object>> buildHeaderListDayReport() {
		List<String> headerList = Lists.newArrayList();
		List<Map<String, Object>> lstmp = new ArrayList();
		Map<String, Object> mp = new HashMap();
		return lstmp;
	}

	/*
	 * ???????????????????????????
	 * 
	 */
	public List<Map<String, Object>> getDataListDayReport(HttpServletRequest request, Map<String, Object> map)
			throws ParseException {
		List<DoufuTodayWork> listDayWork = queryMsgList(request, map);

		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

		if (null == listDayWork) {
			return null;
		}
		// ??????????????????
		for (int i = 0; i < listDayWork.size(); i++) {
			DoufuTodayWork dufuTodayWork = listDayWork.get(i);
			Map<String, Object> mpcolumn = new HashMap<String, Object>();
			mpcolumn.put("order", String.valueOf(i + 1));
			mpcolumn.put("reportDate", dufuTodayWork.getReportDate());
			mpcolumn.put("productName", dufuTodayWork.getProductName());
			mpcolumn.put("workContents", dufuTodayWork.getWorkContents());

			dataList.add(mpcolumn);
		}

		return dataList;
	}

	/**
	 * 
	 * ???????????????????????????????????????[??????]
	 * 
	 * @param text
	 * @return
	 */
	public static boolean justMsgDateFormat(String text) {
		String[] contentArray = text.trim().split("\n");
		int ipos = text.indexOf("[??????]");
		if (ipos == 0) {
		}
		if (contentArray.length != 0) {
			String strline1 = contentArray[1].toString().trim();
			System.out.println("??????????????????????????????" + strline1);
			if (isValidDate(strline1)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isValidDate(String str) {
		boolean convertSuccess = true;
// ??????????????????????????????/????????????/?????????????????????yyyy/MM/dd??????????????????
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
// ??????lenient???false. ??????SimpleDateFormat???????????????????????????????????????2007/02/29???????????????????????????2007/03/01
			format.setLenient(false);
			format.parse(str);
		} catch (ParseException e) {
			// e.printStackTrace();
// ??????throw java.text.ParseException??????NullPointerException????????????????????????
			convertSuccess = false;
		}
		return convertSuccess;
	}

	/**
	 * ????????????????????????????????????????????????
	 * 
	 * @param msg
	 * @return
	 */
	public static List<String> extractMessageByRegular(String msg) {

		List<String> list = new ArrayList<String>();
		List<String> list2 = new ArrayList<String>();
		Pattern p = Pattern.compile("\\[^\\]*\\]");
		Pattern p1 = Pattern.compile("???.*???");

		Matcher m = p.matcher(msg);
		Matcher m1 = p1.matcher(msg);

		while (m.find()) {
			list.add(m.group().substring(1, m.group().length() - 1));
		}
		while (m1.find()) {
			list2.add(m1.group().substring(1, m1.group().length() - 1));
		}
		return list;
	}

	/**
	 * ?????? ?????? ?????? ?????? map
	 * 
	 * @param workContent
	 * @return
	 */
	public static Map<String, List<String>> buildMap(String workContent) {
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		String[] contentArray = workContent.trim().split("\n");
		String productName = null;
		List<String> productWorkContent = null;
		for (String line : contentArray) {
			line = line.trim();
			if (line.startsWith("[") && line.endsWith("]")) {
				productName = line.substring(1, line.length() - 1);
				continue;
			}
			productWorkContent = result.get(productName);
			if (productWorkContent == null) {
				productWorkContent = new ArrayList<String>();
				if (null == productName) {
					productName = "??????";
				}
				result.put(productName, productWorkContent);
			}
			productWorkContent.add(line);
		}
		return result;
	}

	/**
	 * ???????????????????????????Map
	 * 
	 * @param text
	 * @return
	 */

	public Map<String, Object> siftWorkContent3(String text) {
		Map<String, Object> result = new HashMap<String, Object>();
		String[] contentArray = text.trim().split("\n");

		reportError = "1";
		if (contentArray.length < 2) {
			return null;
		}
		String strQueryDate = contentArray[1].toString().trim();
		if (strQueryDate.indexOf("???") < 0) {
			query_start_date = strQueryDate;
			if (!isValidDate(query_start_date))
				return null;
		} else {
			String[] dateArray = strQueryDate.split("???");
			if (dateArray.length < 2) {
				return null;
			} else {
				query_start_date = dateArray[0].toString();
				query_end_date = dateArray[1].toString();
				if (!isValidDate(query_start_date))
					return null;
				if (!isValidDate(query_end_date))
					return null;
			}
		}

		result.put("query_start_date", query_start_date);
		result.put("query_end_date", query_end_date);
		reportError = "0";
		return result;
	}

	/**
	 * ???????????????????????????Map
	 * 
	 * @param text
	 * @return
	 */

	public Map<String, Object> siftWorkContent4(String text) {
		Map<String, Object> result = new HashMap<String, Object>();
		String[] contentArray = text.trim().split("\n");

		reportError = "1";
		if (contentArray.length < 3) {
			return null;
		}
		String strQueryName = contentArray[1].toString().trim();
		String strQueryDate = contentArray[2].toString().trim();
		if (strQueryDate.indexOf("???") < 0) {
			query_start_date = strQueryDate;
			if (!isValidDate(query_start_date))
				return null;
		} else {
			String[] dateArray = strQueryDate.split("???");
			if (dateArray.length < 2) {
				return null;
			} else {

				query_start_date = dateArray[0].toString();
				query_end_date = dateArray[1].toString();
				if (!isValidDate(query_start_date))
					return null;
				if (!isValidDate(query_end_date))
					return null;
			}
		}

		result.put("query_start_date", query_start_date);
		result.put("query_name", strQueryName);
		result.put("query_end_date", query_end_date);
		reportError = "0";
		return result;
	}

	/**
	 * ?????????????????????Map
	 * 
	 * @param text
	 * @return
	 */
	public Map<String, Map<String, List<String>>> siftWorkContent1(String text) {
		Map<String, Map<String, List<String>>> result = new LinkedHashMap<String, Map<String, List<String>>>();
		String pattern = ".*??????(.*)??????(.*)??????(.*)";
		Pattern r = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher m = r.matcher(text);
		if (m.find()) {
			result.put("today", buildMap(m.group(1)));
			result.put("towarrow", buildMap(m.group(2)));
			result.put("summary", buildMap(m.group(3)));
			reportError = "0";
		} else {
			reportError = "1";
		}
		return result;
	}

	/**
	 * ?????????????????????Map
	 * 
	 * @param text
	 * @return
	 */

	public Map<String, Map<String, List<String>>> siftWorkContent2(String text) {
		Map<String, Map<String, List<String>>> result = new LinkedHashMap<String, Map<String, List<String>>>();
		String[] contentArray = text.trim().split("\n");

		reportError = "1";
		if (contentArray.length < 3) {
			return null;
		}
		if (!isValidDate(contentArray[1].toString())) {
			return null;
		} else {
			by_date = contentArray[1].toString();
		}

		// String sysName = contentArray[2].toString();

		String strSubText = text.substring(text.indexOf(by_date) + 10, text.length());
		result.put("bydate", buildMap(strSubText));
		reportError = "0";
		return result;
	}

	public static ReportTmplate initTemp(Map<String, Map<String, List<String>>> work) {
		ReportTmplate reportTmplate = new ReportTmplate();
		// ?????? ?????? ????????????
		Map<String, List<String>> todayWork = work.get("today");
		// ????????????
		Iterator<Entry<String, List<String>>> iter = todayWork.entrySet().iterator();
		List<String> todaywork = new ArrayList<>();

		while (iter.hasNext()) {
			Entry<String, List<String>> entry = iter.next();
			String productName = entry.getKey();
			// System.out.println(productName);
			List<String> productWork = entry.getValue();
			for (String w : productWork) {
				// System.out.println(productName + "|" + w);
				todaywork.add(productName + "|" + w);
			}
		}

		reportTmplate.setLst_today(todaywork);
		reportTmplate.setTodaywork(ListToArray(todaywork));

		// ?????? ?????? ????????????
		Map<String, List<String>> mtowarrowWork = work.get("towarrow");
		// ????????????
		Iterator<Entry<String, List<String>>> iter2 = mtowarrowWork.entrySet().iterator();
		List<String> towarrowwork = new ArrayList<>();

		while (iter2.hasNext()) {
			Entry<String, List<String>> entry = iter2.next();
			String productName = entry.getKey();
			// System.out.println(productName);
			List<String> productWork = entry.getValue();
			for (String w : productWork) {
				// System.out.println(productName + "|" + w);
				towarrowwork.add(productName + "|" + w);
			}
		}

		reportTmplate.setLst_tomorrow(towarrowwork);
		reportTmplate.setTmorrowwork(ListToArray(towarrowwork));

		// ?????? ?????? ????????????
		Map<String, List<String>> msummary = work.get("summary");
		// ????????????
		Iterator<Entry<String, List<String>>> iter3 = msummary.entrySet().iterator();
		List<String> summarywork = new ArrayList<>();

		while (iter3.hasNext()) {
			Entry<String, List<String>> entry = iter3.next();
			String productName = entry.getKey();
			System.out.println(productName);
			List<String> productWork = entry.getValue();
			for (String w : productWork) {
				System.out.println(productName + "|" + w);
				summarywork.add(productName + "|" + w);
			}
		}

		reportTmplate.setLst_summary(summarywork);
		reportTmplate.setSummary(ListToArray(summarywork));

		return reportTmplate;
	}

	/**
	 * ????????????????????????????????? ReportTmplate ???????????? ?????? Map<String, List<String>>> work
	 * 
	 * @param list
	 * @return
	 */
	public static ReportTmplate initTemp2(Map<String, Map<String, List<String>>> work) {
		ReportTmplate reportTmplate = new ReportTmplate();
		// ?????? ?????? ????????????
		Map<String, List<String>> todayWork = work.get("bydate");
		// ????????????
		Iterator<Entry<String, List<String>>> iter = todayWork.entrySet().iterator();
		List<String> lst_bydate = new ArrayList<>();

		while (iter.hasNext()) {
			Entry<String, List<String>> entry = iter.next();
			String productName = entry.getKey();
			// System.out.println(productName);
			List<String> productWork = entry.getValue();
			for (String w : productWork) {
				// System.out.println(productName + "|" + w);
				lst_bydate.add(productName + "|" + w);
			}
		}
		reportTmplate.setLst_bydate(lst_bydate);
		reportTmplate.setBydate(ListToArray(lst_bydate));

		return reportTmplate;
	}

	/**
	 * List to Array
	 * 
	 * @param list
	 * @return
	 */
	public static String[] ListToArray(List<String> list) {
		String[] arr = list.toArray(new String[list.size()]);
		return arr;
	}

	public List<DoufuTodayWork> putString2Enty(String[] strin, SysUser sysuser, String report_datein) {

		List<DoufuTodayWork> listEntyInsert = new ArrayList<DoufuTodayWork>();
		String isAfter = "0";
		if (report_datein.equals(by_date)) {
			isAfter = "1";
		}
		if (report_datein.equals(next_date)) {
			isAfter = "2";
		}
		for (String s1 : strin) {
			int startPos = s1.indexOf("|");
			String productName = s1;
			String workContent = s1;

			// logger.info("startPos???" + startPos + "???");
			// logger.info("s1.length()-1 ???" + (s1.length() - 1) + "???");

			if (startPos > 0) {
				productName = s1.substring(0, startPos);
			}
			if (startPos == 0) {
				productName = s1;
			}
			if (startPos != s1.length() - 1 && startPos != 0) {
				workContent = s1.substring(startPos + 1, s1.length());
				// logger.info("workContent???" + workContent + "???");
				// logger.info("s1 ???" + s1 + "???");
			}

			String finishRate = "";
			if (s1.indexOf("[") >= 0 && s1.indexOf("]") >= 0) {
				finishRate = s1.substring(s1.indexOf("[") + 1, s1.indexOf("]"));
			}
			if (s1.indexOf("???") >= 0 && s1.indexOf("???") >= 0) {
				finishRate = s1.substring(s1.indexOf("???") + 1, s1.indexOf("???"));
			}

			if (s1.indexOf("???") >= 0 && s1.indexOf("???") >= 0) {
				finishRate = s1.substring(s1.indexOf("???") + 1, s1.indexOf("???"));
			}

			DoufuTodayWork entryDoufuTodayWork = new DoufuTodayWork();
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
			entryDoufuTodayWork.setReportDate(report_datein);
			entryDoufuTodayWork.setIsEmergency("1");
			entryDoufuTodayWork.setImpoLevel("0");
			entryDoufuTodayWork.setIsImportant("1");
			entryDoufuTodayWork.setFinishPercent(finishRate);
			entryDoufuTodayWork.setWorkContents(workContent);
			entryDoufuTodayWork.setWorkDetail(s1);
			entryDoufuTodayWork.setInputOrder(iorder++);
			entryDoufuTodayWork.setRemarks(sysuser.getName());
			entryDoufuTodayWork.setReporterName(sysuser.getUsername());
			entryDoufuTodayWork.setIsAfter(isAfter);
			entryDoufuTodayWork.setReporterId(sysuser.getId());
			entryDoufuTodayWork.setProductName(productName);

			listEntyInsert.add(entryDoufuTodayWork);
		}
		return listEntyInsert;
	}

	/**
	 * ?????????????????????????????????
	 * 
	 * @param msg
	 * @return
	 * @throws ParseException
	 */

	public String queryMsg(HttpServletRequest request, Map<String, Object> map) throws ParseException {
		String queryResult = null;
		String dynamicSQL = "";
		SysUser sysuser = (SysUser) request.getSession().getAttribute(Constant.SESSION_LOGIN_USER);
		Map<String, Object> queryMap = new HashMap<String, Object>();
		if ("".equals(map.get("query_start_date")) || null == map.get("query_start_date")) {
			return null;
		} else {

		}

		if ("".equals(map.get("query_end_date")) || null == map.get("query_end_date")) {
			// queryMap.put("endDate", map.get("query_start_date"));
			dynamicSQL = "t.REPORT_DATE ='" + query_start_date + "'";
		} else {
			if (DateUtils.daysBetween(query_start_date, query_end_date) > 5)
				return "???????????????????????????????????????????????????????????????????????????";
			dynamicSQL = "t.REPORT_DATE >='" + map.get("query_start_date").toString() + "' and t.REPORT_DATE <='"
					+ map.get("query_end_date").toString() + "'";
		}

		queryMap.put("reporterName", sysuser.getUsername());
		queryMap.put("dynamicSQL", dynamicSQL);
		queryMap.put("sortC", "report_date,input_order");
		queryMap.put("order", "asc");
		List<DoufuTodayWork> lstQuery = doufuTodayWorkService.entityList(queryMap);
		if (null == lstQuery)
			return null;
		queryResult = formatRtnQueryMsg(lstQuery);
		/*
		 * for (DoufuTodayWork doufuTodayWork : lstQuery) { if (null == queryResult) {
		 * queryResult = "???" + doufuTodayWork.getReportDate() + "???" + "???" +
		 * doufuTodayWork.getProductName() + "???" + doufuTodayWork.getWorkContents(); }
		 * else { queryResult = queryResult + "\n???" + doufuTodayWork.getReportDate() +
		 * "???" + "???" + doufuTodayWork.getProductName() + "???" +
		 * doufuTodayWork.getWorkContents(); }
		 * 
		 * }
		 */
		return queryResult;

	}

	/**
	 * ??????????????????????????????????????????????????????????????????
	 * 
	 * @param msg
	 * @return
	 * @throws ParseException
	 */

	public String formatRtnQueryMsg(List<DoufuTodayWork> lstQuery) {
		String queryResult = null;
		String strolddate = null;
		List<Map<String, List<DoufuTodayWork>>> lstMap = new ArrayList<Map<String, List<DoufuTodayWork>>>();
		List<DoufuTodayWork> lstsplit = new ArrayList<DoufuTodayWork>();
		if (null == lstQuery || lstQuery.size() == 0)
			return null;
		Map<String, List<DoufuTodayWork>> mpdayorder = new HashMap<String, List<DoufuTodayWork>>();
		for (DoufuTodayWork doufuTodayWork : lstQuery) {

			logger.info("??????Key" + doufuTodayWork.getReportDate());

			List<DoufuTodayWork> lstExist = (List<DoufuTodayWork>) mpdayorder.get(doufuTodayWork.getReportDate());
			if (null == lstExist) {
				lstExist = new ArrayList<DoufuTodayWork>();
				lstExist.add(doufuTodayWork);
				mpdayorder.put(doufuTodayWork.getReportDate(), lstExist);

			} else {
				lstExist.add(doufuTodayWork);
				mpdayorder.put(doufuTodayWork.getReportDate(), lstExist);
			}

			// strolddate = doufuTodayWork.getReportDate();
		}
		mpdayorder = sortMapByKey(mpdayorder);
		if (null == mpdayorder) {
			return null;
		}
		Set set = mpdayorder.keySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			List<DoufuTodayWork> lstDou = (List<DoufuTodayWork>) mpdayorder.get(key);
			// System.out.println("MAP???????????????"+key+"???");
			if (null == queryResult) {
				queryResult = "???" + key + "???\n";
			} else {
				queryResult = queryResult + "???" + key + "???\n";
			}
			for (DoufuTodayWork dtw : lstDou) {
				queryResult = queryResult + "???" + dtw.getProductName() + "??????" + dtw.getWorkContents() + "???\n";
				// System.out.println(dtw.getWorkContents());
			}
		}
		// Iterator<Entry<String, List<DoufuTodayWork>>> iter =
		// mpdayorder.entrySet().iterator();
		logger.info(queryResult);

		return queryResult;
	}

	/**
	 * 
	 * ??? Map???key????????????
	 * 
	 */

	public static Map<String, List<DoufuTodayWork>> sortMapByKey(Map<String, List<DoufuTodayWork>> map) {

		if (map == null || map.isEmpty()) {

			return null;

		}

		Map<String, List<DoufuTodayWork>> sortMap = new TreeMap<String, List<DoufuTodayWork>>();

		sortMap.putAll(map);

		return sortMap;

	}

	/**
	 * ?????????????????????????????????
	 * 
	 * @param msg
	 * @return
	 * @throws ParseException
	 */

	public List<DoufuTodayWork> queryMsgList(HttpServletRequest request, Map<String, Object> map)
			throws ParseException {
		String queryResult = null;
		String dynamicSQL = "";
		SysUser sysuser = (SysUser) request.getSession().getAttribute(Constant.SESSION_LOGIN_USER);
		Map<String, Object> queryMap = new HashMap<String, Object>();
		if ("".equals(map.get("query_start_date")) || null == map.get("query_start_date")) {
			return null;
		} else {

		}

		if ("".equals(map.get("query_end_date")) || null == map.get("query_end_date")) {
			// queryMap.put("endDate", map.get("query_start_date"));
			dynamicSQL = "t.REPORT_DATE ='" + query_start_date + "'";
		} else {
			if (DateUtils.daysBetween(query_start_date, query_end_date) > 5)
				logger.info("???????????????????????????????????????????????????????????????????????????");
			dynamicSQL = "t.REPORT_DATE >='" + map.get("query_start_date").toString() + "' and t.REPORT_DATE <='"
					+ map.get("query_end_date").toString() + "'";
		}
		String queryUser = "";

		if ("".equals(map.get("query_name")) || null == map.get("query_name")) {
			queryUser = sysuser.getUsername();
		} else {
			String queryUserName = (String) map.get("query_name");
			WeiXinUtil weiXinUtil = new WeiXinUtil();
			SysUser sysUserQueryOne = weiXinUtil.getUserInfo(queryUserName);
			if (null != sysUserQueryOne) {
				queryUser = sysUserQueryOne.getUsername();
			}
		}
//       logger.info("???????????????????????????"+queryUser+"???");
		queryMap.put("reporterName", queryUser);

		queryMap.put("dynamicSQL", dynamicSQL);
		queryMap.put("sortC", "create_date,input_order,PRODUCT_NAME");
		queryMap.put("order", "asc");
		List<DoufuTodayWork> lstQuery = doufuTodayWorkService.entityList(queryMap);
		if (null == lstQuery) {
			return null;
		} else {
			return lstQuery;

		}

	}

	/**
	 * ???????????????????????????
	 * 
	 * @param msg
	 * @return
	 */

	public Result saveMsg(HttpServletRequest request, ReportTmplate reportTmplate) {

		int successNumInsert = 0;
		int failureNumInsert = 0;
		SysUser sysuser = (SysUser) request.getSession().getAttribute(Constant.SESSION_LOGIN_USER);

		List<DoufuTodayWork> listEntytoday = new ArrayList<DoufuTodayWork>();
		List<DoufuTodayWork> listEntytomorrow = new ArrayList<DoufuTodayWork>();
		List<DoufuTodayWork> listEntysummary = new ArrayList<DoufuTodayWork>();
		List<DoufuTodayWork> listEntybydate = new ArrayList<DoufuTodayWork>();
		List<DoufuTodayWork> listEntyInsert = new ArrayList<DoufuTodayWork>();

		if (null != reportTmplate.getTodaywork() && reportTmplate.getTodaywork().length > 0) {

			listEntytoday = putString2Enty(reportTmplate.getTodaywork(), sysuser, report_date);
		}

		if (null != reportTmplate.getTmorrowwork() && reportTmplate.getTmorrowwork().length > 0) {

			listEntytomorrow = putString2Enty(reportTmplate.getTmorrowwork(), sysuser, next_date);
		}

		if (null != reportTmplate.getSummary() && reportTmplate.getSummary().length > 0) {

			listEntysummary = putString2Enty(reportTmplate.getSummary(), sysuser, report_date);
		}

		if (null != reportTmplate.getBydate() && reportTmplate.getBydate().length > 0) {

			listEntybydate = putString2Enty(reportTmplate.getBydate(), sysuser, by_date);
		}
		listEntyInsert.addAll(listEntytoday);
		listEntyInsert.addAll(listEntytomorrow);
		listEntyInsert.addAll(listEntysummary);
		listEntyInsert.addAll(listEntybydate);
		if (listEntyInsert != null && listEntyInsert.size() > 0) {
			// ??????by_date ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
			if (by_date.equals("")) {
				Map<String, Object> params = new HashMap();
				params.put("reportDate", report_date);
				params.put("reporterName", sysuser.getUsername().toString());
				doufuTodayWorkService.deleteByParams(params);
				params.put("reportDate", next_date);
				params.put("reporterName", sysuser.getUsername().toString());
				doufuTodayWorkService.deleteByParams(params);

			}

			successNumInsert = doufuTodayWorkService.insertBatch(listEntyInsert);
		}
		return new Result(1, "??????????????????????????????" + successNumInsert + "??????????????????" + failureNumInsert + "???");

	}

	/**
	 * ????????????????????????????????????????????????
	 * 
	 * @param msg
	 * @return
	 */

	public String dealEvent(HttpServletRequest request, String strEvenKey) {
		String strRtn = null;
		logger.info("--------------??????????????????????????????????????????????????????????????????????????????\n" + strEvenKey);

		return strRtn;

	}
	
	


	

}
