package com.javatechie.saga.order.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javatechie.saga.commons.dto.OrderRequestDto;
import com.javatechie.saga.commons.event.OrderEvent;
import com.javatechie.saga.commons.event.OrderStatus;

import lombok.var;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Sinks;
@Slf4j
@Service
public class OrderStatusPublisher {

    @Autowired
    private Sinks.Many<OrderEvent> orderSinks;


    public void publishOrderEvent(OrderRequestDto orderRequestDto, OrderStatus orderStatus) {
        OrderEvent orderEvent = new OrderEvent(orderRequestDto, orderStatus);
        var result = orderSinks.tryEmitNext(orderEvent);
        log.info("Published OrderEvent: {} | Emit result: {}", orderEvent, result);
    }
}
