package com.boot.web.tomorrowplan.model;

import java.math.BigDecimal;
import java.util.*;

import com.boot.util.BaseEntity;
import com.boot.util.BaseFile;

import javax.persistence.Transient;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * 
 * @author 赵祖龙
 * 明天工作计划表javaBean
 * 2019-08-26
 */
@Table(name="doufu_tomorrow_plan")
public class DoufuTomorrowPlan extends BaseEntity<DoufuTomorrowPlan>{
	
	private static final long serialVersionUID = 1L;
	
	@Id
    private String  id;      //主键
    private String  workContents;      //工作内容简写
    private String  workDetail;      //工作内容详细描述
    private String  finishPercent;      //完成比例
    private String  isImportant;      //是否重要
    private String  isEmergency;      //是否紧急
    private String  impoLevel;      //重要级别
    private String  projectGroupId;      //所在项目组名称
    private String  projectId;      //项目名称
    private String  productId;      //对应产品
    private String  delFlag;      //逻辑删除标记(0.正常，1.删除)
    private String  status;      //状态
    private String  instId;      //机构ID
    private String  loginIp;      //登录IP
    private Date  loginDate;      //登录日期
    private String  createBy;      //创建人
    private Date  createDate;      //创建时间
    private String  updateBy;      //最近修改人
    private Date  updateDate;      //最近修改时间
    private String  remarks;      //描述
    private String  reportDate;      //报告日期
	@Transient
	private List<BaseFile>	upfileList;			//批量上传文件list
	
	public DoufuTomorrowPlan() {
    	super.setModuleName("明天工作计划表");
	}
	/**
	 * 主键
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	/**
	 * 工作内容简写
	 * @param workContents
	 */
	public void setWorkContents(String workContents) {
		this.workContents = workContents;
	}
	public String getWorkContents() {
		return workContents;
	}
	/**
	 * 工作内容详细描述
	 * @param workDetail
	 */
	public void setWorkDetail(String workDetail) {
		this.workDetail = workDetail;
	}
	public String getWorkDetail() {
		return workDetail;
	}
	/**
	 * 完成比例
	 * @param finishPercent
	 */
	public void setFinishPercent(String finishPercent) {
		this.finishPercent = finishPercent;
	}
	public String getFinishPercent() {
		return finishPercent;
	}
	/**
	 * 是否重要
	 * @param isImportant
	 */
	public void setIsImportant(String isImportant) {
		this.isImportant = isImportant;
	}
	public String getIsImportant() {
		return isImportant;
	}
	/**
	 * 是否紧急
	 * @param isEmergency
	 */
	public void setIsEmergency(String isEmergency) {
		this.isEmergency = isEmergency;
	}
	public String getIsEmergency() {
		return isEmergency;
	}
	/**
	 * 重要级别
	 * @param impoLevel
	 */
	public void setImpoLevel(String impoLevel) {
		this.impoLevel = impoLevel;
	}
	public String getImpoLevel() {
		return impoLevel;
	}
	/**
	 * 所在项目组名称
	 * @param projectGroupId
	 */
	public void setProjectGroupId(String projectGroupId) {
		this.projectGroupId = projectGroupId;
	}
	public String getProjectGroupId() {
		return projectGroupId;
	}
	/**
	 * 项目名称
	 * @param projectId
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectId() {
		return projectId;
	}
	/**
	 * 对应产品
	 * @param productId
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductId() {
		return productId;
	}
	/**
	 * 逻辑删除标记(0.正常，1.删除)
	 * @param delFlag
	 */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String getDelFlag() {
		return delFlag;
	}
	/**
	 * 状态
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus() {
		return status;
	}
	/**
	 * 机构ID
	 * @param instId
	 */
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public String getInstId() {
		return instId;
	}
	/**
	 * 登录IP
	 * @param loginIp
	 */
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public String getLoginIp() {
		return loginIp;
	}
	/**
	 * 登录日期
	 * @param loginDate
	 */
	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
	public Date getLoginDate() {
		return loginDate;
	}
	/**
	 * 创建人
	 * @param createBy
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreateBy() {
		return createBy;
	}
	/**
	 * 创建时间
	 * @param createDate
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * 最近修改人
	 * @param updateBy
	 */
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	/**
	 * 最近修改时间
	 * @param updateDate
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	/**
	 * 描述
	 * @param remarks
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getRemarks() {
		return remarks;
	}
	/**
	 * 报告日期
	 * @param reportDate
	 */
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public String getReportDate() {
		return reportDate;
	}

	/**
	 * 批量上传文件list
	 * @param upfileList
	 */
	public List<BaseFile> getUpfileList() {
		return upfileList;
	}
	public void setUpfileList(List<BaseFile> upfileList) {
		this.upfileList = upfileList;
	}

	@Override
	public String toString() {
		return "明天工作计划表： {\"id\": \""+id+"\", \"workContents\": \""+workContents+"\", \"workDetail\": \""+workDetail+"\", \"finishPercent\": \""+finishPercent+"\", \"isImportant\": \""+isImportant+"\", \"isEmergency\": \""+isEmergency+"\", \"impoLevel\": \""+impoLevel+"\", \"projectGroupId\": \""+projectGroupId+"\", \"projectId\": \""+projectId+"\", \"productId\": \""+productId+"\", \"delFlag\": \""+delFlag+"\", \"status\": \""+status+"\", \"instId\": \""+instId+"\", \"loginIp\": \""+loginIp+"\", \"loginDate\": \""+loginDate+"\", \"createBy\": \""+createBy+"\", \"createDate\": \""+createDate+"\", \"updateBy\": \""+updateBy+"\", \"updateDate\": \""+updateDate+"\", \"remarks\": \""+remarks+"\", \"reportDate\": \""+reportDate+"\"}";
	}

  public  DoufuTomorrowPlan(Map<String, Object> row)throws ParseException{
         if( row.get("id") !=null  ){
          String strValue=row.get("id").toString();
          this.setId(strValue);
        }
         if( row.get("workContents") !=null  ){
          String strValue=row.get("workContents").toString();
          this.setWorkContents(strValue);
        }
         if( row.get("workDetail") !=null  ){
          String strValue=row.get("workDetail").toString();
          this.setWorkDetail(strValue);
        }
         if( row.get("finishPercent") !=null  ){
          String strValue=row.get("finishPercent").toString();
          this.setFinishPercent(strValue);
        }
         if( row.get("isImportant") !=null  ){
          String strValue=row.get("isImportant").toString();
          this.setIsImportant(strValue);
        }
         if( row.get("isEmergency") !=null  ){
          String strValue=row.get("isEmergency").toString();
          this.setIsEmergency(strValue);
        }
         if( row.get("impoLevel") !=null  ){
          String strValue=row.get("impoLevel").toString();
          this.setImpoLevel(strValue);
        }
         if( row.get("projectGroupId") !=null  ){
          String strValue=row.get("projectGroupId").toString();
          this.setProjectGroupId(strValue);
        }
         if( row.get("projectId") !=null  ){
          String strValue=row.get("projectId").toString();
          this.setProjectId(strValue);
        }
         if( row.get("productId") !=null  ){
          String strValue=row.get("productId").toString();
          this.setProductId(strValue);
        }
         if( row.get("delFlag") !=null  ){
          String strValue=row.get("delFlag").toString();
          this.setDelFlag(strValue);
        }
         if( row.get("status") !=null  ){
          String strValue=row.get("status").toString();
          this.setStatus(strValue);
        }
         if( row.get("instId") !=null  ){
          String strValue=row.get("instId").toString();
          this.setInstId(strValue);
        }
         if( row.get("loginIp") !=null  ){
          String strValue=row.get("loginIp").toString();
          this.setLoginIp(strValue);
        }
       /*
         if( row.get("loginDate") !=null  ){
         SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         String strValue=row.get("loginDate").toString();
         this.setLoginDate(sdf.parse(strValue));
        }
       */
         if( row.get("createBy") !=null  ){
          String strValue=row.get("createBy").toString();
          this.setCreateBy(strValue);
        }
       /*
         if( row.get("createDate") !=null  ){
         SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         String strValue=row.get("createDate").toString();
         this.setCreateDate(sdf.parse(strValue));
        }
       */
         if( row.get("updateBy") !=null  ){
          String strValue=row.get("updateBy").toString();
          this.setUpdateBy(strValue);
        }
       /*
         if( row.get("updateDate") !=null  ){
         SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         String strValue=row.get("updateDate").toString();
         this.setUpdateDate(sdf.parse(strValue));
        }
       */
         if( row.get("remarks") !=null  ){
          String strValue=row.get("remarks").toString();
          this.setRemarks(strValue);
        }
         if( row.get("reportDate") !=null  ){
          String strValue=row.get("reportDate").toString();
          this.setReportDate(strValue);
        }
  }

}