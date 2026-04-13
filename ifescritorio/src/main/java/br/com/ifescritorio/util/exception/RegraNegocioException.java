package br.com.ifescritorio.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String msg) {
        super(msg);
    }
}