package com.example.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Borrow;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BorrowMapper extends BaseMapper<Borrow> {

    Page<Borrow> selectPage(Page<Borrow> page,
                            @Param("role") String role,
                            @Param("userId") Integer userId,
                            @Param("searchTitle") String searchTitle,
                            @Param("searchUsername") String searchUsername,
                            @Param("searchPhone") String searchPhone,
                            @Param("searchEmail") String searchEmail,
                            @Param("searchState") String searchState);

    List<Borrow> selectStateCounts(@Param("role") String role, @Param("userId") Integer userId);

}
