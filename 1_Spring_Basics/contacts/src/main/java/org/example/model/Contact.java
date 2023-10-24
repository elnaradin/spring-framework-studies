package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    private String fullName;
    private String phone;
    private String email;

    @Override
    public String toString() {
        String delimiter = " | ";
        return fullName
                .concat(delimiter)
                .concat(phone)
                .concat(delimiter)
                .concat(email);
    }
}
