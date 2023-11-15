package com.example.bookservice.dto;

import lombok.Data;

@Data
public class UpsertBookRequest {
    private String title;
    private String authorName;
    private String categoryName;
}
