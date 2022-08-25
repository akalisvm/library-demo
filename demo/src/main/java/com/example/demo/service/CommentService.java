package com.example.demo.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.dao.CommentMapper;
import com.example.demo.entity.Comment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {

    @Resource
    CommentMapper commentMapper;

    public void insert(Comment comment) {
        comment.setTime(new Date());
        commentMapper.insert(comment);
    }

    public Page<Comment> selectPage(Integer pageNum, Integer pageSize, String search) {
        LambdaQueryWrapper<Comment> wrapper = Wrappers.lambdaQuery();
        if(StrUtil.isNotBlank(search)) {
            wrapper.like(Comment::getTitle, search);
        }
        return commentMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    public void updateById(Comment comment) {
        commentMapper.updateById(comment);
    }

    public void deleteById(Integer id) {
        commentMapper.deleteById(id);
    }

    public List<Comment> selectThreeLatestComments() {
        return commentMapper.selectThreeLatestComments();
    }

}
