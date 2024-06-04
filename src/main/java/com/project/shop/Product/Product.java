package com.project.shop.Product;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    private String name;
    private String description;
    private int price;
    private String category;
    private String imageUrl;
}
