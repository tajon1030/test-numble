package com.timedeal.numble.controller.order;

import com.timedeal.numble.controller.product.Product;
import com.timedeal.numble.controller.user.User;
import com.timedeal.numble.entity.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Order {
    private Long id;
    private Product product;
    private User user;

    public static Order fromOrderEntity(OrderEntity orderEntity){
        return Order.builder()
                .id(orderEntity.getId())
                .product(Product.fromProductEntity(orderEntity.getProduct()))
                .user(User.fromUserEntity(orderEntity.getUser()))
                .build();
    }
}
