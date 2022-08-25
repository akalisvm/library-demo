package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.Borrow;
import com.example.demo.service.BorrowService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/borrow")
public class BorrowController {

    @Resource
    BorrowService borrowService;

    @PostMapping("/{userId}")
    public Result<?> applyBorrow(@RequestBody Borrow borrow, @PathVariable Integer userId) {
        borrow.setUserId(userId);
        borrow.setState("待审核");
        return borrowService.applyBorrow(borrow) == 0 ? Result.success() : Result.error("-1", "已经提交过借阅申请");
    }

    @GetMapping
    public Result<?> selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(defaultValue = "0") Integer userId,
                                @RequestParam(defaultValue = "") String searchTitle,
                                @RequestParam(defaultValue = "") String searchUsername,
                                @RequestParam(defaultValue = "") String searchPhone,
                                @RequestParam(defaultValue = "") String searchEmail,
                                @RequestParam(defaultValue = "") String searchState) {
        return Result.success(borrowService.selectPage(
                pageNum, pageSize, userId, searchTitle, searchUsername, searchPhone, searchEmail, searchState));
    }

    @PutMapping("/approve")
    public Result<?> approveBorrow(@RequestBody Borrow borrow) {
        borrowService.approveBorrow(borrow);
        return Result.success();
    }

    @PutMapping("/finish")
    public Result<?> finishBorrow(@RequestBody Borrow borrow) {
        borrowService.finishBorrow(borrow);
        return Result.success();
    }

    @PutMapping("/drop")
    public Result<?> dropBorrow(@RequestBody Borrow borrow) {
        borrowService.dropBorrow(borrow);
        return Result.success();
    }

    @GetMapping("/count")
    public Result<?> selectStateCounts(@RequestParam(defaultValue = "0") Integer userId) {
        return Result.success(borrowService.selectStateCounts(userId));
    }

}
