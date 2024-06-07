package com.project.shop.User;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String username;
    private String password;
    private String email;
    private String nickname;
    private int age;
    private String phoneNumber;
    private String address;
    private String gender;
    private String birthDate;
    private String skinType;
    private String scalpType;
}
