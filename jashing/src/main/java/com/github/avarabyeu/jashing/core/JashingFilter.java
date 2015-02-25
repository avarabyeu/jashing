package com.github.avarabyeu.jashing.core;

import com.google.inject.Module;
import spark.servlet.SparkApplication;
import spark.servlet.SparkFilter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import java.util.List;

/**
 * Servlet Filter. Should be used in case of deployment into application server
 *
 * @author Andrei Varabyeu
 */
abstract public class JashingFilter extends SparkFilter implements ServletContextListener {

    private Jashing jashing;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Jashing jashing = Jashing.newOne().registerModule(getModules(filterConfig)).build();
        jashing.bootstrap();
        this.jashing = jashing;

        super.init(filterConfig);
    }

    @Override
    protected SparkApplication getApplication(FilterConfig filterConfig) throws ServletException {
        return jashing.getController();
    }


    abstract public List<Module> getModules(FilterConfig filterConfig);

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        jashing.shutdown();
    }
}
