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

        int newTotalAmount = products.stream().mapToInt(Product::getPrice).sum();

        // Check for existing order with same product ID and status PENDING
        List<Order> existingOrders = orderRepository.findByUser(user);
        Optional<Order> existingOrderOpt = existingOrders.stream()
                .filter(o -> o.getStatus().equals("PENDING"))
                .filter(o -> o.getProducts().stream()
                        .anyMatch(p -> products.stream()
                                .anyMatch(np -> np.getId().equals(p.getId()))))
                .findFirst();

        if (existingOrderOpt.isPresent()) {
            Order existingOrder = existingOrderOpt.get();

            for (Product newProduct : products) {
                Optional<Product> existingProductOpt = existingOrder.getProducts().stream()
                        .filter(p -> p.getId().equals(newProduct.getId()))
                        .findFirst();

                if (existingProductOpt.isPresent()) {
                    // Same product ID exists, just update totalAmount
                    existingOrder.setTotalAmount(existingOrder.getTotalAmount() + newProduct.getPrice());
                }
            }

            // Update existing order
            return orderRepository.save(existingOrder);
        } else {
            // Create new order
            order.setTotalAmount(newTotalAmount);
            return orderRepository.save(order);
        }
    }

    public Order updateOrderStatus(String id, String status) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public Optional<Order> updateOrderQuantity(String userId, String productId, int newTotalAmount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        List<Order> orders = orderRepository.findByUser(user);
        if (orders.isEmpty()) {
            return Optional.empty();
        }

        for (Order order : orders) {
            for (Product product : order.getProducts()) {
                if (product.getId().equals(productId)) {
                    int oldTotalAmount = product.getPrice() * (order.getTotalAmount() / product.getPrice());
                    order.setTotalAmount(order.getTotalAmount() - oldTotalAmount + newTotalAmount);
                    orderRepository.save(order);
                    return Optional.of(order);
                }
            }
        }

        return Optional.empty();
    }

    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }
}