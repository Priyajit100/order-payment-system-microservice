package com.javatechie.saga.commons.event;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.javatechie.saga.commons.dto.OrderRequestDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderEvent implements Event {

    private UUID eventId;
    private Date eventDate;
    private OrderRequestDto orderRequestDto;
    private OrderStatus orderStatus;

    public OrderEvent(OrderRequestDto orderRequestDto, OrderStatus orderStatus) {
        this.eventId = UUID.randomUUID();
        this.eventDate = new Date();
        this.orderRequestDto = orderRequestDto;
        this.orderStatus = orderStatus;
    }

    // Optional: a post-constructor initializer, if needed
    {
        if (eventId == null) eventId = UUID.randomUUID();
        if (eventDate == null) eventDate = new Date();
    }

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public Date getDate() {
        return eventDate;
    }
}

