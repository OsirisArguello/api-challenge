package ar.example.apichallenge.api.rest.handler;

import ar.example.apichallenge.api.rest.dto.ErrorResponseDTO;
import ar.example.apichallenge.application.exception.TransactionCreationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({TransactionCreationException.class})
    public ResponseEntity<ErrorResponseDTO> handleTransactionCreationException(TransactionCreationException ex) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(ex.getCode(),
                ex.getMessage());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO("error.invalid.argument",
                "Invalid argument on the body sent");
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
                                                               HttpStatus status, WebRequest request) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO("error.missing.path.variable",
                "Missing a path variable on the method call");
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }
}
