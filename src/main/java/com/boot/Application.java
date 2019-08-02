package com.boot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Entity;
import javax.sql.DataSource;

import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.beetl.sql.core.ClasspathLoader;
import org.beetl.sql.core.Interceptor;
import org.beetl.sql.core.UnderlinedNameConversion;
import org.beetl.sql.core.db.MySqlStyle;
import org.beetl.sql.ext.DebugInterceptor;
import org.beetl.sql.ext.spring4.BeetlSqlDataSource;
import org.beetl.sql.ext.spring4.BeetlSqlScannerConfigurer;
import org.beetl.sql.ext.spring4.SqlManagerFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import com.boot.util.ConfigUtil;
import com.boot.util.CurrentWeek;

@SpringBootApplication(scanBasePackages ={"com.boot", "com.krm"})
@MapperScan({"com.boot.web.*","com.boot.util.*"})
@tk.mybatis.spring.annotation.MapperScan(basePackages = "com.boot.web.*")
public class Application implements InitializingBean {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		// TODO Auto-generated method stub
		logger.info(ConfigUtil.getValue("db.schema"));
		String sql = "SELECT id FROM " + ConfigUtil.getValue("db.schema")
				+ "t_happy_work_title WHERE begindate=? AND enddate=? ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,
				new Object[] { CurrentWeek.getCurrenproDay("yyyy-MM-dd"), CurrentWeek.getCurrenaftDay("yyyy-MM-dd") });
		if (list.size() == 0) {
			sql = "INSERT INTO t_happy_work_title (id,work_title,begindate,enddate) VALUES (?,?,?,?)";
			String id = UUID.randomUUID().toString();
			Date begindate = (Date) CurrentWeek.getCurrenproDate();
			Date enddate = (Date) CurrentWeek.getCurrenaftDate();
			Calendar calendar = Calendar.getInstance();
			String work_title = calendar.get(Calendar.YEAR) + "年" + CurrentWeek.getMonth() + "月第"
					+ CurrentWeek.getWeekOfMonth() + "周";

			logger.info("CurrentWeek.getCurrenproDate()" + CurrentWeek.getCurrenproDate().toString());

			jdbcTemplate.update(sql, new Object[] { id, work_title, begindate, enddate });

		}

	}

}