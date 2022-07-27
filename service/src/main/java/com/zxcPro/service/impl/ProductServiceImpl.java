package com.zxcPro.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxcPro.dao.ProductImgMapper;
import com.zxcPro.dao.ProductMapper;
import com.zxcPro.dao.ProductParamsMapper;
import com.zxcPro.dao.ProductSkuMapper;
import com.zxcPro.entity.*;
import com.zxcPro.service.ProductService;
import com.zxcPro.vo.ResStatus;
import com.zxcPro.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductImgMapper productImgMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private ProductParamsMapper productParamsMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    public ResultVO listRecommendProducts() {
        List<ProductVO> productVOS = productMapper.selectRecommendProducts();
        return new ResultVO(ResStatus.OK, "success", productVOS);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public ResultVO getProductBasicInfo(String productId) {
        String productInfo = (String)stringRedisTemplate.boundHashOps("product").get(productId);

        if (productInfo != null) {
            try {
                Product product = objectMapper.readValue(productInfo, Product.class);
                // redis --> imgs
                String imgStr = (String) stringRedisTemplate.boundHashOps("productImgs").get(productId);
                JavaType javaTypeImg = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ProductImg.class);
                List<ProductImg> productImgs = objectMapper.readValue(imgStr, javaTypeImg);

                // redis --> sku
                String skusStr = (String) stringRedisTemplate.boundHashOps("productSkus").get(productId);
                JavaType javaTypeSku = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ProductSku.class);
                List<ProductImg> productSkus = objectMapper.readValue(skusStr, javaTypeSku);

                Map<String, Object> basicInfo = new HashMap<>();
                basicInfo.put("product", product);
                basicInfo.put("productImgs", productImgs);
                basicInfo.put("productSkus",productSkus);
                return new ResultVO(ResStatus.OK, "success", basicInfo);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        else {
            Example example = new Example(Product.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("productId" , productId);//entity类的字段
            criteria.andEqualTo("productStatus", 1);
            List<Product> products = productMapper.selectByExample(example);

            if (products.size() > 0) {
                try {
                    Product product = products.get(0);
                    String jsonStrProduct = objectMapper.writeValueAsString(product);
                    //product --> redis
                    stringRedisTemplate.boundHashOps("product").put(productId, jsonStrProduct);

                    Example example1 = new Example(ProductImg.class);
                    Example.Criteria criteria1 = example1.createCriteria();
                    criteria1.andEqualTo("itemId" , productId);
                    List<ProductImg> productImgs = productImgMapper.selectByExample(example1);
                    //productImgs --> redis
                    stringRedisTemplate.boundHashOps("productImgs").put(productId, objectMapper.writeValueAsString(productImgs));

                    Example example2 = new Example(ProductSku.class);
                    Example.Criteria criteria2 = example2.createCriteria();
                    criteria2.andEqualTo("productId" , productId).andEqualTo("status",1);
                    List<ProductSku> productSkus = productSkuMapper.selectByExample(example2);
                    //productSkus --> redis
                    stringRedisTemplate.boundHashOps("productSkus").put(productId, objectMapper.writeValueAsString(productSkus));

                    Map<String, Object> map = new HashMap<>();
                    map.put("product", product);
                    map.put("productImgs", productImgs);
                    map.put("productSkus",productSkus);

                    return new ResultVO(ResStatus.OK, "success", map);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return new ResultVO(ResStatus.NO,"fail",null);
    }

    @Override
    public ResultVO getProductParamsById(String productId) {
        Example example = new Example(ProductParams.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId" , productId);
        List<ProductParams> productParams = productParamsMapper.selectByExample(example);
        if (productParams.size() > 0) {
            return new ResultVO(ResStatus.OK, "success", productParams.get(0));
        }
        else {
            return new ResultVO(ResStatus.NO, "三无产品", null);
        }
    }
}
