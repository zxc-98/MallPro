package com.zxcPro.service.impl;

import com.zxcPro.dao.ProductCommentsMapper;
import com.zxcPro.entity.ProductComments;
import com.zxcPro.entity.ProductCommentsVO;
import com.zxcPro.service.ProductCommentService;
import com.zxcPro.util.PageHelper;
import com.zxcPro.vo.ResStatus;
import com.zxcPro.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductCommentServiceImpl implements ProductCommentService {

    @Autowired
    private ProductCommentsMapper productCommentsMapper;


    @Override
    public ResultVO listCommentsByProductId(String productId,int pageNum,  int limit) {
        //总记录数
        Example example = new Example(ProductComments.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId", productId);

        int count = productCommentsMapper.selectCountByExample(example);

        //总页数
        int pageCount = count % limit == 0 ? count / limit : count / limit + 1;

        //分页数据
        //3.查询当前页的数据（因为评论中需要用户信息，因此需要连表查询---自定义）
        int start = (pageNum-1)*limit;
        List<ProductCommentsVO> productCommentsVOS = productCommentsMapper.selectCommentsByProductId(productId, start, limit);

        return new ResultVO(ResStatus.OK, "success", new PageHelper<>(count, pageCount, productCommentsVOS));
    }

    @Override
    public ResultVO getCommentCountByProductId(String productId) {
        //总记录数
        Example example = new Example(ProductComments.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId", productId);

        int totalCount = productCommentsMapper.selectCountByExample(example);

        criteria.andEqualTo("commType", 1);
        int goodCommentCount = productCommentsMapper.selectCountByExample(example);

        Example example1 = new Example(ProductComments.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("productId", productId);
        criteria1.andEqualTo("commType", 0);
        int midCommentCount = productCommentsMapper.selectCountByExample(example1);

        Example example2 = new Example(ProductComments.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andEqualTo("productId", productId);
        criteria2.andEqualTo("commType", -1);
        int badCommentCount = productCommentsMapper.selectCountByExample(example2);

        //5.计算好评率
        double percent = (Double.parseDouble(goodCommentCount+"") / Double.parseDouble(totalCount+"") )*100;
        String percentValue = (percent+"").substring(0,(percent+"").lastIndexOf(".")+3);//小数点后3位

        Map<String, Object> map = new HashMap<>();
        map.put("total", totalCount);
        map.put("goodTotal", goodCommentCount);
        map.put("midTotal", midCommentCount);
        map.put("badTotal", badCommentCount);
        map.put("percent", percentValue);
        return new ResultVO(ResStatus.OK, "success", map);
    }
}
