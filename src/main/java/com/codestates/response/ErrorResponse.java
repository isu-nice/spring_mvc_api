package com.codestates.response;

import com.codestates.exception.ExceptionCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ErrorResponse {

    private Integer status;
    private String message;

    // MethodArgumentNotValidException 으로부터 발생하는 에러 정보를 담는 멤버 변수
    // -> DTO 멤버 변수 필드의 유효성 검증 실패로 발생한 에러 정보를 담는 멤버 변수
    private List<FieldError> fieldErrors;
    // ConstraintViolationException 으로부터 발생하는 에러 정보를 담는 멤버 변수
    // -> URI 변수 값의 유효성 검증 실패로 발생한 에러 정보를 담는 멤버 변수
    private List<ConstraintViolationError> violationErrors;

    // constructor 추가
    public ErrorResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    // private 로 설정 -> new 하지 못하도록 함
    // -> of() 메서드를 이용해서 ErrorResponse 객체를 생성할 수 있음
    private ErrorResponse(List<FieldError> fieldErrors, List<ConstraintViolationError> violationErrors) {
        this.fieldErrors = fieldErrors;
        this.violationErrors = violationErrors;
    }

    // MethodArgumentNotValidException 에 대한 ErrorResponse 객체 생성
    // -> 에러 정보를 얻기 위해 필요한 것이 BindingResult 객체이므로, 파라미터로 넘겨받음
    public static ErrorResponse of(BindingResult bindingResult) {
        // BindingResult 객체로 에러 정보를 추출하고 가공하는 일은 FieldError 클래스에 위임
        return new ErrorResponse(FieldError.of(bindingResult), null);
    }

    // Set<ConstraintViolation<?>> 객체에 대한 ErrorResponse 객체 생성
    // -> 에러 정보를 얻기 위해 필요한 것이 Set<ConstraintViolation<?>> 객체이므로, 파라미터로 넘겨받음
    public static ErrorResponse of(Set<ConstraintViolation<?>> violations) {
        // Set<ConstraintViolation<?>> 객체로 에러 정보를 추출하고 가공하는 일은 ConstraintViolationError 클래스에 위임
        return new ErrorResponse(null, ConstraintViolationError.of(violations));
    }

    public static ErrorResponse of(ExceptionCode exceptionCode) {
        return new ErrorResponse(exceptionCode.getStatus(), exceptionCode.getMessage());
    }

    public static ErrorResponse of(HttpStatus httpStatus) {
        return new ErrorResponse(httpStatus.value(), httpStatus.getReasonPhrase());
    }

    // 1. 필드(DTO 클래스의 멤버 변수)의 유효성 검증에서 발생하는 에러 정보를 생성
    @Getter
    public static class FieldError {
        private String field;
        private Object rejectedValue;
        private String reason;

        public FieldError(String field, Object rejectedValue, String reason) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.reason = reason;
        }


        public static List<FieldError> of(BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors =
                    bindingResult.getFieldErrors();

            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ?
                                    "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }


    // 2. URI 변수 값에 대한 에러 정보 생성
    @Getter
    public static class ConstraintViolationError {
        private String propertyPath;
        private Object rejectedValue;
        private String reason;

        public ConstraintViolationError(String propertyPath, Object rejectedValue, String reason) {
            this.propertyPath = propertyPath;
            this.rejectedValue = rejectedValue;
            this.reason = reason;
        }

        public static List<ConstraintViolationError> of(
                Set<ConstraintViolation<?>> constraintViolations) {

            return constraintViolations.stream()
                    .map(constraintViolation -> new ConstraintViolationError(
                            constraintViolation.getPropertyPath().toString(),
                            constraintViolation.getInvalidValue().toString(),
                            constraintViolation.getMessage()))
                    .collect(Collectors.toList());
        }
    }
}
