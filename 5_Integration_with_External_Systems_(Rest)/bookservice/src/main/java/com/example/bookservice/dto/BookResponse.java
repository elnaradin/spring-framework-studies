package com.example.bookservice.dto;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@Data
@FieldNameConstants
public class BookResponse implements Serializable {
    private Long id;
    private String title;
    private String authorName;
    private String categoryName;

}
