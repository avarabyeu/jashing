/*
 * Copyright (c) 2008. All rights reserved.
 */
package com.github.avarabyeu.jashing.core;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.Validate;
import ro.isdc.wro.model.resource.locator.UriLocator;
import ro.isdc.wro.model.resource.locator.support.LocatorProvider;
import ro.isdc.wro.model.resource.locator.wildcard.DefaultWildcardStreamLocator;
import ro.isdc.wro.model.resource.locator.wildcard.JarWildcardStreamLocator;
import ro.isdc.wro.model.resource.locator.wildcard.WildcardStreamLocator;
import ro.isdc.wro.model.resource.locator.wildcard.WildcardUriLocatorSupport;
import ro.isdc.wro.model.transformer.WildcardExpanderModelTransformer.NoMoreAttemptsIOException;
import ro.isdc.wro.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;

import static java.lang.String.format;

/**
 * Implementation of the {@link UriLocator} that is able to read a resource from a classpath.
 *
 * @author Alex Objelean
 * @created Created on Nov 6, 2008
 */
public class MultipleClasspathUrlLocator
        extends WildcardUriLocatorSupport {

    /**
     * Alias used to register this locator with {@link LocatorProvider}.
     */
    public static final String ALIAS = "multipleclasspath";
    /**
     * Prefix of the resource uri used to check if the resource can be read by this {@link UriLocator} implementation.
     */
    public static final String PREFIX = format("%s:", ALIAS);

    /**
     * {@inheritDoc}
     */
    public boolean accept(final String url) {
        return isValid(url);
    }

    /**
     * Check if a uri is a classpath resource.
     *
     * @param uri to check.
     * @return true if the uri is a classpath resource.
     */
    public static boolean isValid(final String uri) {
        return uri.trim().startsWith(PREFIX);
    }

    /**
     * {@inheritDoc}
     */
    public InputStream locate(final String uri)
            throws IOException {
        Validate.notNull(uri, "URI cannot be NULL!");
        // replace prefix & clean path by removing '..' characters if exists and
        // normalizing the location to use.
        String location = StringUtils.cleanPath(uri.replaceFirst(PREFIX, "")).trim();

        if (getWildcardStreamLocator().hasWildcard(location)) {
            try {
                return locateWildcardStream(uri, location);
            } catch (final IOException e) {
                if (location.contains("?")) {
                    location = DefaultWildcardStreamLocator.stripQueryPath(location);
                }
            }
        }
        Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(location);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            IOUtils.copy(url.openStream(), out);

        }

        return new BufferedInputStream(new ByteArrayInputStream(out.toByteArray()));
    }

    /**
     * @return an input stream for an uri containing a wildcard for a given location.
     */
    private InputStream locateWildcardStream(final String uri, final String location)
            throws IOException {
        // prefix with '/' because we use class relative resource retrieval. Using ClassLoader.getSystemResource doesn't
        // work well.
        final String fullPath = "/" + FilenameUtils.getFullPathNoEndSeparator(location);
        Enumeration<URL> urls = getClass().getClassLoader().getResources(fullPath);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            try {
                IOUtils.copy(locateWildcardStream(uri, url), out);
            } catch (final IOException e) {
                //do not attempt unless exception is of this type
                if (e instanceof NoMoreAttemptsIOException) {
                    throw e;
                }
                // try once more, in order to treat classpath resources located in the currently built project.
                url = getClass().getResource("");
                return locateWildcardStream(uri, url);
            }
        }
        return new BufferedInputStream(new ByteArrayInputStream(out.toByteArray()));

    }

    private InputStream locateWildcardStream(final String uri, final URL url)
            throws IOException {
        if (url == null) {
            throw new IOException("Cannot locate stream for null URL");
        }
        return getWildcardStreamLocator().locateStream(uri, new File(URLDecoder.decode(url.getFile(), "UTF-8")));
    }

    /**
     * Builds a {@link JarWildcardStreamLocator} in order to get resources from the full classpath.
     */
    @Override
    public WildcardStreamLocator newWildcardStreamLocator() {
        return new JarWildcardStreamLocator() {
            @Override
            public boolean hasWildcard(final String uri) {
                return isEnableWildcards() && super.hasWildcard(uri);
            }
        };
    }
}
