package com.zxcPro.dao;

import com.zxcPro.entity.Category;
import com.zxcPro.entity.CategoryVO;
import com.zxcPro.general.GeneralDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper extends GeneralDAO<Category> {

    //1.连接查询
    List<CategoryVO> selectAllCategories();

    //2.子查询
    List<CategoryVO> selectAllCategories2(int parentId);
}