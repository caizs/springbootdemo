package org.caizs.project.task;


import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

public class ElasticDemoJob implements SimpleJob {

  @Override
  public void execute(ShardingContext context) {
    System.out.println(" job名称:" + context.getJobName()
        + ",分片数量:" + context.getShardingTotalCount()
        + ",当前分区:" + context.getShardingItem()
        + ",当前分区名称:" + context.getShardingParameter()
        + ",当前自定义参数:" + context.getJobParameter());
  }

}