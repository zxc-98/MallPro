package com.zxcPro.dao;

import com.zxcPro.ControllerApplication;
import com.zxcPro.entity.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ControllerApplication.class)
public class UserDaoTest {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductCommentsMapper productCommentsMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis(){
        String categories = stringRedisTemplate.boundValueOps("categories").get();
        stringRedisTemplate.delete("categories");
    }

    @Test
    public void testOrderTimeoutCheck(){
        Example example = new Example(Orders.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status" , 1);

        Date time = new Date(System.currentTimeMillis() - 30*60*1000);
        criteria.andLessThan("createTime", time);

        List<Orders> orders = ordersMapper.selectByExample(example);
        for (Orders order : orders) {
            System.out.println(order);
        }
    }




    @Test
    public void testSelectShoppingCart(){
        List<ShoppingCartVO> shoppingCartVOS = shoppingCartMapper.selectShoppingCartByUserId(6);
        for (ShoppingCartVO shoppingCartVO : shoppingCartVOS) {
            System.out.println(shoppingCartVO);
        }
    }



    @Test
    public void testFirstLevel(){
        List<ProductCommentsVO> productCommentsVOS = productCommentsMapper.selectCommentsByProductId("3", 0, 2);
        for (ProductCommentsVO productCommentsVO : productCommentsVOS) {
            System.out.println(productCommentsVO);
        }
    }


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
