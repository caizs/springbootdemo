package org.caizs.project.web;

import com.github.pagehelper.PageHelper;
import org.caizs.project.common.constants.CodeConstants;
import org.caizs.project.detail.domain.City;
import org.caizs.project.detail.service.CityService;
import org.caizs.project.domain.User;
import org.caizs.project.exception.ProjectException;
import org.caizs.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RefreshScope // 任何方法调用都会重新获取配置，并重新new当前实例
@RequestMapping("/api/test")
public class HelloController {
    @Autowired
    private CityService cityService;

    @Autowired
    private UserService userService;

    @RequestMapping("/hello")
    public @ResponseBody String index() {
        return "Hello 2222";
    }

    @RequestMapping("/errpage")
    public String index(ModelMap map) {
        map.put("errMsg", "xxxx");
        return "/errors/errPage";
    }

    @RequestMapping("/exception")
    public @ResponseBody String exception(ModelMap map) {
        throw new ProjectException(CodeConstants.OBJECT_UNEXIST, "xx");
    }

    @RequestMapping("/dao")
    public @ResponseBody List<City> dao(ModelMap map, @RequestParam(value = "status", required = false) Integer status) {
        PageHelper.offsetPage(0, 10);
        return cityService.findList();
    }

    @GetMapping("/session")
    public @ResponseBody String uid(HttpSession session) {
        UUID uid = (UUID) session.getAttribute("uid");
        if (uid == null) {
            uid = UUID.randomUUID();
        }
        session.setAttribute("uid", uid);
        return uid.toString();
    }

    @RequestMapping("/aop")
    public @ResponseBody void aop() {
        City city = new City(0, "name1111234", "xx", "xxx", 123);
        // cityService.insert_write_transaction(city);
        cityService.get_read_transaction();
        cityService.no_transaction();
    }

    @RequestMapping("/cache")
    public @ResponseBody void cache() {
        User user = new User(2, "name", 17, "address", 1, new Date());
        System.out.println(userService.findById(user.getId()));
        userService.save(user);
        System.out.println(userService.findById(user.getId()));
    }


    @Value("${spring.application.name}")
    private String projectName;

    @RequestMapping("/config")
    public @ResponseBody String from() {
        return this.projectName;
    }

}