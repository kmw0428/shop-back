package com.project.shop.Wish;

import com.project.shop.Product.Product;
import com.project.shop.Product.ProductRepository;
import com.project.shop.Review.Review;
import com.project.shop.User.User;
import com.project.shop.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WishService {
    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Wish> getAllWish() {
        return wishRepository.findAll();
    }

    public Optional<Wish> getWishById(String id) {
        return wishRepository.findById(id);
    }

    public List<Wish> getWishByUser(User user) {
        return wishRepository.findByUser(user);
    }

    public List<Wish> getWishByProduct(Product product) {
        return wishRepository.findByProduct(product);
    }

    public Wish createWish(Wish wish) {
        Product product = productRepository.findById(wish.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + wish.getProduct().getId()));
        wish.setProduct(product);

        User user = userRepository.findById(wish.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found: " + wish.getUser().getId()));
        wish.setUser(user);

        return wishRepository.save(wish);
    }

    public void deleteWish(String id) {
        wishRepository.deleteById(id);
    }
}
