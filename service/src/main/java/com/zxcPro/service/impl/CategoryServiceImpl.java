package com.zxcPro.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxcPro.dao.CategoryMapper;
import com.zxcPro.entity.CategoryVO;
import com.zxcPro.service.CategoryService;
import com.zxcPro.vo.ResStatus;
import com.zxcPro.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 查询分类列表（包含三级分类）
     * @return
     */
    @Override
    public ResultVO listCategories() {
        //List<CategoryVO> categoryVOS = categoryMapper.selectAllCategories();
        List<CategoryVO> categoryVOS = null;

        String categories = stringRedisTemplate.boundValueOps("categories").get();

        try {
            if (categories == null) {

                    categoryVOS = categoryMapper.selectAllCategories();
                    String str = objectMapper.writeValueAsString(categoryVOS);
                    stringRedisTemplate.boundValueOps("categories").set(str, 1, TimeUnit.DAYS);

            }
            else {
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, CategoryVO.class);
                categoryVOS = objectMapper.readValue(categories, javaType);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new ResultVO(ResStatus.OK, "success", categoryVOS);
    }

    /**
     * 查询所有一级分类，同时查询当前一级分类下销量最高的6个商品
     * @return
     */
    @Override
    public ResultVO listFirstLevelCategories() {
        List<CategoryVO> categoryVOS = categoryMapper.selectFirstLevelCategories();
        return new ResultVO(ResStatus.OK, "success", categoryVOS);
    }


}
