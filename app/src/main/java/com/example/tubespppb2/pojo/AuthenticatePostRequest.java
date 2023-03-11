package com.example.tubespppb2.pojo;

public class AuthenticatePostRequest {
    String email;
    String password;
    String role;

    public AuthenticatePostRequest(String email, String password, String role){
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
