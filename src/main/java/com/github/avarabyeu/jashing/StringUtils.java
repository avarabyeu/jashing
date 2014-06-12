package com.github.avarabyeu.jashing;

import com.google.common.base.Strings;

/**
 * Set of usefull stuff for working with strings
 *
 * @author avarabyeu
 */
public class StringUtils {

    /**
     * Returns substring before provided string
     *
     * @param str       String to be truncated
     * @param separator Separator
     * @return Null of initial string is Null, empty if provided string is empty, otherwise substring before
     */
    public static String substringBefore(final String str, final String separator) {
        if (Strings.isNullOrEmpty(str)) {
            return str;
        }
        final int pos = str.indexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }
}
