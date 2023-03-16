package com.timedeal.numble.entity;

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

    private Long amount;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "regular_price"))
    private Money regularPrice; // 기본가격

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "sale_price"))
    private Money salePrice; // 판매가

    private LocalDateTime saleStartDateTime;

    private LocalDateTime saleEndDateTime;

    public ProductEntity update(String name, String desc, Long amount,
                                Money regularPrice, Money salePrice,
                                LocalDateTime saleStartDateTime, LocalDateTime saleEndDateTime) {
        this.name = StringUtils.defaultIfBlank(name, this.name);
        this.description = StringUtils.defaultIfBlank(desc, this.description);
        this.amount = ObjectUtils.defaultIfNull(amount, this.amount);
        this.regularPrice = ObjectUtils.defaultIfNull(regularPrice, this.regularPrice);
        this.salePrice = ObjectUtils.defaultIfNull(salePrice, this.salePrice);
        this.saleStartDateTime = ObjectUtils.defaultIfNull(saleStartDateTime, this.saleStartDateTime);
        this.saleEndDateTime = ObjectUtils.defaultIfNull(saleEndDateTime, this.saleEndDateTime);
        return this;
    }
}
