package com.zxcPro.service.impl;

import com.zxcPro.dao.IndexImgMapper;
import com.zxcPro.entity.IndexImg;
import com.zxcPro.service.IndexImgService;
import com.zxcPro.vo.ResStatus;
import com.zxcPro.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexImgServiceImpl implements IndexImgService {

    @Autowired
    private IndexImgMapper indexImgMapper;

    @Override
    public ResultVO listIndexImgs() {
        List<IndexImg> indexImgs = indexImgMapper.listIndexImgs();
        if (indexImgs.size() == 0) {
            return new ResultVO(ResStatus.NO, "fail", null);
        }
        else {
            return new ResultVO(ResStatus.OK, "success", indexImgs);
        }
    }
}
