package com.timedeal.numble.service;

import com.timedeal.numble.controller.error.CustomException;
import com.timedeal.numble.controller.error.ErrorCode;
import com.timedeal.numble.controller.product.AddProductRequest;
import com.timedeal.numble.controller.product.ModifyProductRequest;
import com.timedeal.numble.controller.product.Product;
import com.timedeal.numble.entity.ProductEntity;
import com.timedeal.numble.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Slf4j
@RequiredArgsConstructor
@Validated
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product addProduct(@Valid AddProductRequest request) {
        ProductEntity saveProductEntity = productRepository.save(request.toProductEntity());
        return Product.fromProductEntity(saveProductEntity);
    }

    @Transactional(readOnly = true)
    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .map(Product::fromProductEntity)
                .orElseThrow(()->new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(Product::fromProductEntity);
    }

    @Transactional
    public Product modifyProduct(Long productId, ModifyProductRequest request) {
        return productRepository.findById(productId)
                .map(productEntity -> {
                    ProductEntity updatedProductEntity = productEntity.update(
                            request.getName(),
                            request.getDescription(),
                            request.getAmount(),
                            request.getRegularPrice(),
                            request.getSalePrice(),
                            request.getSaleStartDateTime(),
                            request.getSaleEndDateTime()
                    );
                    return Product.fromProductEntity(updatedProductEntity);
                })
                .orElseThrow(()->new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    @Transactional
    public void removeProduct(Long productId) {
        productRepository.deleteById(productId);
    }
}
