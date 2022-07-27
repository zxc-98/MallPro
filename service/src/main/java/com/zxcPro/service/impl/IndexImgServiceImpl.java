package com.zxcPro.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxcPro.dao.IndexImgMapper;
import com.zxcPro.entity.IndexImg;
import com.zxcPro.service.IndexImgService;
import com.zxcPro.vo.ResStatus;
import com.zxcPro.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class IndexImgServiceImpl implements IndexImgService {

    @Autowired
    private IndexImgMapper indexImgMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ResultVO listIndexImgs() {
        String indexImgs = stringRedisTemplate.boundValueOps("indexImgs").get();
        List<IndexImg> imgsList = null;
        try {
            if (indexImgs == null) {
                imgsList = indexImgMapper.listIndexImgs();
                stringRedisTemplate.boundValueOps("indexImgs").set(objectMapper.writeValueAsString(imgsList));
                stringRedisTemplate.boundValueOps("indexImgs").expire(1, TimeUnit.DAYS);
            }
            else {
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, IndexImg.class);
                imgsList = objectMapper.readValue(indexImgs, javaType);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (imgsList != null) {
            return new ResultVO(ResStatus.OK, "success", imgsList);
        }
        else {
            return new ResultVO(ResStatus.NO, "fail", null);
        }
    }
}
