package com.example.booklibrary.mapper;

import com.example.booklibrary.dto.ReaderWithBooksDto;
import com.example.booklibrary.entity.Book;
import com.example.booklibrary.entity.Reader;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReaderMapper {
  ReaderMapper INSTANCE = Mappers.getMapper(ReaderMapper.class);

  ReaderWithBooksDto readerToDto(Reader reader, List<Book> books);
}
