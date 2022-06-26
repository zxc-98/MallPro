package com.zxcPro.dao;

import com.zxcPro.entity.ProductImg;
import com.zxcPro.general.GeneralDAO;

import java.util.List;

public interface ProductImgMapper extends GeneralDAO<ProductImg> {

    List<ProductImg> selectProductImgByProductId(int ProductId);
}