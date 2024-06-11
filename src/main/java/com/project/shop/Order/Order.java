package com.project.shop.Order;

import com.project.shop.Product.Product;
import com.project.shop.User.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    @DBRef
    private User user;

    @DBRef
    private List<Product> products;

    private int totalAmount;
    private String status;
    private Date orderDate;
}
