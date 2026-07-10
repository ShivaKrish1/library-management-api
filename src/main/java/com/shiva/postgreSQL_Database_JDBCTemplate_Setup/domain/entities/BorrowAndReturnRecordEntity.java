package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "borrowed books")
public class BorrowAndReturnRecordEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "borrow_seq"
    )
    @SequenceGenerator(
            name = "borrow_seq",
            sequenceName = "borrow_seq",
            allocationSize = 1
    )
    private Long id;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "book_isbn")
    private BooksEntity book;

}
