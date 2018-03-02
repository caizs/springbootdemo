package org.caizs.project.web;

import org.caizs.project.common.utils.Json;
import org.caizs.project.domain.User;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @ClassName: SwaggerDemoController
 * @Description: rest定义即生成文档，文档地址： http://localhost:8080/swagger-ui.html
 *               主要用到注解 @ApiOperation@ApiParam(@ApiImplicitParam
 *               和 @ApiImplicitParams需要更多声明属性）
 *               参考：https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#data-types
 *               http://www.jianshu.com/p/b730b969b6a2
 */
@Controller
@RequestMapping("/api/user")
public class SwaggerDemoController {

    @ApiOperation(value = "获取用户列表", notes = "")
    @RequestMapping(value = { "" }, method = RequestMethod.GET)
    public @ResponseBody List<User> getUserList(@ApiParam("整形参数") @RequestParam(value = "a", required = false) Integer a,
            @ApiParam(name = "time", value = "时间参数", required = true, defaultValue = "1970-01-01") @RequestParam(value = "time", required = false) Date time) {
        List<User> r = new ArrayList<User>();
        r.add(new User(1, "name", 18, "", 1, time));
        System.out.println(time);
        return r;
    }

    @ApiOperation(value = "创建用户", notes = "根据User对象创建用户")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public @ResponseBody User postUser(@RequestBody User user) {
        System.out.println(Json.toJson(user));
        return new User(1, "name", 18, "", 1, new Date());
    }

    @ApiOperation(value = "获取用户详细信息", notes = "根据url的id来获取用户详细信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody User getUser(@ApiParam("用户id") @PathVariable Integer id) {
        return new User(id, "name", 18, "", 1, new Date());
    }

    @ApiOperation(value = "更新用户详细信息", notes = "根据url的id来指定更新对象，并根据传过来的user信息来更新用户详细信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public @ResponseBody User putUser(@ApiParam("用户id") @PathVariable Integer id, @RequestBody User user) {
        user.setId(id);
        return user;
    }

    @ApiOperation(value = "删除用户", notes = "根据url的id来指定删除对象")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody void deleteUser(@ApiParam("用户id") @PathVariable Integer id) {
        System.out.println(id);
    }


}