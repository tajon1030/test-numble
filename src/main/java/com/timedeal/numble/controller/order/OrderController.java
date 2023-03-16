package com.timedeal.numble.controller.order;

import com.timedeal.numble.controller.Utils;
import com.timedeal.numble.controller.product.Product;
import com.timedeal.numble.controller.user.User;
import com.timedeal.numble.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문하기
     * @param user 로그인 사용자
     * @param request 상품 주문 요청 DTO
     */
    @PostMapping
    public ResponseEntity<?> order(@SessionAttribute("loginUser") User user,
                                      @RequestBody OrderSaveRequest request){
        orderService.addOrder(user.getLoginId(), request);
        return ResponseEntity.ok().build();
    }

    /**
     * 주문 상세 조회
     * @param user 로그인 사용자
     * @param orderId 주문 일련번호
     * @return 주문 DTO
     */
    @DeleteMapping("{orderId}")
    public ResponseEntity<Order> getOrder(@SessionAttribute("loginUser") User user,
                                         @PathVariable("orderId") Long orderId){
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    /**
     * 주문 취소
     * @param user 로그인 사용자
     * @param orderId 주문 일련번호
     */
    @DeleteMapping("{orderId}")
    public ResponseEntity<?> cancelOrder(@SessionAttribute("loginUser") User user,
                                         @PathVariable("orderId") Long orderId){
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }

    /**
     * 유저의 구매한 상품리스트 조회
     * @param loginUser 로그인 사용자
     * @param loginId 조회할 유저 아이디
     * @param pageable 페이지 값
     * @return 상품 페이지
     */
    @GetMapping("/member/{loginId}")
    public ResponseEntity<Page<Product>> getMemberOrders(
            @SessionAttribute("loginUser") User loginUser,
            @PathVariable("loginId") String loginId,
            Pageable pageable) {
        // 관리자 권한 확인
        Utils.checkAdminPermission(loginUser);

        return ResponseEntity.ok(orderService.getMemberOrders(loginId, pageable)
                .map(Order::getProduct));
    }

    /**
     * 상품별 구매한 유저 리스트 조회
     * @param loginUser 로그인 사용자
     * @param productId 조회할 상품 일련번호
     * @param pageable 페이지 값
     * @return 유저 페이지
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<User>> getProductOrders(
            @SessionAttribute("loginUser") User loginUser,
            @PathVariable("productId") Long productId,
            Pageable pageable) {
        // 관리자 권한 확인
        Utils.checkAdminPermission(loginUser);

        return ResponseEntity.ok(orderService.getProductOrders(productId, pageable)
                .map(Order::getUser));
    }
}
