package com.timedeal.numble.service;

import com.timedeal.numble.controller.order.OrderSaveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedissonOrderFacade {

    private final RedissonClient redissonClient;
    private final OrderService orderService;

    public void addOrder(String loginId, OrderSaveRequest request) throws InterruptedException {
        //key 로 Lock 객체 가져옴
        RLock lock = redissonClient.getLock(request.getProductId().toString());

        try {
            //획득시도 시간, 락 점유 시간
            boolean available = lock.tryLock(5, 1, TimeUnit.SECONDS);

            if (!available) {
                System.out.println("lock 획득 실패");
                return;
            }
            orderService.addOrder(loginId, request);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }


    }
}
