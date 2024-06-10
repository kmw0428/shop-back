package com.project.shop.User;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    private String id;
    private String token;

    @DBRef
    private User user;

    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.user = user;
    }

    // getters and setters 생략...
}
