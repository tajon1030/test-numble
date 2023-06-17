package com.timedeal.numble.service;

import com.timedeal.numble.controller.order.OrderSaveRequest;
import com.timedeal.numble.entity.ProductEntity;
import com.timedeal.numble.entity.UserEntity;
import com.timedeal.numble.entity.UserRole;
import com.timedeal.numble.repository.OrderRepository;
import com.timedeal.numble.repository.ProductRepository;
import com.timedeal.numble.repository.UserRepository;
import com.timedeal.numble.vo.Money;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderFacade orderFacade;

    @Autowired
    LettuceOrderFacade lettuceOrderFacade;

    @Autowired
    RedissonOrderFacade redissonOrderFacade;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @BeforeEach
    public void before() {
        ProductEntity product = ProductEntity.builder()
                .name("product1")
                .quantity(30L)
                .saleStartDateTime(LocalDateTime.now())
                .saleEndDateTime(LocalDateTime.of(2099, 12, 31, 0, 0))
                .salePrice(new Money("1"))
                .regularPrice(new Money("10"))
                .description("example")
                .build();

        productRepository.saveAndFlush(product);

        ArrayList<UserEntity> userEntities = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            userEntities.add(UserEntity.builder()
                    .loginId("user" + i)
                    .userRole(UserRole.MEMBER)
                    .userName("user" + i)
                    .password("password")
                    .build());
        }
        userRepository.saveAllAndFlush(userEntities);
    }

    @AfterEach
    public void after() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void 동시에_30개의_주문요청_비관락이용() throws InterruptedException {
        int threadCount = 30;
        //멀티스레드 이용 ExecutorService : 비동기를 단순하게 처리할 수 있도록 해주는 java api
        ExecutorService executorService = Executors.newFixedThreadPool(15);

        //다른 스레드에서 수행이 완료될 때 까지 대기할 수 있도록 도와주는 API - 요청이 끝날때 까지 기다림
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 1; i <= threadCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
                        try {
                            orderService.addOrderWithPessimisticLock("user" + finalI, new OrderSaveRequest(1L));
                        } finally {
                            latch.countDown();
                        }
                    }
            );
        }

        latch.await();

        ProductEntity product = productRepository.findById(1L).orElseThrow();

        //30 - (1*30) = 0
        assertThat(product.getQuantity()).isEqualTo(0L);
    }

    @Test
    public void 동시에_30개의_주문요청_낙관락이용() throws InterruptedException {
        int threadCount = 30;
        //멀티스레드 이용 ExecutorService : 비동기를 단순하게 처리할 수 있도록 해주는 java api
        ExecutorService executorService = Executors.newFixedThreadPool(15);

        //다른 스레드에서 수행이 완료될 때 까지 대기할 수 있도록 도와주는 API - 요청이 끝날때 까지 기다림
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 1; i <= threadCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
                        try {
                            orderFacade.addOrder("user" + finalI, new OrderSaveRequest(1L));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } finally {
                            latch.countDown();
                        }
                    }
            );
        }

        latch.await();

        ProductEntity product = productRepository.findById(1L).orElseThrow();

        //30 - (1*30) = 0
        assertThat(product.getQuantity()).isEqualTo(0L);
    }

    @Test
    public void 동시에_30개의_주문요청_synchronized이용() throws InterruptedException {
        int threadCount = 30;
        //멀티스레드 이용 ExecutorService : 비동기를 단순하게 처리할 수 있도록 해주는 java api
        ExecutorService executorService = Executors.newFixedThreadPool(15);

        //다른 스레드에서 수행이 완료될 때 까지 대기할 수 있도록 도와주는 API - 요청이 끝날때 까지 기다림
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 1; i <= threadCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
                        try {
                            orderService.addOrderWithSynchronized("user" + finalI, new OrderSaveRequest(1L));
                        } finally {
                            latch.countDown();
                        }
                    }
            );
        }

        latch.await();

        ProductEntity product = productRepository.findById(1L).orElseThrow();

        //30 - (1*30) = 0
        assertThat(product.getQuantity()).isEqualTo(0L);
    }

    @Test
    public void 동시에_30개의_주문요청_Lettuce() throws InterruptedException {
        int threadCount = 30;
        //멀티스레드 이용 ExecutorService : 비동기를 단순하게 처리할 수 있도록 해주는 java api
        ExecutorService executorService = Executors.newFixedThreadPool(15);

        //다른 스레드에서 수행이 완료될 때 까지 대기할 수 있도록 도와주는 API - 요청이 끝날때 까지 기다림
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 1; i <= threadCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
                        try {
                            lettuceOrderFacade.addOrder("user" + finalI, new OrderSaveRequest(1L));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } finally {
                            latch.countDown();
                        }
                    }
            );
        }

        latch.await();

        ProductEntity product = productRepository.findById(1L).orElseThrow();

        //30 - (1*30) = 0
        assertThat(product.getQuantity()).isEqualTo(0L);
    }

    @Test
    public void 동시에_30개의_주문요청_Redisson() throws InterruptedException {
        int threadCount = 30;
        //멀티스레드 이용 ExecutorService : 비동기를 단순하게 처리할 수 있도록 해주는 java api
        ExecutorService executorService = Executors.newFixedThreadPool(15);

        //다른 스레드에서 수행이 완료될 때 까지 대기할 수 있도록 도와주는 API - 요청이 끝날때 까지 기다림
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 1; i <= threadCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
                        try {
                            redissonOrderFacade.addOrder("user" + finalI, new OrderSaveRequest(1L));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } finally {
                            latch.countDown();
                        }
                    }
            );
        }

        latch.await();

        ProductEntity product = productRepository.findById(1L).orElseThrow();

        //30 - (1*30) = 0
        assertThat(product.getQuantity()).isEqualTo(0L);
    }
}