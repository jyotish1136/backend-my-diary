package com.mynotes.DTO;

import lombok.Data;

@Data
public class CompleteSignupRequest {
    private String tempToken;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
}
