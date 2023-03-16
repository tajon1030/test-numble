package com.timedeal.numble.entity;

import com.timedeal.numble.controller.error.CustomException;
import com.timedeal.numble.controller.error.ErrorCode;
import com.timedeal.numble.vo.Money;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private String description;

    private Long quantity;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "regular_price"))
    private Money regularPrice; // 기본가격

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "sale_price"))
    private Money salePrice; // 판매가

    private LocalDateTime saleStartDateTime;

    private LocalDateTime saleEndDateTime;

    public ProductEntity update(String name, String desc, Long quantity,
                                Money regularPrice, Money salePrice,
                                LocalDateTime saleStartDateTime, LocalDateTime saleEndDateTime) {
        this.name = StringUtils.defaultIfBlank(name, this.name);
        this.description = StringUtils.defaultIfBlank(desc, this.description);
        this.quantity = ObjectUtils.defaultIfNull(quantity, this.quantity);
        this.regularPrice = ObjectUtils.defaultIfNull(regularPrice, this.regularPrice);
        this.salePrice = ObjectUtils.defaultIfNull(salePrice, this.salePrice);
        this.saleStartDateTime = ObjectUtils.defaultIfNull(saleStartDateTime, this.saleStartDateTime);
        this.saleEndDateTime = ObjectUtils.defaultIfNull(saleEndDateTime, this.saleEndDateTime);
        return this;
    }

    public boolean isSoldOut() {
        return this.quantity == 0L;
    }

    public boolean isSaleTime() {
        return LocalDateTime.now().isAfter(this.saleStartDateTime) &&
                LocalDateTime.now().isBefore(this.saleEndDateTime);
    }

    public void removeQuantity(long quantity) {
        long restStock = this.quantity - quantity;
        // 검증 중복?
        if (restStock < 0) {
            throw new CustomException(ErrorCode.PRODUCT_SOLD_OUT);
        }
        this.quantity = restStock;
    }

    public void addQuantity(long quantity) {
        this.quantity += quantity;
    }
}
