package com.project.shop.Review;

import com.project.shop.Product.Product;
import com.project.shop.Product.ProductRepository;
import com.project.shop.User.User;
import com.project.shop.User.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Optional<Review> getReviewById(String id) {
        return reviewRepository.findById(id);
    }

    public List<Review> getReviewByProduct(Product product) {
        return reviewRepository.findByProduct(product);
    }

    public List<Review> getReviewByUser(User user) {
        return reviewRepository.findByUser(user);
    }

    public Review createReview(Review review) {
        Product product = productRepository.findById(review.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + review.getProduct().getId()));
        review.setProduct(product);

        User user = userRepository.findById(review.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found: " + review.getUser().getId()));
        review.setUser(user);

        return reviewRepository.save(review);
    }

    public Review updateReview(String id, Review reviewDetails) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
        review.setContent(reviewDetails.getContent());
        review.setRating(reviewDetails.getRating());
        return reviewRepository.save(review);
    }

    public void deleteReview(String id) {
        reviewRepository.deleteById(id);
    }
}
