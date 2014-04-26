package com.github.avarabyeu.jashing;

import com.google.common.base.Strings;

/**
 * Created by andrey.vorobyov on 25/04/14.
 */
public class StringUtils {

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
