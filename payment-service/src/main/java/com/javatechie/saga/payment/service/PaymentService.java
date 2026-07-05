package com.javatechie.saga.payment.service;

import com.javatechie.saga.commons.dto.OrderRequestDto;
import com.javatechie.saga.commons.dto.PaymentRequestDto;
import com.javatechie.saga.commons.event.OrderEvent;
import com.javatechie.saga.commons.event.PaymentEvent;
import com.javatechie.saga.commons.event.PaymentStatus;
import com.javatechie.saga.payment.entity.UserBalance;
import com.javatechie.saga.payment.entity.UserTransaction;
import com.javatechie.saga.payment.repository.UserBalanceRepository;
import com.javatechie.saga.payment.repository.UserTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PaymentService {

    @Autowired
    private UserBalanceRepository userBalanceRepository;
    @Autowired
    private UserTransactionRepository userTransactionRepository;

    @PostConstruct
    public void initUserBalanceInDB() {
        userBalanceRepository.saveAll(Stream.of(new UserBalance(101, 5000),
                new UserBalance(102, 3000),
                new UserBalance(103, 4200),
                new UserBalance(104, 20000),
                new UserBalance(105, 999)).collect(Collectors.toList()));
    }

    /**
     * // get the user id
     * // check the balance availability
     * // if balance sufficient -> Payment completed and deduct amount from DB
     * // if payment not sufficient -> cancel order event and update the amount in DB
     **/
    @Transactional
    public PaymentEvent newOrderEvent(OrderEvent orderEvent) {
        OrderRequestDto orderRequestDto = orderEvent.getOrderRequestDto();

        PaymentRequestDto paymentRequestDto = new PaymentRequestDto(orderRequestDto.getOrderId(),
                orderRequestDto.getUserId(), orderRequestDto.getAmount());

        return userBalanceRepository.findById(orderRequestDto.getUserId())
                .filter(ub -> ub.getPrice() > orderRequestDto.getAmount())
                .map(ub -> {
                    ub.setPrice(ub.getPrice() - orderRequestDto.getAmount());
                    userTransactionRepository.save(new UserTransaction(orderRequestDto.getOrderId(), orderRequestDto.getUserId(), orderRequestDto.getAmount()));
                    return new PaymentEvent(paymentRequestDto, PaymentStatus.PAYMENT_COMPLETED);
                }).orElse(new PaymentEvent(paymentRequestDto, PaymentStatus.PAYMENT_FAILED));

    }

//    @Transactional
//    public void cancelOrderEvent(OrderEvent orderEvent) {
//
//        userTransactionRepository.findById(orderEvent.getOrderRequestDto().getOrderId())
//                .ifPresent(ut->{
//                    userTransactionRepository.delete(ut);
//                    userTransactionRepository.findById(ut.getUserId())
//                            .ifPresent(ub->ub.setAmount(ub.getAmount()+ut.getAmount()));
//                });
//    }
    
    @Transactional
    public PaymentEvent cancelOrderEvent(OrderEvent orderEvent) {
        // 1. Find the transaction we are trying to undo
        userTransactionRepository.findById(orderEvent.getOrderRequestDto().getOrderId())
            .ifPresent(ut -> {
                // 2. Delete the record of the transaction
                userTransactionRepository.delete(ut);

                // 3. Find the user's balance and refund the "price"
                userBalanceRepository.findById(ut.getUserId())
                    .ifPresent(ub -> {
                        // Use 'price' as defined in your UserBalance entity
                        int currentBalance = ub.getPrice();
                        int refundAmount = ut.getAmount();
                        ub.setPrice(currentBalance + refundAmount);
                    });
            });

        // 4. Create the "Notice" to send back to Kafka
        // We convert the Order info into a PaymentRequestDto for the event
        PaymentRequestDto dto = new PaymentRequestDto(
            orderEvent.getOrderRequestDto().getOrderId(),
            orderEvent.getOrderRequestDto().getUserId(),
            orderEvent.getOrderRequestDto().getAmount()
        );

        return new PaymentEvent(dto, PaymentStatus.PAYMENT_FAILED);
    }
}
