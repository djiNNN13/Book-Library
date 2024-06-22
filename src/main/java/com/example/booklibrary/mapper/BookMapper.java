package com.example.booklibrary.mapper;

import com.example.booklibrary.dto.BookWithReaderDto;
import com.example.booklibrary.entity.Book;
import com.example.booklibrary.entity.Reader;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {
  BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

  @Mappings({
    @Mapping(source = "book.id", target = "id"),
    @Mapping(source = "book.name", target = "name"),
  })
  BookWithReaderDto bookToBookDto(Book book, Reader reader);
}
