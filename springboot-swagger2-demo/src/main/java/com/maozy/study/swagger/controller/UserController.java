package com.maozy.study.swagger.controller;

import com.maozy.study.swagger.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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

    @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Integer")
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Integer userId) {
        List<User> users = userList.stream().filter(user -> user.getUserId().intValue() == userId.intValue()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(users)) {
            return null;
        } else {
            return users.get(0);
        }
    }

    @ApiOperation(value = "获取用户列表", notes = "获取用户列表")
    @GetMapping("")
    public List userList() {
        return userList;
    }

    @ApiOperation(value = "新增用户", notes = "根据User对象创建用户")
    @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
    @PostMapping("")
    public boolean save(@RequestBody User user) {
        return userList.add(user);
    }

    @ApiOperation(value = "更新用户", notes = "根据url的userId来指定更新对象，并根据传过来的user信息来更新用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Integer"),
            @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
    })
    @PutMapping("/{userId}")
    public boolean update(@PathVariable Integer userId, @RequestBody User user) {
        for (User u : userList) {
            if (u.getUserId().intValue() == userId.intValue()) {
                u.setUsername(user.getUsername());
                u.setPassword(user.getPassword());
            }
        }

        return true;

    }


    @ApiOperation("批量删除用户")
    @ApiImplicitParam(name = "userIds", value = "N个用户ID", dataType = "String")
    @DeleteMapping("/batch/{userIds}")
    public boolean delete(@PathVariable List<Integer> userIds) {
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

    @ApiOperation(value = "删除单个用户", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "integer")
    @DeleteMapping("/{userId}")
    public boolean delete(@PathVariable Integer userId) {
        if (CollectionUtils.isEmpty(userList)) {
            return true;
        }
        Iterator<User> iterator = userList().iterator();
        while (iterator.hasNext()) {
            User next = iterator.next();
            if (next.getUserId().intValue() == userId.intValue()) {
                iterator.remove();
            }
        }
        return true;
    }

}
