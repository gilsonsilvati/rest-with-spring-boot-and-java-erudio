package br.com.erudio.exceptions;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;

public class ExceptionResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -2076122951856686165L;

    private ZonedDateTime timestamp;
    private String message;
    private String details;

    public ExceptionResponse(ZonedDateTime timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
