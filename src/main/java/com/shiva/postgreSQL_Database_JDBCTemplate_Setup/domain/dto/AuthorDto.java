package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/*we create this class because we want it to look exactly like authorEntity. We can't use authorEntity obejct
in our controller/presentation layer because we never want the presentation layer to interact with the
persistence layer and the author object class is part of persistence layer*/
public class AuthorDto {
    private Long id;
    private String name;
    private Integer age;
}
