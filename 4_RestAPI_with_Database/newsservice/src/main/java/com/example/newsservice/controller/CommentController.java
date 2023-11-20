package com.example.newsservice.controller;

import com.example.newsservice.aspect.UserVerifiable;
import com.example.newsservice.dto.ErrorResponse;
import com.example.newsservice.dto.comment.CommentListResponse;
import com.example.newsservice.dto.comment.CommentResponse;
import com.example.newsservice.dto.comment.InsertCommentRequest;
import com.example.newsservice.dto.comment.UpdateCommentRequest;
import com.example.newsservice.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
@Tag(name = "Comment V1", description = "Comment API version V1")
public class CommentController {
    private final CommentService commentService;


    @Operation(summary = "Get all comments by news ID", tags = {"get all"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = CommentListResponse.class)), mediaType = "application/json")}),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")})
    })
    @GetMapping("/byNews/{newsId}")
    public ResponseEntity<List<CommentResponse>> findAllByNewsId(@PathVariable Long newsId) {
        return ResponseEntity.ok(commentService.findByNewsId(newsId));
    }


    @Operation(summary = "Get comment by ID", tags = {"get by id"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = CommentResponse.class), mediaType = "application/json")}),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")})
    })
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.findById(id));
    }


    @Operation(summary = "Create comment", tags = {"create"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = {@Content(schema = @Schema(implementation = CommentResponse.class), mediaType = "application/json")}),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(
                    responseCode = "400",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(
                    responseCode = "403",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")})
    })
    @PostMapping
    public ResponseEntity<CommentResponse> create(@Valid @RequestBody InsertCommentRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.save(request));
    }


    @Operation(summary = "Update comment by ID", tags = {"update"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = CommentResponse.class), mediaType = "application/json")}),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(
                    responseCode = "400",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")})
    })
    @UserVerifiable
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> update(@PathVariable String id,
                                                  @Valid @RequestBody UpdateCommentRequest request,
                                                  @RequestParam Long userId) {
        return ResponseEntity.ok(commentService.update(id, request));
    }

    @Operation(summary = "Delete comment by ID", tags = {"delete"})
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(
                    responseCode = "400",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")})
    })
    @UserVerifiable
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id,
                                           @RequestParam Long userId) {
        commentService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
