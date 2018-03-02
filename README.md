# springboot-demo
  - springboot示例工程

## 技术栈
  1. springboot
  2. jdk8
  3. mybatis-plus
  4. caffeine, redis
  5. logback
  6. swagger2
  7. lombok
  7. gradle

## 功能示例说明列表：
  1. spring-cache框架多缓存策略示例，支持3种（根据CacheNameConfig.java路由）
        - caffeine本地缓存，支持redis发布订阅删除
        - redis缓存，支持redis服务异常继续走业务逻辑
        - caffeine + redis 二级缓存，支持redis发布订阅删除一级缓存，redis服务异常继续走业务逻辑
  2. 多数据源使用包名区分, sharding-jdbc读写分离配置
  3. JWT token认证机制示例
  4. elastic job、spring task示例
  5. redis缓存http session
  6. swagger生成接口文档


