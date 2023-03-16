package com.timedeal.numble.repository;

import com.timedeal.numble.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Page<OrderEntity> findByUserId(Long userId, Pageable pageable);

    Page<OrderEntity> findByProductId(Long productId, Pageable pageable);

    Optional<OrderEntity> findByUserIdAndProductId(Long userId, Long productId);
}
