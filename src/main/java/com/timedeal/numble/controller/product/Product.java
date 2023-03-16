package com.timedeal.numble.controller.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.timedeal.numble.entity.ProductEntity;
import com.timedeal.numble.vo.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
public class Product {

    private String name;
    private String desc;
    private Long amount;
    private Money regularPrice;
    private BigDecimal percentOff;
    private Money salePrice;
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime saleStartDateTime;
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime saleEndDateTime;

    public static Product fromProductEntity(ProductEntity productEntity) {
        return Product.builder()
                .name(productEntity.getName())
                .desc(productEntity.getDescription())
                .amount(productEntity.getAmount())
                .regularPrice(productEntity.getRegularPrice())
                .salePrice(productEntity.getSalePrice())
                .saleStartDateTime(productEntity.getSaleStartDateTime())
                .saleEndDateTime(productEntity.getSaleEndDateTime())
                .build();
    }

    public BigDecimal getPercentOff(){
        return this.regularPrice.getPercentOff(this.salePrice);
    }
}
