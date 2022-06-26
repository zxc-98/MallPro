package com.zxcPro.service;

import com.zxcPro.vo.ResultVO;

public interface CategoryService {
    ResultVO listCategories();

    ResultVO listFirstLevelCategories();
}
