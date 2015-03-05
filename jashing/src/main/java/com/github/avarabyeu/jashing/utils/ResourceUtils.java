package com.github.avarabyeu.jashing.utils;

import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import com.google.common.io.Resources;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * Useful resource utils
 *
 * @author Andrei Varabyeu
 */
public class ResourceUtils {


    public static Properties getResourceAsProperties(final String resource) {
        return getResourceAs(resource, new IOConverter<Properties>() {
            @Override
            public Properties convert(ByteSource source) throws IOException {
                try (InputStream is = source.openStream()) {
                    Properties properties = new Properties();
                    properties.load(is);
                    return properties;
                }
            }
        });
    }

    public static File getResourceAsTempFile(final String resource) {
        return getResourceAs(resource, source -> {
            String fileName = Files.getNameWithoutExtension(resource);
            String extension = Files.getFileExtension(resource);
            File tempFile = File.createTempFile(fileName, "." + extension);
            source.copyTo(Files.asByteSink(tempFile));
            return tempFile;
        });
    }

    public static Source getResourceAsSource(String resource) {
        return getResourceAs(resource, source -> new StreamSource(source.openBufferedStream()));
    }

    public static String getResourceAsString(String resource) {
        return getResourceAs(resource, source -> source.asCharSource(Charset.defaultCharset()).read());
    }

    public static byte[] getResourceAsByteArray(String resource) {
        return getResourceAs(resource, ByteSource::read);
    }

    public static URL getResourceAsURL(String resource) {
        return Resources.getResource(resource);
    }


    public static ByteSource getResourceAsByteSource(String resource) {
        return Resources.asByteSource(getResourceAsURL(resource));
    }


    public static <T> T getResourceAs(String resource, IOConverter<T> converter) {
        try {
            ByteSource source = getResourceAsByteSource(resource);
            return converter.convert(source);
        } catch (IOException e) {
            throw new RuntimeException("Unable to convert resource with name '"
                    + resource + "' using converter " + converter.getClass());
        }
    }

    private interface IOConverter<T> {
        T convert(ByteSource is) throws IOException;

    }

}


