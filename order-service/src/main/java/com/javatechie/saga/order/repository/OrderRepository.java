package com.javatechie.saga.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javatechie.saga.order.entity.PurchaseOrder;
@Repository
public interface OrderRepository extends JpaRepository<PurchaseOrder,Integer> {
}
