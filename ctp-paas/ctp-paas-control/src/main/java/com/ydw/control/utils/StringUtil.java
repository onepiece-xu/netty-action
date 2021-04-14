package com.ydw.control.utils;

import org.apache.commons.lang3.StringUtils;

public class StringUtil extends StringUtils {

    /**
     *
    * @Title: isIP
    * @Description: 判断输入字符船是否符合IP规则
    * @param @param ipStr
    * @param @return    参数
    * @return Boolean    返回类型
    * @throws
     */
    public static Boolean isIP(String ipStr) {
        ipStr = ipStr.trim();
        String ipRegex = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        return ipStr.matches(ipRegex);
    }

}
