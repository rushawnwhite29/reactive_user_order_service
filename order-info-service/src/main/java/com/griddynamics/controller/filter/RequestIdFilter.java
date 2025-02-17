package com.griddynamics.controller.filter;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.jetbrains.annotations.NotNull;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.util.UUID;

/**
 * Filter to add a unique request ID to each incoming request for logging purposes.
 * This filter ensures that each request to the `/orders` endpoint has a unique ID
 * that is logged with the request details.
 */
@Component
public class RequestIdFilter extends CommonsRequestLoggingFilter {

    private static final String REQUEST_ID_HEADER = "requestId";

    /**
     * Determines whether the request should be logged.
     * Only requests to the `/orders` endpoint are logged.
     *
     * @param request the HTTP request
     * @return true if the request URI starts with `/orders`, false otherwise
     */
    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/orders");
    }

    /**
     * Adds a unique request ID to the request and the logging context before the request is processed.
     *
     * @param request the HTTP request
     * @param message the log message
     */
    @Override
    protected void beforeRequest(@NotNull HttpServletRequest request, @NotNull String message) {
        final String requestID = StringUtils.substringAfterLast(UUID.randomUUID().toString(), "-");

        request.setAttribute(REQUEST_ID_HEADER, requestID);
        ThreadContext.put(REQUEST_ID_HEADER, requestID);
    }

    /**
     * Removes the request ID from the request and the logging context after the request is processed.
     *
     * @param request the HTTP request
     * @param message the log message
     */
    @Override
    protected void afterRequest(HttpServletRequest request, @NotNull String message) {
        request.removeAttribute(REQUEST_ID_HEADER);
        ThreadContext.remove(REQUEST_ID_HEADER);
    }

}