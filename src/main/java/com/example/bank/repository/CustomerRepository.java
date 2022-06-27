package com.example.bank.repository;

import com.example.bank.models.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
    Customer getCustomerById(String id);
    Customer findCustomerByCustomerNo(String customerNo);
    List<Customer> findAll();
}
