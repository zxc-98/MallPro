package com.zxcPro.dao;

import com.zxcPro.entity.ProductImg;
import com.zxcPro.general.GeneralDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImgMapper extends GeneralDAO<ProductImg> {

    List<ProductImg> selectProductImgByProductId(int ProductId);
}