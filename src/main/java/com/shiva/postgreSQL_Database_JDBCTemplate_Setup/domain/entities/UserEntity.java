package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_seq"
    )
    @SequenceGenerator(
            name = "user_seq",
            sequenceName = "user_seq",
            allocationSize = 1
    )
    private Long id;
    private String name;
    //no one can have same email
    @Column(unique = true)
    private String email;
    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<BorrowAndReturnRecordEntity> borrowRecord;

}
