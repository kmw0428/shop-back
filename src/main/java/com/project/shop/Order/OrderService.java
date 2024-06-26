package com.project.shop.Order;

import com.project.shop.Product.Product;
import com.project.shop.Product.ProductRepository;
import com.project.shop.User.User;
import com.project.shop.User.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderStatusRepository orderStatusRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository, OrderStatusRepository orderStatusRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderStatusRepository = orderStatusRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<OrderStatus> getAllOrderStatuses() {
        return orderStatusRepository.findAll();
    }

    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrderByUser(User user) {
        return orderRepository.findByUser(user);
    }

    public List<OrderStatus> getOrderStatusByUser(User user) {
        return orderStatusRepository.findByUser(user);
    }

    public Order createOrder(Order order) {
        // 제품 정보 설정
        List<Product> products = order.getProducts().stream()
                .map(product -> {
                    Product foundProduct = productRepository.findById(product.getId())
                            .orElseThrow(() -> new RuntimeException("Product not found: " + product.getId()));
                    // 클라이언트에서 보낸 가격으로 설정
                    foundProduct.setPrice(product.getPrice());
                    return foundProduct;
                })
                .collect(Collectors.toList());

        order.setProducts(products);

        // 사용자 정보 설정
        User user = userRepository.findById(order.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found: " + order.getUser().getId()));
        order.setUser(user);

        // 주문 날짜 및 상태 설정
        order.setOrderDate(new Date());
        order.setStatus("PENDING");

        // 총 금액 및 수량 설정
        int newTotalAmount = order.getTotalAmount();
        int newQuantity = 1;

        // 제품 가격 검증 및 수량 계산
        if (products.isEmpty() || products.get(0).getPrice() == 0) {
            throw new RuntimeException("Invalid product price or empty product list");
        } else {
            newQuantity = newTotalAmount / products.get(0).getPrice();  // 수량 계산
        }

        order.setTotalAmount(newTotalAmount);
        order.setQuantity(newQuantity);  // 수량 설정

        // 기존 주문 확인 및 업데이트
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
                    existingOrder.setTotalAmount(existingOrder.getTotalAmount() + newTotalAmount);
                    existingOrder.setQuantity(existingOrder.getQuantity() + newQuantity);  // 수량 업데이트
                } else {
                    existingOrder.getProducts().add(newProduct);
                    existingOrder.setTotalAmount(existingOrder.getTotalAmount() + newTotalAmount);
                    existingOrder.setQuantity(existingOrder.getQuantity() + newQuantity);  // 수량 업데이트
                }
            }
            return orderRepository.save(existingOrder);
        } else {
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

    public OrderStatus mergeOrdersAndUpdateStatus(String userId, List<String> orderIds, String status) {
        List<Order> orders = orderRepository.findAllById(orderIds);

        if (orders.isEmpty()) {
            throw new IllegalArgumentException("No orders found for the given IDs");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        for (Order order : orders) {
            if (!order.getUser().getId().equals(userId)) {
                throw new IllegalArgumentException("Orders do not belong to the same user");
            }
        }

        OrderStatus newOrderStatus = new OrderStatus();
        newOrderStatus.setOrders(orders);
        newOrderStatus.setUser(user); // 추가된 부분

        for (Order order : orders) {
            order.setStatus(status);
            orderRepository.save(order);
        }

        return orderStatusRepository.save(newOrderStatus);
    }

    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }
}
