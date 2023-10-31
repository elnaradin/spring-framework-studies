package com.example.studentrecorder.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class Student {
    private Integer id;
    private String firstName;
    private String lastName;
    private Integer age;

    public Student(String firstName, String lastName, Integer age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    @Override
    public String toString() {
        return String
                .join(
                        " | ",
                        String.valueOf(id),
                        firstName,
                        lastName,
                        String.valueOf(age)
                );
    }

}
