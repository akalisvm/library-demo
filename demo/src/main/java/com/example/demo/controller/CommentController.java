package com.example.demo.controller;


import com.example.demo.common.Result;
import com.example.demo.entity.Comment;
import com.example.demo.service.CommentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    CommentService commentService;

    @PostMapping
    public Result<?> insert(@RequestBody Comment comment) {
        comment.setTime(new Date());
        commentService.insert(comment);
        return Result.success();
    }

    @GetMapping
    public Result<?> selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(defaultValue = "") String search) {
        return Result.success(commentService.selectPage(pageNum, pageSize, search));
    }

    @PutMapping
    public Result<?> update(@RequestBody Comment comment) {
        commentService.updateById(comment);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        commentService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/latest")
    public Result<?> selectThreeLatestComments() {
        return Result.success(commentService.selectThreeLatestComments());
    }

}
