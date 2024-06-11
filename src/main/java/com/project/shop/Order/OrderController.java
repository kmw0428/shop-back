package com.project.shop.Order;

import com.project.shop.Product.Product;
import com.project.shop.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Optional<Order> getOrderById(@PathVariable String id) {
        return orderService.getOrderById(id);
    }

    @GetMapping("/user/{user}")
    public List<Order> getOrderByUserId(@PathVariable User user) {
        return orderService.getOrderByUser(user);
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @PutMapping("/user/{userId}/products/{productId}")
    public ResponseEntity<Order> updateOrderQuantity(@PathVariable String userId, @PathVariable String productId, @RequestParam int totalAmount) {
        Optional<Order> updatedOrder = orderService.updateOrderQuantity(userId, productId, totalAmount);
        if (updatedOrder.isPresent()) {
            return ResponseEntity.ok(updatedOrder.get());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable String id) {
        orderService.deleteOrder(id);
    }

}
