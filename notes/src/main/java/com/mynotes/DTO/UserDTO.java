package com.mynotes.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String avatar;
}
