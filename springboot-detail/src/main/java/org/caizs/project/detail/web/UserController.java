package org.caizs.project.detail.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import org.caizs.project.detail.dao.CityDao;
import org.caizs.project.detail.domain.City;

@Controller
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private CityDao cityDao;

    @RequestMapping("/dao")
    public @ResponseBody List<City> dao(ModelMap map, @RequestParam(value = "status", required = false) Integer status) {
        PageHelper.offsetPage(0, 10);
        return cityDao.findList();
    }

 


}