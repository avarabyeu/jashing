package com.github.avarabyeu.jashing.core;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import spark.servlet.SparkApplication;
import spark.servlet.SparkFilter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import java.util.List;

/**
 * Servlet Filter. Should be used in case of deployment into application server
 *
 * @author Andrei Varabyeu
 */
abstract public class JashingFilter extends SparkFilter {

    private JashingController jashingController;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Injector injector = Guice.createInjector(new JashingModule(ImmutableList.<Module>builder()
                .addAll(getModules(filterConfig)).build()));
        jashingController = injector.getInstance(JashingController.class);

        super.init(filterConfig);

        /* bootstrap event sources* */
        ServiceManager eventSources = injector.getInstance(ServiceManager.class);
        eventSources.startAsync();

    }

    @Override
    protected SparkApplication getApplication(FilterConfig filterConfig) throws ServletException {
        return jashingController;
    }


    abstract public List<Module> getModules(FilterConfig filterConfig);


}
