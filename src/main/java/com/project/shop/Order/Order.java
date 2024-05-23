package com.project.shop.Order;

import com.project.shop.Product.Product;
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

    private String userId;

    @DBRef
    private List<Product> products;

    private double totalAmount;
    private String status;
    private Date orderDate;
}
