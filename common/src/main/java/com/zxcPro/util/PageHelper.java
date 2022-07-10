package com.zxcPro.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageHelper<T> {

    //总记录数
    private int count;

    //总页数
    private int PageCount;

    //分页数据
    private List<T> list;
}
