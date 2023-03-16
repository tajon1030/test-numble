package com.timedeal.numble.repository;

import com.timedeal.numble.entity.OrderEntity;
import com.timedeal.numble.entity.ProductEntity;
import com.timedeal.numble.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Page<OrderEntity> findByUserEntity(UserEntity userEntity, Pageable pageable);
    Page<OrderEntity> findByProductEntity(ProductEntity productEntity, Pageable pageable);
}
