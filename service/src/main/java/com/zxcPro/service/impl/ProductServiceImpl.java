package com.zxcPro.service.impl;

import com.zxcPro.dao.ProductImgMapper;
import com.zxcPro.dao.ProductMapper;
import com.zxcPro.dao.ProductParamsMapper;
import com.zxcPro.dao.ProductSkuMapper;
import com.zxcPro.entity.*;
import com.zxcPro.service.ProductService;
import com.zxcPro.vo.ResStatus;
import com.zxcPro.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

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

    public ResultVO listRecommendProducts() {
        List<ProductVO> productVOS = productMapper.selectRecommendProducts();
        return new ResultVO(ResStatus.OK, "success", productVOS);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public ResultVO getProductBasicInfo(String productId) {
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId" , productId);//entity类的字段
        criteria.andEqualTo("productStatus", 1);
        List<Product> products = productMapper.selectByExample(example);

        if (products.size() > 0) {
            Example example1 = new Example(ProductImg.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("itemId" , productId);
            List<ProductImg> productImgs = productImgMapper.selectByExample(example1);

            Example example2 = new Example(ProductSku.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("productId" , productId).andEqualTo("status",1);
            List<ProductSku> productSkus = productSkuMapper.selectByExample(example2);

            Map<String, Object> map = new HashMap<>();
            map.put("product", products.get(0));
            map.put("productImgs", productImgs);
            map.put("productSkus",productSkus);

            return new ResultVO(ResStatus.OK, "success", map);
        }
        else{
            return new ResultVO(ResStatus.NO,"查询的商品不存在！",null);
        }
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
