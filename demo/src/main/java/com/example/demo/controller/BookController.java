package com.example.demo.controller;


import com.example.demo.common.Result;
import com.example.demo.entity.Book;
import com.example.demo.entity.Borrow;
import com.example.demo.service.BookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Resource
    BookService bookService;

    @PostMapping
    public Result<?> insert(@RequestBody Book book) {
        bookService.insert(book);
        return Result.success();
    }

    @GetMapping
    public Result<?> selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(defaultValue = "0") Integer userId,
                                @RequestParam(defaultValue = "") String searchTitle,
                                @RequestParam(defaultValue = "") String searchAuthor,
                                @RequestParam(defaultValue = "") String searchPress,
                                @RequestParam(defaultValue = "") String searchClassification,
                                @RequestParam(defaultValue = "") Boolean onlyCollection) {
        return Result.success(bookService.selectPage(pageNum, pageSize, userId,
                searchTitle, searchAuthor, searchPress, searchClassification, onlyCollection));
    }

    @PutMapping
    public Result<?> update(@RequestBody Book book) {
        bookService.updateById(book);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        bookService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/collection/{userId}")
    public Result<?> collection(@RequestBody Integer bookId, @PathVariable Integer userId) {
        return bookService.collection(userId, bookId) == 0 ? Result.success() : Result.error("-1", "已经收藏过了");
    }

    @DeleteMapping("/collection/{userId}/{bookId}")
    public Result<?> cancelCollection(@PathVariable Integer userId, @PathVariable Integer bookId) {
        bookService.cancelCollection(userId, bookId);
        return Result.success();
    }

    @GetMapping("/classificationCounts")
    public Result<?> selectClassificationCounts() {
        return Result.success(bookService.selectClassificationCounts());
    }

}
