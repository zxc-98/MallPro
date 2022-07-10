package com.zxcPro.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;

/**
 * 首页类别商品信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CategoryVO {

    private Integer categoryId;

    private String categoryName;

    private Integer categoryLevel;

    private Integer parentId;

    private String categoryIcon;

    private String categorySlogan;

    private String categoryPic;

    private String categoryBgColor;

    private List<CategoryVO> categories;//实现首页类别显示

    private List<ProductVO> products;//实现首页分类商品推荐 ProductVO中有imgs
}