package com.timedeal.numble.service;

import com.timedeal.numble.controller.order.OrderSaveRequest;
import com.timedeal.numble.repository.RedisLockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LettuceOrderFacade {

    private final RedisLockRepository redisLockRepository;
    private final OrderService orderService;

    public void addOrder(String loginId, OrderSaveRequest request) throws InterruptedException {
        // Lock 획득 시도
        while (!redisLockRepository.lock(request.getProductId())) {
            //SpinLock 방식이 redis 에게 주는 부하를 줄여주기위한 sleep
            Thread.sleep(100);
        }

        //lock 획득 성공시
        try {
            orderService.addOrder(loginId, request);
        } finally {
            //락 해제
            redisLockRepository.unlock(request.getProductId());
        }
    }
}
