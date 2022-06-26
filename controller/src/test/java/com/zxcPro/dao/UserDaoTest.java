package com.zxcPro.dao;

import com.zxcPro.ControllerApplication;
import com.zxcPro.entity.CategoryVO;
import com.zxcPro.entity.ProductVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ControllerApplication.class)
public class UserDaoTest {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ProductMapper productMapper;

    @Test
    public void testProduct(){
        List<ProductVO> productVOS = productMapper.selectRecommendProducts();
        for (ProductVO productVO : productVOS) {
            System.out.println(productVO);
        }
    }



    @Test
    public void testCategory(){
        List<CategoryVO> categoryVOS = categoryMapper.selectAllCategories2(0);
        for (CategoryVO categoryVO : categoryVOS) {
            System.out.println(categoryVO);
            for (CategoryVO vo1 : categoryVO.getCategories()) {
                System.out.println(vo1);
                for (CategoryVO vo2 : vo1.getCategories()) {
                    System.out.println(vo2);
                }
            }
        }
    }


}
