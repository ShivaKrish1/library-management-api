package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.mappers;

public interface mapper<A, B> {

    B mapto(A a);

    A mapfrom(B b);

}
