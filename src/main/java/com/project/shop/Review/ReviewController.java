package com.project.shop.Review;

import com.project.shop.Product.Product;
import com.project.shop.User.User;
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

    @GetMapping("/product/{product}")
    public List<Review> getReviewByProductId(@PathVariable Product product) {
        return reviewService.getReviewByProduct(product);
    }

    @GetMapping("/user/{user}")
    public List<Review> getReviewByUserId(@PathVariable User user) {
        return reviewService.getReviewByUser(user);
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
