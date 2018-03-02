package org.caizs.project.detail.service;

import java.util.List;

import org.caizs.project.detail.dao.CityDao;
import org.caizs.project.detail.domain.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service("cityService")
@CacheConfig(cacheNames = {"city"}) //声明缓存名称；只对内部有声明缓存注解的方法生效；可被方法级别声明覆盖
public class CityService {

    @Autowired
    private CityDao cityDao;
    
    @Cacheable(key = "#id")  
    public List<City> findById(Integer id){
        System.out.println("没有缓存，查询数据库");
        return cityDao.findList();
    }
    
    public int insert_write_transaction(City City) {
        System.out.println("断点查看是否被代理，事务传播属性，以及是否回滚;");
        cityDao.insert(City);
        int a = 1 / 0;
        return a;
    }
    
    public int get_read_transaction() {
        System.out.println("断点查看是否被代理，事务传播属性，以及是否回滚");
        cityDao.findById(1);
        return 1;
    }
    
    public int no_transaction(){
        System.out.println("断点查看是否被代理，事务传播属性，以及是否回滚");
        return 1;
    }

    public List<City> findList(){
        return cityDao.findList();
    }
    

}
