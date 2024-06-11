package com.project.shop.Wish;

import com.project.shop.Product.Product;
import com.project.shop.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/wish")

public class WishController {

    @Autowired
    private WishService wishService;

    @GetMapping
    public List<Wish> getAllWishes() {
        return wishService.getAllWish();
    }

    @GetMapping("/{id}")
    public Optional<Wish> getWishById(@PathVariable String id) {
        return wishService.getWishById(id);
    }

    @GetMapping("/product/{product}")
    public List<Wish> getWishByProductId(@PathVariable Product product) {
        return wishService.getWishByProduct(product);
    }

    @GetMapping("/user/{user}")
    public List<Wish> getWishByUserId(@PathVariable User user) {
        return wishService.getWishByUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteWish(@PathVariable String id){wishService.deleteWish(id);}
}
