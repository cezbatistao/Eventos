package br.com.cdcorp.eventos.infrastructure.exception;

import lombok.AllArgsConstructor;

/**
 * Created by ceb on 02/07/17.
 */
public abstract class EventosException extends RuntimeException {

    public EventosException(String message, Throwable cause) {
        super(message, cause);
    }
}