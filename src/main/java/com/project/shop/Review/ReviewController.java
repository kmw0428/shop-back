package com.project.shop.Review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/{id}")
    public Optional<Review> getReviewById(@PathVariable String id) {
        return reviewService.getReviewById(id);
    }

    @GetMapping("/product/{productId}")
    public List<Review> getReviewByProductId(@PathVariable String productId) {
        return reviewService.getReviewByProductId(productId);
    }

    @GetMapping("/user/{userId}")
    public List<Review> getReviewByUserId(@PathVariable String userId) {
        return reviewService.getReviewByUserId(userId);
    }

    @PostMapping
    public Review createReview(@RequestBody Review review) {
        return reviewService.createReview(review);
    }

    @PutMapping("/{id}")
    public Review updateReview(@PathVariable String id, @RequestBody Review reviewDetails) {
        return reviewService.updateReview(id, reviewDetails);
    }

    @DeleteMapping
    public void deleteReview(@PathVariable String id) {
        reviewService.deleteReview(id);
    }
}
