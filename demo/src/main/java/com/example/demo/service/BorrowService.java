package com.example.demo.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.dao.BorrowMapper;
import com.example.demo.dao.UserMapper;
import com.example.demo.entity.Borrow;
import com.example.demo.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BorrowService {

    @Resource
    UserMapper userMapper;

    @Resource
    BorrowMapper borrowMapper;

    public int applyBorrow(Borrow borrow) {
        Borrow res = borrowMapper.selectOne(Wrappers.<Borrow>lambdaQuery()
                .eq(Borrow::getUserId, borrow.getUserId())
                .eq(Borrow::getBookId, borrow.getBookId())
                .gt(Borrow::getEndDate, borrow.getEndDate()));
        if(res != null) {
            return -1;
        }
        borrowMapper.insert(borrow);
        return 0;
    }

    public Page<Borrow> selectPage(Integer pageNum,
                                   Integer pageSize,
                                   Integer userId,
                                   String searchTitle,
                                   String searchUsername,
                                   String searchPhone,
                                   String searchEmail,
                                   String searchState) {
        String role = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getId, userId)).getRole();
        return borrowMapper.selectPage(
                new Page<>(pageNum, pageSize), role, userId, searchUsername, searchTitle, searchPhone, searchEmail, searchState);
    }

    public void approveBorrow(Borrow borrow) {
        borrow.setState("借阅中");
        borrowMapper.updateById(borrow);
    }

    public void finishBorrow(Borrow borrow) {
        borrow.setState("已归还");
        borrowMapper.updateById(borrow);
    }

    public void dropBorrow(Borrow borrow) {
        borrow.setState("未归还");
        borrowMapper.updateById(borrow);
    }

    public List<Borrow> selectStateCounts(Integer userId) {
        String role = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getId, userId)).getRole();
        return borrowMapper.selectStateCounts(role, userId);
    }

}
