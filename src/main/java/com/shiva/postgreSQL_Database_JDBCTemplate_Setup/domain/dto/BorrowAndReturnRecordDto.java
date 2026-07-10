package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowAndReturnRecordDto {
    private Long id;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private UserDto user;
    private BooksDto book;
}
