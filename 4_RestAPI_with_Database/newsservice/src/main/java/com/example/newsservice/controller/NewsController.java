package com.example.newsservice.controller;

import com.example.newsservice.aspect.UserVerifiable;
import com.example.newsservice.dto.ErrorResponse;
import com.example.newsservice.dto.NewsFilter;
import com.example.newsservice.dto.news.NewsListResponse;
import com.example.newsservice.dto.news.SingleNewsResponse;
import com.example.newsservice.dto.news.UpsertNewsRequest;
import com.example.newsservice.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/news")
@Tag(name = "News V1", description = "News API version V1")
public class NewsController {
    private final NewsService newsService;


    @Operation(summary = "Get all news", tags = {"get all"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = NewsListResponse.class), mediaType = "application/json")}),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(
                    responseCode = "400",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")})
    })
    @PostMapping("/filter")
    public ResponseEntity<NewsListResponse> findAll(@Valid @RequestBody NewsFilter newsFilter) {
        return ResponseEntity.ok(newsService.findAll(newsFilter));
    }

    @Operation(summary = "Get news by ID", tags = {"get by id"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = SingleNewsResponse.class), mediaType = "application/json")}),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")})
    })
    @GetMapping("/{id}")
    public ResponseEntity<SingleNewsResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.findById(id));
    }

    @Operation(summary = "Create news", tags = {"create"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = {@Content(schema = @Schema(implementation = SingleNewsResponse.class), mediaType = "application/json")}),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(
                    responseCode = "400",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")})
    })
    @PostMapping
    public ResponseEntity<SingleNewsResponse> create(@Valid @RequestBody UpsertNewsRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newsService.save(request));
    }

    @Operation(summary = "Update news by ID", tags = {"update"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = SingleNewsResponse.class), mediaType = "application/json")}),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(
                    responseCode = "400",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(
                    responseCode = "400",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")})
    })
    @PutMapping("/{id}")
    @UserVerifiable
    public ResponseEntity<SingleNewsResponse> update(@PathVariable Long id,
                                                     @RequestBody UpsertNewsRequest request,
                                                     @RequestParam Long userId) {
        return ResponseEntity.ok(newsService.update(id, request));
    }

    @Operation(summary = "Delete news by ID", tags = {"delete"})
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(
                    responseCode = "400",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")})
    })
    @DeleteMapping("/{id}")
    @UserVerifiable
    public ResponseEntity<Void> deleteById(@PathVariable Long id, @RequestParam Long userId) {
        newsService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
