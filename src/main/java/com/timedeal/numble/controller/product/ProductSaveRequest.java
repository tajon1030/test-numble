package com.timedeal.numble.controller.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.timedeal.numble.entity.ProductEntity;
import com.timedeal.numble.vo.Money;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSaveRequest {
    @NotBlank
    private String name;
    private String description;
    private Long quantity;
    private Money regularPrice;
    private Money salePrice;
    // FIXME CustomDeserializer 만들기
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime saleStartDateTime;
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime saleEndDateTime;

    public ProductEntity toProductEntity(){
        return ProductEntity.builder()
                .name(this.name)
                .description(this.description)
                .quantity(this.quantity)
                .regularPrice(this.regularPrice)
                .salePrice(this.salePrice)
                .saleStartDateTime(this.saleStartDateTime)
                .saleEndDateTime(this.getSaleEndDateTime())
                .build();
    }
}
