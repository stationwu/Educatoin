package com.edu.errorhandler;

import com.edu.domain.Payment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ResponseBody
    @ExceptionHandler(RequestDeniedException.class)
    public ResponseEntity<Object> handleRequestDeniedException(RequestDeniedException ex, WebRequest request) {
        String body = "Access Denied";
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ResponseBody
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        String body = "Resource Not Found";
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ResponseBody
    @ExceptionHandler(InvalidOrderException.class)
    public ResponseEntity<Object> handleInvalidOrderException(InvalidOrderException ex, WebRequest request) {
        String body = ex.getMessage();
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(PaymentException.class)
    public ModelAndView handlePaymentException(PaymentException ex, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", request.getRequestURL());
        mav.setViewName("payment_error");
        return mav;
    }
}
