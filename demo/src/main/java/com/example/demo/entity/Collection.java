package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("t_collection")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Collection {

    private Integer userId;
    private Integer bookId;

}
