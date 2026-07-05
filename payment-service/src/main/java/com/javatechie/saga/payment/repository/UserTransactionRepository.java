package com.javatechie.saga.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javatechie.saga.payment.entity.UserTransaction;
@Repository
public interface UserTransactionRepository extends JpaRepository<UserTransaction,Integer> {
}
