package com.project.shop.Order;

import com.project.shop.User.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderStatusRepository extends MongoRepository<OrderStatus, String> {
    List<OrderStatus> findByUser(User user);
}
