package com.zxcPro.service;

import com.zxcPro.vo.ResultVO;
import org.springframework.stereotype.Service;

public interface ProductService {
    ResultVO listRecommendProducts();

    //获取基本信息
    ResultVO getProductBasicInfo(String productId);

    //商品参数
    ResultVO getProductParamsById(String productId);
}
