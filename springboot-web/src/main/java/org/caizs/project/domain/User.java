package org.caizs.project.domain;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 
 * @ClassName: User
 * @Description:
 *
 */
@ApiModel(value = "User", description = "用户对象")
public class User {

    @ApiModelProperty(value = "ID")
    private Integer id;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "年龄", example = "123", required = true)
    private int age;
    @ApiModelProperty(value = "地址")
    private String address;
    @ApiModelProperty(value = "性别")
    private int sex;
    @ApiModelProperty(value = "生日", example = "2016-01-01 10:10:10", required = true)
    private Date birthday;

    public User() {
        super();
    }

    public User(Integer id, String name, int age, String address, int sex, Date bir) {
        super();
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
        this.sex = sex;
        this.birthday = bir;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }


}