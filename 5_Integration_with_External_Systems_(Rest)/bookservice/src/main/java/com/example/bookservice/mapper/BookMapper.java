package com.example.bookservice.mapper;

import com.example.bookservice.dto.BookResponse;
import com.example.bookservice.dto.UpsertBookRequest;
import com.example.bookservice.entity.Book;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
@DecoratedWith(BookMapperDecorator.class)
public interface BookMapper {
    Book requestToBook(UpsertBookRequest request);

    Book requestToBook(Long id, UpsertBookRequest request);

    @Mapping(source = "category.name", target = "categoryName")
    BookResponse bookToResponse(Book book);

    List<BookResponse> bookToResponse(List<Book> books);

    void update(UpsertBookRequest request, @MappingTarget Book book);

    default String trimString(String value) {
        return value != null ? value.trim() : null;
    }
}
