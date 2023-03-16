package com.timedeal.numble.controller.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.timedeal.numble.vo.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyProductRequest {
    private String name;
    private String description;
    private Long amount;
    private Money regularPrice;
    private Money salePrice;
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime saleStartDateTime;
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime saleEndDateTime;
}
