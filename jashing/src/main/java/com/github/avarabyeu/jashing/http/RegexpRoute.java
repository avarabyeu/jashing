package com.github.avarabyeu.jashing.http;

import com.google.common.collect.ImmutableMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * {@link Route} based on regular expressions
 * <p>
 * Find '{XXX}' constructions which represents path variables
 * and replaced them with '(?<XXX>.*)' which is named group.
 * After check for match and if TRUE obtains values of path variables
 *
 * @author Andrei Varabyeu
 * @see <a href="http://www.regular-expressions.info/named.html">Named Groups</a>
 */
public class RegexpRoute extends Route {

    private static final Pattern PATH_VARIABLES_PATTERN = Pattern.compile("\\{(.*?)\\}");

    private final Request.Method method;
    private final Pattern pathPattern;
    private final List<String> pathVariableNames;

    public RegexpRoute(Request.Method method, String path) {
        this.method = method;

        /* find path variables */
        Matcher m = PATH_VARIABLES_PATTERN.matcher(path);
        StringBuffer urlPattern = new StringBuffer();
        this.pathVariableNames = new LinkedList<>();
        while (m.find()) {
            /* replace them with named groups and remember variable names */
            String varName = m.group(1);
            pathVariableNames.add(varName);
            m.appendReplacement(urlPattern, "(?<$1>.*?)");
        }
        m.appendTail(urlPattern);

        this.pathPattern = Pattern.compile(urlPattern.toString());
    }

    @Override
    public boolean matches(Request request) {

        /* if incorrect method then just go out */
        if (!method.equals(request.getMethod())) {
            return false;
        }

        Matcher m = pathPattern.matcher(request.getRequestUri());
        boolean matches = m.matches();
        if (matches) {
            Map<String, String> vars = this.pathVariableNames.stream().collect(Collectors.toMap(var -> var, m::group));
            request.setPathVariables(ImmutableMap.<String, String>builder().putAll(vars).build());
        }
        return method.equals(request.getMethod()) && matches;
    }
}
