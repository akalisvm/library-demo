package com.example.demo.service;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.dao.BookMapper;
import com.example.demo.dao.CollectionMapper;
import com.example.demo.entity.Book;
import com.example.demo.entity.Collection;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class BookService {

    @Resource
    BookMapper bookMapper;

    @Resource
    CollectionMapper collectionMapper;

    public void insert(Book book) {
        bookMapper.insert(book);
    }

    public Page<Book> selectPage(Integer pageNum,
                                 Integer pageSize,
                                 Integer userId,
                                 String searchTitle,
                                 String searchAuthor,
                                 String searchPress,
                                 String searchClassification,
                                 Boolean onlyCollection) {
        return bookMapper.selectPage(new Page<>(pageNum, pageSize), userId,
                searchTitle, searchAuthor, searchPress, searchClassification, onlyCollection);
    }

    public void updateById(Book book) {
        bookMapper.updateById(book);
    }

    public void deleteById(Integer id) {
        bookMapper.deleteById(id);
    }

    public int collection(Integer userId, Integer bookId) {
        Collection res = collectionMapper.selectOne((Wrappers.<Collection>lambdaQuery().
                        eq(Collection::getUserId, userId).
                        eq(Collection::getBookId, bookId)));
        if(res != null) {
            return -1;
        }
        collectionMapper.insert(new Collection(userId, bookId));
        return 0;
    }

    public void cancelCollection(Integer userId, Integer bookId) {
        collectionMapper.delete(Wrappers.<Collection>lambdaQuery()
                .eq(Collection::getUserId, userId)
                .eq(Collection::getBookId, bookId));
    }

    public List<Map<String, Object>> selectClassificationCounts() {
        return bookMapper.selectClassificationCounts();
    }

}
