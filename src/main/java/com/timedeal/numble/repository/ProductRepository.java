package com.timedeal.numble.repository;

import com.timedeal.numble.entity.ProductEntity;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "1000")})
    @Query(value = "select p from ProductEntity p where p.id = :productId")
    Optional<ProductEntity> findByIdWithPessimisticLock(@Param("productId") Long productId);

    //Optimistic Lock
    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select p from ProductEntity p where p.id = :productId")
    Optional<ProductEntity> findByWithOptimisticLock(@Param("productId") Long productId);
}
