package com.github.avarabyeu.jashing.core;

/**
 * Jashing startup parameters bean
 *
 * @author avarabyeu
 */
class BootstrapProperties {
    private int port = 8089;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
