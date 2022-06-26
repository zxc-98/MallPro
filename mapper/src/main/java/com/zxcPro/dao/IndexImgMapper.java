package com.zxcPro.dao;

import com.zxcPro.entity.IndexImg;
import com.zxcPro.general.GeneralDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndexImgMapper extends GeneralDAO<IndexImg> {

    List<IndexImg> listIndexImgs ();
}