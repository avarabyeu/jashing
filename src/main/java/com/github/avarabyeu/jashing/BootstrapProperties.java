package com.github.avarabyeu.jashing;

/**
 * Jashing startup parameters bean
 *
 * @author avarabyeu
 */
public class BootstrapProperties {
    private int port = 8089;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
