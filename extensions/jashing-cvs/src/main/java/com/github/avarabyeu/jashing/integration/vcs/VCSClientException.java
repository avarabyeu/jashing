package com.github.avarabyeu.jashing.integration.vcs;

/**
 * VCS clients exceptions
 *
 * @author Andrey Vorobyov
 */
public class VCSClientException extends RuntimeException {

    public VCSClientException(String message) {
        super(message);
    }

    public VCSClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
