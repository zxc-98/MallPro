package com.zxcPro.service;


import com.zxcPro.vo.ResultVO;

public interface ProductCommentService {

    /**
     * id实现分页查询
     * @param ProductId 商品id
     * @param pageNum 查询页码
     * @param Limit 每页显示条数
     * @return 分页数据
     */
    ResultVO listCommentsByProductId(String ProductId,int pageNum,int Limit);

    ResultVO getCommentCountByProductId(String productId);
}
