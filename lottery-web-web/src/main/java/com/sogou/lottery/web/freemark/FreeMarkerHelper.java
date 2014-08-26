package com.sogou.lottery.web.freemark;

import java.io.StringWriter;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.netease.common.util.PropertyUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

@Component("freeMarkerHelper")
public class FreeMarkerHelper implements ServletContextAware {

	private static Logger logger = Logger.getLogger("error");
	private ServletContext context;

	private Configuration cfg;
	
	@PostConstruct
	private void ini() {
		String ftlPath = PropertyUtil.getProperty("freemarker.ftl.path");
		if (StringUtils.isBlank(ftlPath)) {
			ftlPath = "/WEB-INF/ftl/template/";
		}
		if (!ftlPath.endsWith("/")) {
			ftlPath += "/";
		}
		cfg = new Configuration();
		cfg.setServletContextForTemplateLoading(context, ftlPath);
		cfg.setObjectWrapper(new DefaultObjectWrapper());
	}

	public String makeHtml(String templateFileName, Map<String, Object> data) {
		StringWriter out = new StringWriter();
		if (!templateFileName.endsWith(".ftl")) {
			templateFileName += ".ftl";
		}
		try {
			Template temp = cfg.getTemplate(templateFileName, "utf-8");
			
			temp.process(data, out);
		} catch (Exception e) {
			logger.error("error occured when make html file. ", e);
		} 
		
		return out.toString();
	}
	
	@Override
	public void setServletContext(ServletContext arg0) {
		this.context = arg0;
	}
}
