package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "books")
public class BooksEntity {
    @Id
    //id is like primary key so once it is made it can't be changed
    private String isbn;
    private String title;

    //ManyToOne annotation means many books can belong to one author
    //cascade means any operation that happens to a book should also happen to its author
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")
    /*joinColumn is pretty much saying when storing the id of the author object, store it
    in a column named author_id. it knows when we are storing the id bc of the Id annotation
    for AuthorEntity Class*/
    private AuthorEntity author;
    private Integer totalCopies;
    private Integer availableCopies;
    @OneToMany(mappedBy = "book")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<BorrowAndReturnRecordEntity> borrowRecord;


}
