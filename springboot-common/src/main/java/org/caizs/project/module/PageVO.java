package org.caizs.project.module;

import lombok.Data;

import java.util.Collection;

@Data
public class PageVO<E> {

  /**
   * 总条数
   */
  private int total;

  /**
   * 查询结果
   */
  private Collection<E> rows;

  public PageVO() {
  }

  public PageVO(Collection<E> rows, int total) {
    this.rows = rows;
    this.total = total;
  }

}