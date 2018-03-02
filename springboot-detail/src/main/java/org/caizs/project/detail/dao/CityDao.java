package org.caizs.project.detail.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import org.caizs.project.detail.domain.City;

@Mapper //每个dao接口都需要加
public interface CityDao {
    //通过xml文件声明sql
    List<City> findList();
    
    //还可以直接通过注解形式
    @Select("SELECT * FROM city WHERE ID = #{id}")
    City findById(@Param("id") Integer id);
   
    @Insert("INSERT INTO city(Name, CountryCode, District, Population) VALUES(#{name}, #{countryCode}, #{district}, #{population})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(City City);
   
    @Update("UPDATE city SET Name=#{name} WHERE id=#{id}")
    void update(City City);
    
}
