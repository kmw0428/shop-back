package com.project.shop.Order;

import com.project.shop.Product.Product;
import com.project.shop.Product.ProductRepository;
import com.project.shop.User.User;
import com.project.shop.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrderByUser(User user) {
        return orderRepository.findByUser(user);
    }

    public Order createOrder(Order order) {
        List<Product> products = order.getProducts().stream()
                .map(product -> productRepository.findById(product.getId())
                        .orElseThrow(() -> new RuntimeException("Product not found: " + product.getId())))
                .collect(Collectors.toList());

        order.setProducts(products);

        User user = userRepository.findById(order.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found: " + order.getUser().getId()));
        order.setUser(user);

        order.setOrderDate(new Date());
        order.setStatus("PENDING");

        double totalAmount = products.stream().mapToDouble(Product::getPrice).sum();
        order.setTotalAmount(totalAmount);

        return orderRepository.save(order);
    }

    public Order updateOrderStatus(String id, String status) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }
}
