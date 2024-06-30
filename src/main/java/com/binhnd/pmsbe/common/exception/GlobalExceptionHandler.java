package com.binhnd.pmsbe.common.exception;

import com.binhnd.pmsbe.common.enums.EnumPMSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String TRACE_ID_S = "TraceId: %s";
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        String traceId = UUID.randomUUID().toString();
        logException(traceId, (HttpServletRequest) request, ex);

        List<PMSExceptionValidationMsg> details = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                PMSExceptionValidationMsg detail = PMSExceptionValidationMsg.builder()
                        .objectName(fieldError.getObjectName())
                        .field(fieldError.getField())
                        .messageDefault(fieldError.getDefaultMessage())
                        .build();
                details.add(detail);
            }
        }

        PMSExceptionValidationResponse error = new PMSExceptionValidationResponse(status, "VALIDATION", details);
        return super.handleMethodArgumentNotValid(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        String traceId = UUID.randomUUID().toString();
        logException(traceId, (HttpServletRequest) request, ex);

        String errorCode = "MALFORMED_JSON_REQUEST";
        PMSExceptionMsg PMSExceptionMsg = new PMSExceptionMsg(
                "exception.malformed-json-request",
                ex.getMessage(),
                List.of(String.format(TRACE_ID_S, traceId)));

        PMSExceptionResponse PMSExceptionResponse = new PMSExceptionResponse(status, errorCode, PMSExceptionMsg);
        return new ResponseEntity<>(PMSExceptionResponse, HttpStatus.BAD_REQUEST);
    }


    private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
        try {
            return new String(contentAsByteArray, 0, contentAsByteArray.length, characterEncoding);
        } catch (UnsupportedEncodingException e) {
            LOGGER.info("getStringValue", e);
        }
        return "";
    }

    private void logException(String traceId, HttpServletRequest request, Exception ex) {
        if (request != null) {
            String url = request.getRequestURI();
            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
            String queryString = requestWrapper.getQueryString();
            Enumeration<String> headerNames = requestWrapper.getHeaderNames();
            HashMap<String, String> headers = new HashMap<>();

            while (headerNames.hasMoreElements()) {
                String key = headerNames.nextElement();
                headers.put(key, requestWrapper.getHeader(key));
            }

            String requestBody = getStringValue(requestWrapper.getContentAsByteArray(), request.getCharacterEncoding());

            LOGGER.error(String.format("TraceId: %s\n" + "URL: %s\n" + "QueryParam: %s\n" + "RequestHeader: %s\n" + "RequestBody: %s", traceId, url, queryString, headers, requestBody), ex); //NOSONAR
        }
    }

    @ExceptionHandler({RuntimeException.class, Exception.class})
    @ResponseBody
    public ResponseEntity<PMSExceptionResponse> handleRuntimeException(Exception ex, HttpServletRequest request) {
        String traceId = UUID.randomUUID().toString();
        logException(traceId, request, ex);
        PMSExceptionMsg PMSExceptionMsg = new PMSExceptionMsg(
                "error.unknown",
                "Lỗi không xác định, vui lòng liên hệ quản trị viên",
                List.of(String.format(TRACE_ID_S, traceId)));
        PMSExceptionResponse PMSExceptionResponse = new PMSExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "UnknownError",
                PMSExceptionMsg);
        return new ResponseEntity<>(PMSExceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<PMSExceptionResponse> handleConstraintViolationException(
            ConstraintViolationException ex,
            WebRequest request) {
        String traceId = UUID.randomUUID().toString();
        logException(traceId, (HttpServletRequest) request, ex);

        PMSExceptionMsg PMSExceptionMsg = new PMSExceptionMsg(
                "exception.constraint-violation",
                ex.getMessage(),
                List.of(String.format(TRACE_ID_S, traceId)));

        PMSExceptionResponse PMSExceptionResponse = new PMSExceptionResponse(
                HttpStatus.BAD_REQUEST,
                "ConstraintViolationException",
                PMSExceptionMsg);
        return new ResponseEntity<>(PMSExceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PMSException.class)
    @ResponseBody
    public ResponseEntity<PMSExceptionResponse> handlePMSException(HttpServletRequest request, PMSException e) {
        String traceId = UUID.randomUUID().toString();
        logException(traceId, request, e);
        EnumPMSException enumPMSException = e.getEnumPMSException();

        List<String> params = new ArrayList<>();

        if (ObjectUtils.isEmpty(e.getMsgParams())) {
            params.addAll(e.getMsgParams());
        }

        params.add(String.format(TRACE_ID_S, traceId));

        return getPMSExceptionResponse(enumPMSException, params, e.getEx());
    }


    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<Object> handleMultipartException(MultipartException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


    private ResponseEntity<PMSExceptionResponse> getPMSExceptionResponse(
            EnumPMSException enumPMSException,
            List<String> params,
            Exception ex) {
        PMSExceptionMsg PMSExceptionMsg = null;

        if (ex != null) {
            PMSExceptionMsg = new PMSExceptionMsg(enumPMSException.getMessageKey(), ex.getMessage(), params);
        } else {
            PMSExceptionMsg = new PMSExceptionMsg(
                    enumPMSException.getMessageKey(),
                    enumPMSException.getMessageDefault(),
                    params);
        }

        PMSExceptionResponse PMSExceptionResponse = new PMSExceptionResponse(
                enumPMSException.getHttpStatus(),
                enumPMSException.getErrorCode(),
                PMSExceptionMsg);

        return new ResponseEntity<>(PMSExceptionResponse, enumPMSException.getHttpStatus());
    }
}
