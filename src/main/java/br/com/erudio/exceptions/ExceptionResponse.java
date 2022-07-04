package br.com.erudio.exceptions;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Builder
@Data
public class ExceptionResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -2076122951856686165L;

    private ZonedDateTime timestamp;
    private String message;
    private String details;
}
