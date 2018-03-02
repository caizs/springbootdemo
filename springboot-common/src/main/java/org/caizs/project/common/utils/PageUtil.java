package org.caizs.project.common.utils;

import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.collections4.MapUtils;

import java.util.List;
import java.util.Map;

public class PageUtil {

  /**
   * 取mybatis-plus分页对象
   */
  public static Page getPage(Map<String, String> params) {
    int page = MapUtils.getInteger(params, "page", 1);
    int pageSize = MapUtils.getInteger(params, "pageSize", 20);
    return new Page(page, pageSize);
  }

  /**
   * 内存分页
   */
  public static <T> List<T> page(List<T> source, int page, int size) {
    if (page <= 0) {
      page = 1;
    }
    int from = (page - 1) * size;
    int to = page * size;
    if (to > source.size()) {
      to = source.size();
    }
    List<T> ts = source.subList(from, to);

    return ts;
  }
}
