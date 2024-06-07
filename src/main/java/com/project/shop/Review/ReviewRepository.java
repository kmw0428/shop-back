package com.project.shop.Review;

import com.project.shop.Product.Product;
import com.project.shop.User.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByProduct(Product product);
    List<Review> findByUser(User user);
}
