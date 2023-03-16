package com.timedeal.numble.controller.product;

import com.timedeal.numble.controller.Utils;
import com.timedeal.numble.controller.user.User;
import com.timedeal.numble.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/products")
public class ProductController {

    private final ProductService productService;

    // FIXME 커서기반으로 수정

    /**
     * 상품 목록 조회
     *
     * @param pageable 페이징 정보
     * @return 상품 DTO 페이지
     */
    @GetMapping
    public ResponseEntity<Page<Product>> getListProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.getProducts(pageable));
    }

    /**
     * 상품 상세 조회
     *
     * @param productId 상품 아이디
     * @return 상품 DTO
     */
    @GetMapping("{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    /**
     * 상품 등록
     *
     * @param user    로그인 사용자
     * @param request 상품등록요청 DTO
     * @return 상품 DTO
     */
    @PostMapping
    public ResponseEntity<Product> addProduct(@SessionAttribute("loginUser") User user,
                                              @RequestBody ProductSaveRequest request) {
        // 관리자 권한 확인
        Utils.checkAdminPermission(user);
        return ResponseEntity.ok(productService.addProduct(request));
    }

    /**
     * 상품 수정
     *
     * @param user      로그인 사용자
     * @param productId 상품아이디
     * @param request   상품수정요청 DTO
     * @return 상품 DTO
     */
    @PatchMapping("{productId}")
    public ResponseEntity<Product> modifyProduct(@SessionAttribute("loginUser") User user,
                                                 @PathVariable Long productId,
                                                 @RequestBody ProductModifyRequest request) {
        // 관리자 권한 확인
        Utils.checkAdminPermission(user);
        return ResponseEntity.ok(productService.modifyProduct(productId, request));
    }

    /**
     * 상품 삭제
     *
     * @param user      로그인 사용자
     * @param productId 상품아이디
     */
    @DeleteMapping("{productId}")
    public ResponseEntity<?> removeProduct(@SessionAttribute("loginUser") User user,
                                           @PathVariable Long productId) {
        // 관리자 권한 확인
        Utils.checkAdminPermission(user);
        productService.removeProduct(productId);
        return ResponseEntity.ok().build();
    }
}
