package com.example.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    // @Update("update t_user set username=#{username}, password=#{password}, age=#{age}, gender=#{gender}, address=#{address}, role=#{role} where id=#{id};")
    // void updateById(int id, String username, String password, int age, String gender, String address, String role);

}
