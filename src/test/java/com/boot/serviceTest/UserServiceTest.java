package com.boot.serviceTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.boot.baseTest.SpringTestCase;
import com.boot.configurations.QuatzConfig;
import com.boot.util.CommonEntity;
import com.boot.util.DateUtils;
import com.boot.util.FileUtils;
import com.boot.util.StringConvert;
import com.boot.util.SysUserUtils;
import com.boot.util.excel.template.utils.PoiUtil;
import com.boot.util.qq.weixin.mp.aes.DayReportUtil;
import com.boot.util.qq.weixin.mp.aes.WeiXinUtil;
import com.boot.web.sys.service.SysUserService;
import com.boot.web.todaywork.model.DoufuTodayWork;
import com.boot.web.todaywork.service.DoufuTodayWorkService;
import com.boot.web.wxgroup.model.WxUserGroup;
import com.boot.web.wxgroup.service.WxUserGroupService;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.github.pagehelper.PageInfo;

import aj.org.objectweb.asm.Label;

import com.boot.web.sys.model.SysUser;
import com.boot.web.todaywork.controller.DoufuTodayWorkController;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

public class UserServiceTest extends SpringTestCase {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private DoufuTodayWorkService doufuTodayWorkService;

	@Autowired
	private DoufuTodayWorkController doufuTodayWorkController;

	@Autowired
	private WxUserGroupService wxUserGroupService;
	


	public void selectUserByIdTest() throws SchedulerException {

		DoufuTodayWork doufuTodayWork = new DoufuTodayWork();
		QuatzConfig quatzConfig = new QuatzConfig();
		// doufuTodayWorkService.selectOne(doufuTodayWork);

		/*
		 * SysUser user = sysUserService.selectUserByName("admin"); logger.info("????????????" +
		 * user);
		 * 
		 * Map<String, Object> params = new HashMap<String, Object>(); DoufuTodayWork
		 * doufuTodayWork = new DoufuTodayWork();
		 * 
		 * try {
		 * 
		 * logger.info("????????????==----------------------==="); params.put("pageNum", "8");
		 * params.put("pageSize", "5"); // params.put("pageList", "[10, 25, 50, 100]");
		 * 
		 * //List<DoufuTodayWork> lstRtn = (List<DoufuTodayWork>)
		 * doufuTodayWorkService.queryOne(mapparams);
		 * 
		 * // List<CommonEntity> lst = sysUserService.getUserModule(mapparams);
		 * 
		 * // params.put("dynamicSQL", SysUserUtils.dataScopeFilterString1("o", "u",
		 * " todaywork/doufuTodayWork", "id"));
		 * 
		 * if (params.containsKey("sortC")) { // ??????????????????????????????????????????????????????????????????????????????
		 * params.put("sortC",
		 * StringConvert.camelhumpToUnderline(params.get("sortC").toString())); }
		 * PageInfo<DoufuTodayWork> page =
		 * doufuTodayWorkService.queryPageInfoEntity(params); PageInfo<CommonEntity>
		 * page2 =doufuTodayWorkService.queryPageInfo(params);
		 * 
		 * // doufuTodayWorkService.queryOne(params); //CameHumpInterceptor.
		 * 
		 * logger.info("----------------------------????????????====="); } catch (Exception e) {
		 * logger.info(e.getMessage());
		 * 
		 * }
		 */
		/*
		 * String strValue="2019-08-20T15:14:49.000+0000";
		 * 
		 * LocalDateTime date =
		 * LocalDateTime.parse(strValue,DateTimeFormatter.ISO_OFFSET_DATE_TIME); String
		 * dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 * logger.info(dateString);
		 * 
		 */

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("groupname", "??????");
		params.put("reportdate", "2019-09-16");
		List<CommonEntity> lst = doufuTodayWorkService.queryNotCommitUser(params);
		String userName, userAccount;
		if (lst.size() != 0) {
			for (int i = 0; i < lst.size(); i++) {
				Map<String, Object> mpGroupName = (Map<String, Object>) lst.get(i);
				userName = mpGroupName.get("name").toString();
				logger.info(userName);
			}
		}

		quatzConfig.CronTriggerTest();
	}

	public void wxUserGroup() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("isMsg", "1");

		List<WxUserGroup> lst = wxUserGroupService.entityList(params);
		for (int i = 0; i < lst.size(); i++) {
			WxUserGroup wxUserGroup = lst.get(i);
			logger.info("??????????????????" + wxUserGroup.getUserCode() + "???" + "??????????????????" + wxUserGroup.getUsername() + "???"
					+ "?????????????????????" + wxUserGroup.getGroupCname() + "???");
			String wxGroupName = wxUserGroup.getGroupCname();
			if ("".equals(wxGroupName) || null == wxGroupName) {

			} else {
				List<String> lstGroupName = splitGroupName(wxGroupName);
				if (null != lstGroupName) {
					for (String gname : lstGroupName) {
						logger.info("???????????????????????????" + gname + "???");
					}
				}
			}

		}

	}



	public void getWeek() {
		long startTime1 = 1530613938532l;
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);// ??????????????????????????????????????????
		calendar.setMinimalDaysInFirstWeek(4);// ??????????????????
		calendar.setTimeInMillis(System.currentTimeMillis());// ????????????????????????
		int weekYear = calendar.get(Calendar.YEAR);// ??????????????????
		int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);// ??????????????????????????????????????????

		System.out.println("????????????" + weekOfYear);
		calendar.setWeekDate(weekYear, weekOfYear, 2);// ??????????????????????????????????????????
		long starttime = calendar.getTime().getTime();// ??????????????????????????????????????????
		calendar.setWeekDate(weekYear, weekOfYear, 1);// ??????????????????????????????????????????
		long endtime = calendar.getTime().getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateStart = simpleDateFormat.format(starttime);// ????????????????????????????????????
		String dateEnd = simpleDateFormat.format(endtime);
		System.out.println(dateStart);
		System.out.println(dateEnd);
	}

	//@Test
	public void testQurMsg() throws FileNotFoundException {
		Map<String, Object> queryMap = new HashMap<String, Object>();
	
		queryMap.put("reporterName", "bechalin");
		queryMap.put("dynamicSQL", "t.REPORT_DATE >='2019-09-20' and t.REPORT_DATE <='2019-09-25' ");
		queryMap.put("sortC", "report_date,input_order");
		queryMap.put("order", "asc");
		List<DoufuTodayWork> lstQuery = doufuTodayWorkService.entityList(queryMap);

		DayReportUtil dayReportUtil = new DayReportUtil();
		dayReportUtil.formatRtnQueryMsg(lstQuery);
	}

	// TODO Auto-generated method stub
	
	
	public void testJXLWrite() throws ParsePropertyException, org.apache.poi.openxml4j.exceptions.InvalidFormatException, IOException {	
	//List<TempCell> lst = new ArrayList();
	String templatePath = "G:\\GITBRANCH_LOCAL\\sharewithothers\\4quater\\reporttemplate\\???????????????????????????????????????_??????.xlsx";
	String targetPath = "G:\\GITBRANCH_LOCAL\\sharewithothers\\4quater\\reporttemplate\\???????????????????????????????????????_?????????.xlsx";
    InputStream is = null;
    Workbook wb = null;
    
    
	// ????????????????????????

    OutputStream os = new FileOutputStream("D:\\workspace\\krm_java\\demo\\out.xls");
    
    Map<String, Object> model = new HashMap<String, Object>();
    model.put("name", "??????");
    model.put("date", "2017/12/12");
    
    XLSTransformer former = new XLSTransformer();
    former.transformXLS(templatePath, model, targetPath);
    
    System.out.println("the end !!!");
    
		/*
		 * try { is = new FileInputStream(fileName);
		 * 
		 * FileInputStream tps = new FileInputStream(new File(fileName)); final
		 * XSSFWorkbook tpWorkbook = new XSSFWorkbook(tps); XSSFWorkbook workbook = new
		 * XSSFWorkbook(); workbook = tpWorkbook; FileUtils.createFile(outfileName);
		 * Sheet sheet = workbook.createSheet("??????????????????"); FileOutputStream os = new
		 * FileOutputStream(outfileName); workbook.write(os); //List
		 * readExcel(InputStream is, String fileName) os.close(); tps.close();
		 * 
		 * //List lst = PoiUtil.readExcel(is,fileName); } catch (FileNotFoundException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */
	}
	
	
	public void testCountCommitTimes() {
		
		
		Map<String, Object> queryMap = new HashMap<String, Object>();
		
		queryMap.put("reporterName", "bechalin");
		queryMap.put("dynamicSQL", "t.REPORT_DATE >='2019-09-01' and t.REPORT_DATE <='2019-09-30' ");
		queryMap.put("sortC", "report_date,input_order");
		queryMap.put("order", "asc");
		List<CommonEntity>  lstResult = doufuTodayWorkService.countCommitTimes(queryMap);
	    logger.info("???????????????????????????"+lstResult.size());
		
	    Map<String, Object> commitTimeMap = new HashMap<String, Object>();
	    commitTimeMap.put("bechalin", lstResult.size());
	    
	}
	
	@Test
	public  void queryCommitTimes(){
		 Map<String,Object> mpRtn = new TreeMap<>();
		 
		List<Map<String, Object>> mplist =  getInfoUser();
		
		if(null == mplist) {
			return ;
		}
		for(int i=0;i<mplist.size();i++) {
			Map<String,Object> mpinfoUser = mplist.get(i);
			List<String> lstGroupName = (List<String>) mpinfoUser.get("groupList");

			logger.info("????????????????????????????????????:"+lstGroupName.get(0));

			
		}
		return ;
		
	}
	
	//@Test
	public void testQueryNotCommit() {
		String reportToday = DateUtils.DateToStr8(new Date());
		String reportPreday = DateUtils.getPreDateByDate8(reportToday);
		String msgContent = "???????????????????????????????????????????????????";
		List<Map<String, Object>> mplist =  getInfoUser();
		
		if(null == mplist) {
			return;
		}
		for(int i=0;i<mplist.size();i++) {
			Map<String, Object> mpinfoUser = mplist.get(i);
			String infoUserCode = mpinfoUser.get("userCode").toString();
			String instId = mpinfoUser.get("instid").toString();
		    logger.info("istid"+instId);
			List<String> lstGroupName = (List<String>) mpinfoUser.get("groupList");
			 String notCommitInfo = null;
			if(null != lstGroupName) {
				 for(String gname:lstGroupName) {
					 logger.info("???????????????????????????"+gname +"???" );
					 if(notCommitInfo == null) {
					 notCommitInfo = queryNotCommitUser(gname, reportPreday);
					 
					 }else {
					 notCommitInfo = notCommitInfo +"\n"+queryNotCommitUser(gname, reportPreday);
					 }
					 
				 }
				if(null == notCommitInfo) {
				return;	
				}
			}
			
		}
		
	}


	public List<Map<String, Object>> getInfoUser() {
		List<Map<String, Object>> mplist = new ArrayList<Map<String, Object>>();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("isMsg", "1");

		List<WxUserGroup> lst = wxUserGroupService.entityList(params);
		if (null == lst) {
			return null;
		}
		for (int i = 0; i < lst.size(); i++) {
			WxUserGroup wxUserGroup = lst.get(i);
			logger.info("??????????????????" + wxUserGroup.getUserCode() + "???" + "??????????????????" + wxUserGroup.getUsername() + "???"
					+ "?????????????????????" + wxUserGroup.getGroupCname() + "???");
			String wxGroupName = wxUserGroup.getGroupCname();
			Map<String, Object> infUser = new HashMap<String, Object>();
			infUser.put("userCode", wxUserGroup.getUserCode());
			infUser.put("instid", wxUserGroup.getInstId());
			if ("".equals(wxGroupName) || null == wxGroupName) {

			} else {
				List<String> lstGroupName = splitGroupName(wxGroupName);
				infUser.put("groupList", lstGroupName);
				/*
				 * if (null != lstGroupName) { for (String gname : lstGroupName) {
				 * logger.info("???????????????????????????" + gname + "???"); } }
				 */
			}
			mplist.add(infUser);

		}

		return mplist;
	}
	
	public List<String> splitGroupName(String groupName) {
		String[] strSplit = groupName.split("\\|");

		List<String> lst = Arrays.asList(strSplit);

		return lst;

	}	
	
	

	
	
	public String queryNotCommitUser(String groupname, String reportdate){
		String strRtn = "";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("groupname", groupname);
		params.put("reportdate", reportdate);
		List<CommonEntity> lst = doufuTodayWorkService.queryNotCommitUser(params);
		String userName, userAccount;
		if (lst.size() != 0) {
			for (int i = 0; i < lst.size(); i++) {
				Map<String, Object> mpGroupName = (Map<String, Object>) lst.get(i);
				userName = mpGroupName.get("name").toString();
				userAccount = mpGroupName.get("account").toString();
				if (i == 0) {
					strRtn = userName;
				} else {
					strRtn = strRtn + "???" + userName;
				}

			}
			if(!"".equals(strRtn)) {
			strRtn ="???????????????" +groupname+"???????????????"+reportdate+"???\n????????????????????????\n"+strRtn+"\n???????????????????????????????????????";
			}
		}else {
			strRtn ="???????????????" +groupname+"???????????????"+reportdate+"??? ?????????????????????";
		}

		return strRtn;

	}
}

