package com.github.avarabyeu.jashing.core;

import com.google.common.util.concurrent.AbstractIdleService;
import spark.Spark;

/**
 * HTTP Server Controller. Bootstraps server and specifies all needed mappings and request handlers
 *
 * @author avarabyeu
 */
class JashingServer extends AbstractIdleService {


    /* 4567 is default Spart port */
    private final Integer port;

    private final JashingController controller;

    public JashingServer(Integer port, JashingController controller) {
        this.port = port;
        this.controller = controller;
    }

    @Override
    protected void startUp() throws Exception {
        if (null != port && 0 != port) {
            Spark.port(port);
        }
        controller.init();


    }


    @Override
    protected void shutDown() throws Exception {
        /* do nothing */
    }
}
