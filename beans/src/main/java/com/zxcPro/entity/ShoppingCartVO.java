package com.zxcPro.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartVO {
    private Integer cartId;
    private String productId;
    private String skuId;
    private String userId;
    private String cartNum;
    private String cartTime;
    private BigDecimal productPrice;
    private String skuProps;

    private String productName;
    private String productImg;
    private Integer originalPrice;
    private Integer sellPrice;
    private String skuName;
    private int skuStock;
}