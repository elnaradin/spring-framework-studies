package com.example.contactlist.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

}
