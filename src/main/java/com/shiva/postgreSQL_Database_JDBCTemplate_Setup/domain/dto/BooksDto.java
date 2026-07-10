package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BooksDto {
    private String isbn;
    private String title;
    private AuthorDto author;
    private Integer totalCopies;
    private Integer availableCopies;
}
