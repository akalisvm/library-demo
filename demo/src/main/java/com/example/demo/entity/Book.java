package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@TableName("t_book")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String cover;
    private String title;
    private String author;
    private BigDecimal price;
    private String classification;
    private String press;
    private Integer stock;
    @TableField(exist = false)
    private Integer collection;

}
