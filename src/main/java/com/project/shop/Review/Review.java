package com.project.shop.Review;

import com.project.shop.Product.Product;
import com.project.shop.User.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "reviews")
public class Review {

    @Id
    private String id;

    @DBRef
    private User user;

    @DBRef
    private Product product;

    private String content;
    private int rating;
}
