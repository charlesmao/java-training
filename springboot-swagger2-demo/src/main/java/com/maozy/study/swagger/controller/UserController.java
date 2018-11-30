package com.maozy.study.swagger.controller;

import com.maozy.study.swagger.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Api访问地址
 * http://localhost:8080/swagger-ui.html
 */
@Api("用户信息管理")
@RestController
@RequestMapping("/user")
public class UserController {


    private final static List<User> userList = new ArrayList<>();

    {
        userList.add(new User(1, "admin", "123456"));
        userList.add(new User(2, "jacks", "111111"));
        userList.add(new User(3, "lily", "111222"));
    }

    @ApiOperation(value = "根据用户ID获取单个用户", notes = "根据url的id来获取用户详细信息")
    @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Integer", required = true)
    @GetMapping("get/{userId}")
    public User getUserById(@PathVariable Integer userId) {
        List<User> users = userList.stream().filter(user -> user.getUserId().intValue() == userId.intValue()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(users)) {
            return null;
        } else {
            return users.get(0);
        }
    }

    @ApiOperation("获取用户列表")
    @GetMapping("/list")
    public List userList() {
        return userList;
    }

    @ApiOperation("新增用户")
    @PostMapping("/save")
    public boolean save(User user) {
        return userList.add(user);
    }

    @ApiOperation("更新用户")
    @ApiImplicitParam(name = "user", value = "单个用户信息", dataType = "User")
    @PutMapping("/update")
    public boolean update(User user) {
        if (userList().contains(user)) {
            return userList.remove(user) && userList().add(user);
        } else {
            return false;
        }
    }


    @ApiOperation("删除用户")
    @ApiImplicitParam(name = "userIds", value = "N个用户ID", dataType = "List<Integer>")
    @DeleteMapping("/batch/delete")
    public boolean delete(List<Integer> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return false;
        }
        Iterator<User> iterator = userList().iterator();
        while (iterator.hasNext()) {
            User next = iterator.next();
            if (userIds.contains(next.getUserId().intValue())) {
                iterator.remove();
            }
        }
        return true;
    }

    @ApiOperation("删除单个用户")
    @ApiImplicitParam(name = "id", value = "用户ID", dataType = "Integer")
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Integer id) {
        if (CollectionUtils.isEmpty(userList)) {
            return true;
        }
        Iterator<User> iterator = userList().iterator();
        while (iterator.hasNext()) {
            User next = iterator.next();
            if (next.getUserId().intValue() == id.intValue()) {
                iterator.remove();
            }
        }
        return true;
    }





}
