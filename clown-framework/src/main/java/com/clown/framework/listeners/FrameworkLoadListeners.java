package com.clown.framework.listeners;

import com.clown.framework.configurations.ClownContextPropertiesConstant;
import com.clown.framework.configurations.PropertiesConfiguration;
import com.clown.framework.filters.CrossDomainFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;

/**
 * Created by len.li on 2016/4/12.
 * 容器启动监听入口类,在web.xml上配置
 */
public class FrameworkLoadListeners implements ServletContextListener {


    protected final Logger logger = LoggerFactory.getLogger(FrameworkLoadListeners.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        logger.info(PropertiesConfiguration.getDisconfScanpackage());
        // spring characterEncodingFilter 字符过滤UTF-8编码
        ServletContext servletContext = sce.getServletContext();
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        servletContext.addFilter("encodingFilter",characterEncodingFilter).addMappingForUrlPatterns(null,false,"/*");

        // 开启跨域请求添加跨域过滤器
        if(PropertiesConfiguration.getCrossDomain()){
            logger.info("开启跨域请求.");
            servletContext.addFilter("crossDomainFilter",new CrossDomainFilter(PropertiesConfiguration.findPropertieValue(ClownContextPropertiesConstant.CLOWN_CROSS_ALLOWORIGIN,"*"),
                    PropertiesConfiguration.findPropertieValue(ClownContextPropertiesConstant.CLOWN_CROSS_ALLOWHEADERS,"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With"),
                    PropertiesConfiguration.findPropertieValue(ClownContextPropertiesConstant.CLOWN_CROSS_ALLOWMETHODS,"GET,POST,OPTIONS"),
                    PropertiesConfiguration.findPropertieValue(ClownContextPropertiesConstant.CLOWN_CROSS_MAXAGE,"3628800"))).addMappingForUrlPatterns(null,false,
                    PropertiesConfiguration.findPropertieValue(ClownContextPropertiesConstant.CLOWN_CROSSDOMAIN_FILTERMAPPING,"/api/**"));
        }

        // 启用spring mvc 执行
        logger.info("start spring mvc");
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(false);
        dispatcherServlet.setContextConfigLocation("classpath*:spring/spring-*.xml");
        ServletRegistration.Dynamic servletRegistration = servletContext.addServlet(PropertiesConfiguration.findPropertieValue(ClownContextPropertiesConstant.CLOWN_APPLICATION_NAME,"welcome use framework!")
                ,dispatcherServlet);
        servletRegistration.setLoadOnStartup(1);
        servletRegistration.addMapping("/");

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
