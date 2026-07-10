package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "authors")

//this is the class where author Objects directly interact with database, this is also persistence layer
public class AuthorEntity {
    @Id
    //we don't have to set id manually
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "authors_seq"
    )
    @SequenceGenerator(
            name = "authors_seq",
            sequenceName = "authors_seq",
            allocationSize = 1
    )
    private Long id;
    private String name;
    private Integer age;

}
