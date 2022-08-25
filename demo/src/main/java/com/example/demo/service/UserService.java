package com.example.demo.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.dao.UserMapper;
import com.example.demo.entity.User;
import com.example.demo.utils.JwtUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Resource
    UserMapper userMapper;

    public User login(User user) {
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUsername, user.getUsername())
                .eq(User::getPassword, user.getPassword()));
        if(res != null) {
            Map<String, String> payload = new HashMap<>();
            payload.put("id", String.valueOf(user.getId()));
            payload.put("username", user.getUsername());
            payload.put("password", user.getPassword());
            payload.put("role", user.getRole());
            String token = JwtUtils.getToken(payload);
            res.setToken(token);
        }
        return res;
    }

    public int register(User user) {
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()));
        if(res != null) {
            return -1;
        }
        userMapper.insert(user);
        return 0;
    }

    public void insert(User user) {
        userMapper.insert(user);
    }

    public User select(String username) {
        return userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    public Page<User> selectPage(
            Integer pageNum,
            Integer pageSize,
            String searchUsername,
            String searchPhone,
            String searchEmail) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        if(StrUtil.isNotBlank(searchUsername)) {
            wrapper.eq(User::getUsername, searchUsername);
        }
        if(StrUtil.isNotBlank(searchPhone)) {
            wrapper.eq(User::getPhone, searchPhone);
        }
        if(StrUtil.isNotBlank(searchUsername)) {
            wrapper.eq(User::getEmail, searchEmail);
        }
        return userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    public int updateById(User user) {
        User sameNameUser = select(user.getUsername());
        if(sameNameUser != null && !userMapper.selectById(user.getId()).getId().equals(sameNameUser.getId())) {
            return -1;
        }
        userMapper.updateById(user);
        return 0;
    }

    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    public void deleteBatchIds(List<Integer> ids) {
        userMapper.deleteBatchIds(ids);
    }

}
