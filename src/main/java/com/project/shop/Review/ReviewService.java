package com.project.shop.Review;

import com.project.shop.Product.Product;
import com.project.shop.Product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Optional<Review> getReviewById(String id) {
        return reviewRepository.findById(id);
    }

    public List<Review> getReviewByProductId(String productId) {
        return reviewRepository.findByProductId(productId);
    }

    public List<Review> getReviewByUserId(String userId) {
        return reviewRepository.findByUserId(userId);
    }

    public Review createReview(Review review) {
        Product product = productRepository.findById(review.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + review.getProduct().getId()));
        review.setProduct(product);

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
