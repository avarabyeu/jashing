package com.github.avarabyeu.jashing.utils

import com.google.common.base.Strings

/**
 * Set of useful stuff for working with strings

 * @author avarabyeu
 */
object StringUtils {

    /**
     * Returns substring before provided string

     * @param str       String to be truncated
     * *
     * @param separator Separator
     * *
     * @return Null of initial string is Null, empty if provided string is empty, otherwise substring before
     */
    @JvmStatic
    fun substringBefore(str: String, separator: String): String {
        if (Strings.isNullOrEmpty(str)) {
            return str
        }
        val pos = str.indexOf(separator)
        if (pos == -1) {
            return str
        }
        return str.substring(0, pos)
    }
}
