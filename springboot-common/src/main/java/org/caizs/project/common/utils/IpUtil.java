package org.caizs.project.common.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpUtil {

  private static final Pattern IS_LICIT_IP_PATTERN = Pattern
      .compile("^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$");

  private static final String HEADER_PRAGMA = "Pragma";

  private static final String HEADER_EXPIRES = "Expires";

  private static final String HEADER_CACHE_CONTROL = "Cache-Control";


  /**
   * 取ip地址
   */
  public static String getClientIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Real-IP");
    if (StringUtils.isEmpty(ip)) {
      return request.getRemoteAddr();
    }
    int index = ip.indexOf(',');
    String clientIp = ip;
    if (index > 0) {
      clientIp = ip.substring(0, index).trim();
    }
    if ("127.0.0.1".equals(clientIp) || !isLicitIp(clientIp)) {
      return request.getRemoteAddr();
    }
    return clientIp;
  }

  /**
   * 是否合法ip
   */
  private static boolean isLicitIp(final String ip) {
    if (StringUtils.isEmpty(ip)) {
      return Boolean.FALSE;
    }

    Matcher m = IS_LICIT_IP_PATTERN.matcher(ip);
    if (!m.find()) {
      return Boolean.FALSE;
    }
    return Boolean.TRUE;
  }


}
