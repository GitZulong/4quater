package com.boot.web.codegen.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.boot.util.BaseController;
import com.boot.util.CommonEntity;
import com.boot.util.Result;
import com.boot.util.beetl.function.DictFunction;
import com.krm.common.constant.Constant;
import com.boot.util.SpringContextHolder;
import com.boot.util.StringConvert;
import com.boot.util.StringUtil;
import com.boot.web.codegen.model.TableConfig;
import com.boot.web.codegen.model.TableFieldConfig;
import com.boot.web.codegen.service.DbManagService;
import com.boot.web.codegen.service.GenDictService;
import com.boot.web.codegen.service.ProjectsService;
import com.boot.web.codegen.service.TableConfigService;
import com.boot.web.codegen.service.TableFieldConfigService;
import com.boot.web.codegen.util.ConnectionUtil;
import com.boot.web.codegen.util.read.IReadTable;
import com.boot.web.codegen.util.read.ReadTableFactory;
import com.boot.util.SysUserUtils;

@Controller
@RequestMapping("form/config")
@DependsOn("springContextHolder")
public class FormConfigController extends BaseController {

    @Override
    protected String getBaseUrl() {
        return null;
    }

    @Override
    protected String getBasePath() {
        return "codegen";
    }

    static DictFunction dictFunction = SpringContextHolder.getBean("dictFunction");
    @Resource
    private TableConfigService tableConfigService;
    @Resource
    private TableFieldConfigService tableFieldConfigService;
    @Resource
    private BeetlGroupUtilConfiguration beetlConfig;
    @Resource
    private DbManagService dbManagService;
    @Resource
    private ProjectsService projectsService;
    @Resource
    private GenDictService genDictService;

    /**
     * ????????????????????????
     *
     * @param model
     * @return
     */
    @RequestMapping
    public String index(Model model) {
        logger.info("????????????????????????");
        return "codegen/form-config";
    }

    /**
     * ????????????????????????
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "getSchema", method = RequestMethod.POST)
    @ResponseBody
    public List<CommonEntity> getSchema(@RequestParam Map<String, Object> params) {
        logger.info("??????????????????????????????");
        try {
            if (StringUtil.isEmpty(params.get("dbId"))) {
                return Lists.newArrayList();
            }
            params.put("id", params.get("dbId"));
            CommonEntity entity = dbManagService.queryOneCommon(params);
            String dbAddress = ConnectionUtil.getRealUrl(entity.getString("dbType"), entity.getString("dbAddress"), entity.getString("dbPort"), entity.getString("tbSchema"));
            ConnectionUtil.init(entity.getString("driver"), dbAddress, entity.getString("userName"),
                    entity.getString("password"));
            IReadTable readTable = ReadTableFactory.getReadTable(entity.getString("dbType"));
            return readTable.getAllSchema();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                ConnectionUtil.close();
            } catch (Exception e2) {
                throw e2;
            }
        }
    }

    /**
     * ???????????????????????????
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "getDbTable", method = RequestMethod.POST)
    @ResponseBody
    public List<CommonEntity> getDbTable(@RequestParam Map<String, Object> params) {
        logger.info("???????????????????????????");
        try {
            params.put("id", params.get("dbId"));
            CommonEntity entity = dbManagService.queryOneCommon(params);
            String dbAddress = ConnectionUtil.getRealUrl(entity.getString("dbType"), entity.getString("dbAddress"), entity.getString("dbPort"), entity.getString("tbSchema"));
            ConnectionUtil.init(entity.getString("driver"), dbAddress, entity.getString("userName"),
                    entity.getString("password"));
            IReadTable readTable = ReadTableFactory.getReadTable(entity.getString("dbType"));
            return readTable.getAllTable(params.get("schemaName").toString());
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                ConnectionUtil.close();
            } catch (Exception e2) {
                throw e2;
            }
        }
    }


    /**
     * ????????????????????????????????????
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "checkConfigs", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> checkConfigs(@RequestParam Map<String, Object> params) {
        logger.info("??????????????????????????????????????????");
        params.put("createBy", SysUserUtils.getCacheLoginUser().getId());
        /* ????????????????????????????????????????????? */
//		if(!SysUserUtils.getCacheLoginUser().isAdmin()){
//		}
        return tableFieldConfigService.checkConfigs(params);
    }

    /**
     * ??????????????????
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "initField", method = RequestMethod.POST)
    @ResponseBody
    public List<TableFieldConfig> initField(@RequestParam Map<String, Object> params) {
        String tableName = params.get("tableName").toString();
        logger.info("?????????{}??????????????????", tableName);
        try {
            params.put("id", params.get("dbId"));
            CommonEntity entity = dbManagService.queryOneCommon(params);
            String dbAddress = ConnectionUtil.getRealUrl(entity.getString("dbType"), entity.getString("dbAddress"), entity.getString("dbPort"), entity.getString("tbSchema"));
            ConnectionUtil.init(entity.getString("driver"), dbAddress, entity.getString("userName"),
                    entity.getString("password"));
            IReadTable readTable = ReadTableFactory.getReadTable(entity.getString("dbType"));
            List<TableFieldConfig> list = readTable.initField(params.get("schemaName").toString(), tableName);
            for (int i = 0; i < list.size(); i++) {
                TableFieldConfig temp = list.get(i);
                temp.setSorts(i + 1);
            }
            return list;
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                ConnectionUtil.close();
            } catch (Exception e2) {
                throw e2;
            }
        }
    }


    /**
     * ???????????????????????????
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "getTableFieldConfig", method = RequestMethod.POST)
    @ResponseBody
    public List<CommonEntity> getTableFieldConfig(@RequestParam Map<String, Object> params) {
        logger.info("???????????????????????????");
        params.put("createBy", SysUserUtils.getCacheLoginUser().getId());
        /* ????????????????????????????????????????????? */
//		if(!SysUserUtils.getCacheLoginUser().isAdmin()){
//		}
        List<CommonEntity> list = tableFieldConfigService.selectConfigs(params);
        for (int i = 0; i < list.size(); i++) {
            CommonEntity entity = list.get(i);
            entity.put("rowId", i);
        }
        return list;
    }

    /**
     * ???????????????
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "getTableConfig", method = RequestMethod.POST)
    @ResponseBody
    public TableConfig getTableConfig(@RequestParam Map<String, Object> params) {
        logger.info("???????????????????????????");
        params.put("createBy", SysUserUtils.getCacheLoginUser().getId());
        /* ????????????????????????????????????????????? */
//		if(!SysUserUtils.getCacheLoginUser().isAdmin()){
//		}
        return tableConfigService.queryOne(params);
    }

    /**
     * ??????????????????????????????
     *
     * @param tableConfigs
     * @param params
     * @return
     */
    @RequestMapping(value = "saveConfigs", method = RequestMethod.POST)
    @ResponseBody
    public Result saveConfigs(@ModelAttribute TableConfig tableConfigs, @RequestParam Map<String, Object> params) {
        logger.info("????????????????????????????????????");
        Map<String, Object> newParams = Maps.newHashMap();
        newParams.put("proId", params.get("proId"));
        newParams.put("dbId", params.get("dbId"));
        newParams.put("schemaName", params.get("schemaName"));
        newParams.put("tableName", params.get("tableName").toString().toLowerCase());
        newParams.put("createBy", SysUserUtils.getCacheLoginUser().getId());
        /* ????????????????????????????????????????????? */
//		if(!SysUserUtils.getCacheLoginUser().isAdmin()){
//		}
        // ?????????????????????????????????????????????
        List<TableConfig> tableList = tableConfigService.entityList(newParams);
        tableConfigService.deleteByParams(newParams); // ?????????????????????
        // ????????????????????????????????????????????????
        for (TableConfig entity : tableList) {
            newParams.clear();
            newParams.put("tableId", entity.getId());
            tableFieldConfigService.deleteByParams(newParams);
        }
        String tableName = params.get("tableName").toString().toLowerCase();
        String remarks = params.get("remarks").toString().toLowerCase();
        try {
            remarks = remarks.substring(remarks.indexOf("???") + 1, remarks.indexOf("???"));
        } catch (Exception e) {
            remarks = tableName;
        }
        tableConfigs.setClassName(StringConvert.underlineToCamelhump(tableName)); // ?????????Java??????
        if (tableConfigs.getConfigs() == null || tableConfigs.getConfigs().size() == 0) {
            return Result.errorResult();
        }
        String tableId = tableConfigService.generateId();
        tableConfigs.setId(tableId);
        tableConfigs.setTableName(tableName);
        tableConfigs.setRemarks(remarks);
        tableConfigs.setModuleName(
                StringUtil.isEmpty(tableConfigs.getModuleName()) ? "modules" : tableConfigs.getModuleName());
        tableConfigService.insertSelective(tableConfigs);
        List<TableFieldConfig> list = tableConfigs.getConfigs();
        for (TableFieldConfig entity : list) {
            entity.setId(tableFieldConfigService.generateId());
            entity.setTableId(tableId);
            entity.setCreateBy(SysUserUtils.getCacheLoginUser().getId());
            entity.setCreateDate(new Date());
            entity.setDelFlag(Constant.DEL_FLAG_NORMAL);
        }
        if (tableFieldConfigService.insertBatch(list) > 0) {
            return Result.successResult();
        }
        return Result.errorResult();
    }

    /**
     * ??????????????????????????????
     *
     * @param tableConfigs
     * @param params
     * @return
     */
    @RequestMapping(value = "deleteConfig", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteConfig(@RequestParam Map<String, Object> params) {
        logger.info("????????????????????????????????????");
        Map<String, Object> newParams = Maps.newHashMap();
        newParams.put("proId", params.get("proId"));
        newParams.put("dbId", params.get("dbId"));
        newParams.put("schemaName", params.get("schemaName"));
        newParams.put("tableName", params.get("tableName"));
        newParams.put("createBy", SysUserUtils.getCacheLoginUser().getId());
        /* ????????????????????????????????????????????? */
//		if(!SysUserUtils.getCacheLoginUser().isAdmin()){
//		}
        //?????????????????????????????????????????????
        List<TableConfig> tableList = tableConfigService.entityList(newParams);
        if (tableList.size() > 0) {
            tableConfigService.deleteByParams(newParams); // ?????????????????????
            // ????????????????????????????????????????????????
            for (TableConfig entity : tableList) {
                newParams.clear();
                newParams.put("tableId", entity.getId());
                tableFieldConfigService.deleteByParams(newParams);
            }
            return Result.successResult();
        }
        return new Result(0, "?????????????????????????????????????????????");
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param tableConfigs
     * @param params
     * @return
     */
    @RequestMapping(value = "sync", method = RequestMethod.POST)
    @ResponseBody
    public Result sync(@RequestParam Map<String, Object> params) {
        String tableName = params.get("tableName").toString();
        if (tableName != null && !tableName.equals("")) {
            logger.info("??????????????????{}????????????", tableName);
            Map<String, Object> newParams = Maps.newHashMap();
            newParams.put("proId", params.get("proId"));
            newParams.put("dbId", params.get("dbId"));
            newParams.put("schemaName", params.get("schemaName"));
            newParams.put("tableName", params.get("tableName"));
            newParams.put("createBy", SysUserUtils.getCacheLoginUser().getId());
            /* ????????????????????????????????????????????? */
//			if(!SysUserUtils.getCacheLoginUser().isAdmin()){
//			}
            //?????????????????????????????????????????????
            List<TableConfig> tableList = tableConfigService.entityList(newParams);
            if (tableList.size() > 0) {
                for (TableConfig entity : tableList) {
                    TableFieldConfig temp = new TableFieldConfig(null, null, null, null, null, null);
                    temp.setTableId(entity.getId());
                    List<TableFieldConfig> savedConfigList = tableFieldConfigService.select(temp, "sorts");    //????????????????????????
                    if (savedConfigList.size() > 0) {
                        List<TableFieldConfig> dbConfigList = new ArrayList<TableFieldConfig>();    //??????????????????????????????
                        try {
                            newParams.clear();
                            newParams.put("id", params.get("dbId"));
                            CommonEntity dbManag = dbManagService.queryOneCommon(newParams);
                            String dbAddress = ConnectionUtil.getRealUrl(dbManag.getString("dbType"), dbManag.getString("dbAddress"), dbManag.getString("dbPort"), dbManag.getString("tbSchema"));
                            ConnectionUtil.init(dbManag.getString("driver"), dbAddress, dbManag.getString("userName"),
                                    dbManag.getString("password"));
                            IReadTable readTable = ReadTableFactory.getReadTable(dbManag.getString("dbType"));
                            dbConfigList = readTable.initField(params.get("schemaName").toString(), tableName);
                        } catch (Exception e) {
                            throw e;
                        } finally {
                            try {
                                ConnectionUtil.close();
                            } catch (Exception e2) {
                                throw e2;
                            }
                        }
                        //?????????????????????????????????????????????
                        for (Iterator<TableFieldConfig> iterator = dbConfigList.iterator(); iterator.hasNext(); ) {
                            TableFieldConfig dbConfig = iterator.next();
                            boolean flag = true;
                            TableFieldConfig newConfig = null;
                            for (TableFieldConfig config : savedConfigList) {
                                if (config.getFieldName().equals(dbConfig.getFieldName())) {
                                    flag = false;
                                    break;
                                } else {
                                    newConfig = dbConfig;    //???????????????????????????????????????flag???true
                                }
                            }
                            if (flag) {
                                savedConfigList.add(newConfig);
                            }
                        }
                        //???????????????????????????
                        for (Iterator<TableFieldConfig> iterator = savedConfigList.iterator(); iterator.hasNext(); ) {
                            TableFieldConfig config = iterator.next();
                            boolean flag = true;
                            for (TableFieldConfig dbConfig : dbConfigList) {
                                if (config.getFieldName().equals(dbConfig.getFieldName())) {    //????????????????????????
                                    if (StringUtil.isEmpty(config.getRemarks()) || !config.getRemarks().equals(dbConfig.getRemarks())) {
                                        config.setRemarks(dbConfig.getRemarks());
                                    }
                                    if (StringUtil.isEmpty(config.getIsPrimary()) || !config.getIsPrimary().equals(dbConfig.getIsPrimary())) {
                                        config.setIsPrimary(dbConfig.getIsPrimary());
                                    }
                                    if (StringUtil.isEmpty(config.getFieldType()) || !config.getFieldType().equals(dbConfig.getFieldType())) {
                                        config.setFieldType(dbConfig.getFieldType());
                                    }
                                    if (StringUtil.isEmpty(config.getJavaType()) || !config.getJavaType().equals(dbConfig.getJavaType())) {
                                        config.setJavaType(dbConfig.getJavaType());
                                    }
                                    if (StringUtil.isEmpty(config.getLength()) || !config.getLength().equals(dbConfig.getLength())) {
                                        config.setLength(dbConfig.getLength());
                                    }
                                    if (StringUtil.isEmpty(config.getDecimalPrecision()) || !config.getDecimalPrecision().equals(dbConfig.getDecimalPrecision())) {
                                        config.setDecimalPrecision(dbConfig.getDecimalPrecision());
                                    }
                                    if (StringUtil.isEmpty(config.getDefaultValue()) || !config.getDefaultValue().equals(dbConfig.getDefaultValue())) {
                                        config.setDefaultValue(dbConfig.getDefaultValue());
                                    }
                                    flag = false;
                                    break;
                                }
                            }
                            if (flag) {
                                iterator.remove();
                            }
                        }
                        logger.info("????????????!");
                        return new Result(1, "????????????!", savedConfigList);
                    }
                }
            } else {
                return new Result(0, "?????????????????????");
            }
        }
        return new Result(0, "????????????????????????");
    }


    @RequestMapping(value = "toRelation", method = RequestMethod.GET)
    public String toRelation(@RequestParam Map<String, Object> params) {
        return getBasePath() + "relation";
    }

    @RequestMapping(value = "getDicts", method = RequestMethod.POST)
    @ResponseBody
    public Result getDicts(@RequestParam Map<String, Object> params) {
        List<CommonEntity> dicts = genDictService.getDicts(params);
        return Result.successResult(dicts);
    }
}
