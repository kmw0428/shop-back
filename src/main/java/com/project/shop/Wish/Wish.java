package com.project.shop.Wish;

import com.project.shop.Product.Product;
import com.project.shop.User.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "wishes")
public class Wish {

    @Id
    private String id;

    @DBRef
    private User user;

    @DBRef
    private Product product;
}
