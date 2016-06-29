package com.ucsmy.collection.util;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by yuananyun on 2016/5/1.
 */
public class UrlUtils {

    private static Pattern patternForProtocal = Pattern.compile("[\\w]+://");

    public static String getContextPath(String url) {
        if (StringUtils.isBlank(url)) return url;

        int index = url.indexOf("?");
        return index > 0 ? url.substring(0, index) : url;
    }

    public static String getDomain(String url) {
        try {
            String domain = removeProtocol(url);
            int i = StringUtils.indexOf(domain, "/", 1);
            if (i > 0) {
                domain = StringUtils.substring(domain, 0, i);
            }
            return domain;
        } catch (Exception ex) {
            return null;
        }
    }

    public static String removeProtocol(String url) {
        return patternForProtocal.matcher(url).replaceAll("");
    }


}
