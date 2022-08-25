package com.example.demo.controller;


import com.example.demo.common.Result;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody User user, HttpServletResponse response) throws JsonProcessingException, UnsupportedEncodingException {
        User res = userService.login(user);
        if(res == null) {
            return Result.error("-1", "用户名或密码错误");
        }
        ObjectMapper om = new ObjectMapper();
        String userInfo = URLEncoder.encode(om.writeValueAsString(res), "UTF-8");
        Cookie cookie = new Cookie("user", userInfo);
        cookie.setPath("/");
        cookie.setMaxAge(1000 * 24 * 7);
        response.addCookie(cookie);
        return Result.success(res);
    }

    @PostMapping("/test")
    public Result<?> test() {
        return Result.success();
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        return userService.register(user) == 0 ? Result.success() : Result.error("-1", "用户名重复");
    }

    @PostMapping
    public Result<?> insert(@RequestBody User user) {
        userService.insert(user);
        return Result.success();
    }

    @GetMapping("/{username}")
    public Result<?> select(@PathVariable String username) {
        return Result.success(userService.select(username));
    }

    @GetMapping
    public Result<?> selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(defaultValue = "") String searchUsername,
                                @RequestParam(defaultValue = "") String searchPhone,
                                @RequestParam(defaultValue = "") String searchEmail) {
        return Result.success(userService.selectPage(pageNum, pageSize, searchUsername, searchPhone, searchEmail));
    }

    @PutMapping
    public Result<?> updateById(@RequestBody User user) {
        return userService.updateById(user) == 0 ? Result.success() : Result.error("-1", "用户名重复");
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteById(@PathVariable Integer id) {
        userService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/deleteBatchIds")
    public Result<?> deleteBatchIds(@RequestBody List<Integer> ids) {
        userService.deleteBatchIds(ids);
        return Result.success();
    }

}
