package com.example.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Book;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BookMapper extends BaseMapper<Book> {

    Page<Book> selectPage(Page<Book> page,
                          @Param("collectionUserId") Integer collectionUserId,
                          @Param("searchTitle") String searchTitle,
                          @Param("searchAuthor") String searchAuthor,
                          @Param("searchPress") String searchPress,
                          @Param("searchClassification") String searchClassification,
                          @Param("onlyCollection") Boolean onlyCollection);

    List<Map<String, Object>> selectClassificationCounts();

}
