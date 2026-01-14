package com.sanchitp.dev.task.management.system.common.enums.exception;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    /* ========== 404 ========== */

    @ExceptionHandler({UserNotFoundException.class,TaskNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleNotFound(RuntimeException ex, HttpServletRequest request) {

        return buildResponse(HttpStatus.NOT_FOUND,ex.getMessage(),request);
    }

    /* ========== 403 ========== */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request
    ){
        return buildResponse(HttpStatus.FORBIDDEN,ex.getMessage(),request);
    }

    /* ========== 400 ========== */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request                                                 ){
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        return buildResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    /* ========== 500 ========== */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(
            Exception ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected server error",
                request
        );
    }
    /* ========== HELPER ========== */

    private ResponseEntity<ApiErrorResponse> buildResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request
    ) {
        ApiErrorResponse error = new ApiErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, status);
    }

}