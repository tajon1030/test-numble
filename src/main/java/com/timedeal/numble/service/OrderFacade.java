package com.timedeal.numble.service;

import com.timedeal.numble.controller.order.OrderSaveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderFacade {


    private final OrderService orderService;

    public void addOrder(String loginId, OrderSaveRequest request) throws InterruptedException {
        while (true) {
            try {
                orderService.addOrderWithOptimisticLock(loginId, request);
                break;
            } catch (final Exception ex) {
                log.info("### optimistic lock version 충돌");
                Thread.sleep(50);
            }
        }
    }
}
