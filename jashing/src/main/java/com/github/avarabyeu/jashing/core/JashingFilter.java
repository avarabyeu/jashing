package com.github.avarabyeu.jashing.core;

import com.google.inject.Module;
import spark.servlet.SparkApplication;
import spark.servlet.SparkFilter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

/**
 * Servlet Filter. Should be used in case of deployment into application server
 *
 * @author Andrei Varabyeu
 */
abstract public class JashingFilter extends SparkFilter {

    private Jashing jashing;

    @Override
    protected SparkApplication getApplication(FilterConfig filterConfig) throws ServletException {
        return jashing.getController();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Jashing jashing = Jashing.newOne().registerModule(getModules()).build(Jashing.Mode.CONTAINER);
        jashing.bootstrap();
        this.jashing = jashing;

        super.init(filterConfig);
    }

    @Override
    public void destroy() {
        jashing.shutdown();
        super.destroy();
    }

    abstract protected Module[] getModules();

}
