package com.zxcPro.dao;

import com.zxcPro.entity.Product;
import com.zxcPro.entity.ProductVO;
import com.zxcPro.general.GeneralDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductMapper extends GeneralDAO<Product> {

    List<ProductVO> selectRecommendProducts();
}