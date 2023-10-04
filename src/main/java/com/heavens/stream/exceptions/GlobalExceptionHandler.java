package com.heavens.stream.exceptions;


import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.heavens.stream.response.FinalResponse;
import com.heavens.stream.response.Response;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {

		log.error(ex.getMessage(), ex);

		if (ex.getCause() instanceof UnknownHostException) {
			FinalResponse error = new FinalResponse(false, HttpStatus.NOT_FOUND.value(), "Unknown host",
					ex.getLocalizedMessage());
			return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		FinalResponse error = new FinalResponse(false, HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Could not finish processing your request", details);
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}


	public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		log.error(ex.getMessage(), ex);
		String paramName = ex.getParameterName();
		String message = paramName + " is required";
		FinalResponse response = new FinalResponse(false,  HttpStatus.BAD_REQUEST.value(), message, null);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		String msg = null;
		Throwable cause = ex.getCause();
		MismatchedInputException mie = (MismatchedInputException) cause;

		if (mie != null && mie.getPath() != null && mie.getPath().size() > 0) {
			msg = "Bad request" + mie.getPath().get(0).getFieldName();
		}


		log.error(ex.getMessage(), ex);
		FinalResponse response = new FinalResponse(false, HttpStatus.BAD_REQUEST.value(), msg, null);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NullPointerException.class)
	public final ResponseEntity<Object> handleNullPointerException(NullPointerException ex, WebRequest request) {
		log.error("NULL error occurred", ex);
		FinalResponse error = new FinalResponse(false, HttpStatus.INTERNAL_SERVER_ERROR.value(),
				ex.getLocalizedMessage(), ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}


	@ExceptionHandler({IllegalArgumentException.class,ValidationException.class, NumberFormatException.class})
	public final ResponseEntity<Object> handleValidationException(Exception ex) {

		log.error(ex.getLocalizedMessage(), ex);
		FinalResponse response = new FinalResponse(false, HttpStatus.BAD_REQUEST.value(), "Validation Failed",
				ex.getLocalizedMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}


	@ExceptionHandler(HttpClientErrorException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> handleClientError(HttpClientErrorException ex, WebRequest request) {

		log.error(ex.getLocalizedMessage(), ex);
		FinalResponse error = new FinalResponse(false, HttpStatus.BAD_REQUEST.value(),
				"Bad request", ex.getLocalizedMessage());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public final ResponseEntity<Object> handleMethodArgumentTypeMismatchException(
			MethodArgumentTypeMismatchException ex, WebRequest request) {

		log.error(ex.getLocalizedMessage(), ex);
		FinalResponse error = new FinalResponse(false, HttpStatus.BAD_REQUEST.value(),
				ex.getLocalizedMessage()+" please provide valid " + ex.getName(), null);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}


	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		log.error("MethodArgumentNotValidException Exception occurs : ", ex);
		List<String> errors = new ArrayList<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String errorMessage = error.getDefaultMessage();
			errors.add(errorMessage);
		});
		FinalResponse response = new FinalResponse(false,  HttpStatus.BAD_REQUEST.value(),
				errors.get(0), null);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ResponseBody
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.OK)
	public FinalResponse onConstraintValidationException(ConstraintViolationException e) {
		log.error(e.getMessage(), e);
		return new FinalResponse(false,  HttpStatus.BAD_REQUEST.value(), "Validation failed", e.getMessage());

	}

	@ResponseBody
	@ExceptionHandler(MissingRequestHeaderException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
		log.error("MissingRequestHeaderException Exception occurs : ", ex);
		FinalResponse error = new FinalResponse(false, HttpStatus.BAD_REQUEST.value(),
				"Bad request", ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<Object> handleApplicationException(ApplicationException ex) {
		log.error(ex.getHttpStatus().value()+" "+ex.getLocalizedMessage(), ex);
		return ResponseEntity.status(ex.getHttpStatus()).body(Response.failedResponse(ex.getHttpStatus().value(),
				ex.getMessage()));
	}

	@ExceptionHandler({InternalAuthenticationServiceException.class, NoSuchElementException.class})
	public ResponseEntity<Object> handleApplicationException(InternalAuthenticationServiceException ex) {
		log.error(HttpStatus.UNAUTHORIZED.value() + " " + ex.getLocalizedMessage(), ex);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(Response.failedResponse(HttpStatus.UNAUTHORIZED.value(),
				"Denied Authorized"));
	}


	@ExceptionHandler(DataIntegrityViolationException.class)
	public final ResponseEntity<Object> handleMethodArgumentTypeMismatchException(
			DataIntegrityViolationException ex) {
		log.error(ex.getLocalizedMessage(), ex);
		FinalResponse error = new FinalResponse(false, HttpStatus.CONFLICT.value(),
				"  Duplicate record found. "+ex.getLocalizedMessage(), null);
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}

}
