package com.shiva.postgreSQL_Database_JDBCTemplate_Setup.exceptions;

import java.time.Instant;
import java.util.List;

/* Ensures every failure (validation, conflict, etc.) comes back
   with the same JSON structure instead of an inconsistent body
   or a raw stack trace. */

public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        List<String> details
) {
    public ApiError(int status, String error, String message) {
        this(Instant.now(), status, error, message, List.of());
    }

    public ApiError(int status, String error, String message, List<String> details) {
        this(Instant.now(), status, error, message, details);
    }
}