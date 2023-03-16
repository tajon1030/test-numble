package com.timedeal.numble.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Setter(value = AccessLevel.PRIVATE)
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;


    public static OrderEntity create(UserEntity user, ProductEntity product) {
        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setProduct(product);
        product.removeQuantity(1); // 한번에 한 상품만 주문가능
        return order;
    }

}
