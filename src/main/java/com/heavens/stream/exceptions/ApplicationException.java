package com.heavens.stream.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Data
@SuppressWarnings("java:S1948")
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationException extends RuntimeException {

    private HttpStatus httpStatus;
    private String message;
    private Object data;


    public ApplicationException(String message) {
        this(HttpStatus.BAD_REQUEST, message);
    }

    public ApplicationException(HttpStatus httpStatus, String message) {
        this(httpStatus, message, Collections.singletonList(message), null);
    }

    public ApplicationException(HttpStatus httpStatus, String message, List<String> errors, Object data) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = String.join(" . ", errors);
        this.data = data;
    }


}



