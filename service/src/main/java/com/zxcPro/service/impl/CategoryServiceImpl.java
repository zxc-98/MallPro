package com.zxcPro.service.impl;

import com.zxcPro.dao.CategoryMapper;
import com.zxcPro.entity.CategoryVO;
import com.zxcPro.service.CategoryService;
import com.zxcPro.vo.ResStatus;
import com.zxcPro.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public ResultVO listCategories() {
        List<CategoryVO> categoryVOS = categoryMapper.selectAllCategories();

        return new ResultVO(ResStatus.OK, "success", categoryVOS);
    }
}
