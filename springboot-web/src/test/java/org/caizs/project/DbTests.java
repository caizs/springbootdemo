package org.caizs.project;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import org.caizs.project.detail.dao.CityDao;
import org.caizs.project.detail.domain.City;
 

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional()
@Rollback()
public class DbTests {

    @Autowired
    private CityDao cityDao;

    @Test
    public void findList() throws Exception {
        PageHelper.offsetPage(0, 10);
        List<City> list = cityDao.findList();
        Assert.assertTrue(list != null & list.size() > 0);
    }

    @Test
    public void findById() throws Exception {
        City city = cityDao.findById(1);
        assertTrue(city != null);
    }

    @Test
    public void insert() throws Exception {
        City city = new City(0, "name1234", "xx", "xxx", 123);
        cityDao.insert(city);
        cityDao.insert(city);
        assertTrue(city.getId() > 0);
    }

    @Test
    public void update() throws Exception {
        City city = new City(1, "name123", "xx", "xxx", 123);
        cityDao.update(city);
        city = cityDao.findById(1);
        assertTrue("name123".equals(city.getName()));
    }
}
