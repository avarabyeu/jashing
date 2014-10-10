package com.github.avarabyeu.jashing.core;

/**
 * Incorrect configuration exception
 *
 * @author avarabyeu
 */
public class IncorrectConfigurationException extends RuntimeException {

    public IncorrectConfigurationException(String message) {
        super(message);
    }

    public IncorrectConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
