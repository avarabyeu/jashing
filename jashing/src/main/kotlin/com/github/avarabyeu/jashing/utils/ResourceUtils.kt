package com.github.avarabyeu.jashing.utils

import com.google.common.io.ByteSource
import com.google.common.io.Files
import com.google.common.io.Resources
import java.io.File
import java.io.IOException
import java.net.URL
import java.nio.charset.Charset
import java.util.*
import javax.xml.transform.Source
import javax.xml.transform.stream.StreamSource

/**
 * Useful resource utils

 * @author Andrei Varabyeu
 */
object ResourceUtils {

    fun getResourceAsProperties(resource: String): Properties {
        return getResourceAs(resource, { source ->
            source.openStream().use({ `is` ->
                val properties = Properties()
                properties.load(`is`)
                properties
            })
        })
    }

    fun getResourceAsTempFile(resource: String): File {
        return getResourceAs(resource, { source ->
            val fileName = Files.getNameWithoutExtension(resource)
            val extension = Files.getFileExtension(resource)
            val tempFile = File.createTempFile(fileName, "." + extension)
            source.copyTo(Files.asByteSink(tempFile))
            tempFile
        })
    }

    fun getResourceAsSource(resource: String): Source {
        return getResourceAs(resource, { source -> StreamSource(source.openBufferedStream()) })
    }

    fun getResourceAsString(resource: String): String {
        return getResourceAs(resource, { source -> source.asCharSource(Charset.defaultCharset()).read() })
    }


    fun getResourceAsByteArray(resource: String): ByteArray {
        return getResourceAs(resource, { it.read() })
    }

    fun getResourceAsURL(resource: String): URL {
        return Resources.getResource(resource)
    }


    @JvmStatic
    fun getResourceAsByteSource(resource: String): ByteSource {
        return Resources.asByteSource(getResourceAsURL(resource))
    }


    fun <T> getResourceAs(resource: String, converter: (source: ByteSource) -> T): T {
        try {
            val source = getResourceAsByteSource(resource)
            return converter.invoke(source)
        } catch (e: IOException) {
            throw IllegalArgumentException("Unable to convert resource with name '"
                    + resource + "' using converter " + converter.javaClass, e)
        }

    }


}




