package com.example.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Comment;

import java.util.List;

public interface CommentMapper extends BaseMapper<Comment> {
    List<Comment> selectThreeLatestComments();
}
