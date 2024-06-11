package com.project.shop.Wish;

import com.project.shop.Product.Product;
import com.project.shop.User.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WishRepository extends MongoRepository<Wish, String> {
    List<Wish> findByProduct(Product product);
    List<Wish> findByUser(User user);
}
