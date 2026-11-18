package com.example.daily.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 集中处理指定 Controller 抛出的异常，并把返回对象写入 JSON 响应。
 * @RestControllerAdvice 让 Spring 发现这些异常处理方法；basePackages 限定作用范围。
 * 继承 ResponseEntityExceptionHandler 可以复用 Spring MVC 对常见请求错误的分类。
 */
@RestControllerAdvice(basePackages = "com.example.daily.controller")
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Controller 抛出 ResponseStatusException 时由此方法处理。
     * 例如日程不存在时，取出异常里的 404 和说明文字，分别写入 HTTP 状态和统一响应体。
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResult<Void>> handleStatus(ResponseStatusException exception) {
        HttpStatusCode status = exception.getStatusCode();
        String message = exception.getReason() == null ? "请求的资源不存在" : exception.getReason();
        return ResponseEntity.status(status).body(ApiResult.error(status.value(), message));
    }

    /**
     * Spring MVC 的常见请求异常由父类分派到这里，例如参数校验或 JSON 格式错误。
     * @Override 表示重写父类方法；status 和 headers 沿用 Spring 已判断出的 HTTP 响应信息。
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = "请求格式或参数有误";
        // instanceof 同时判断异常类型并绑定变量 validation，只有匹配且存在字段错误才进入分支。
        if (exception instanceof MethodArgumentNotValidException validation && validation.getBindingResult().hasFieldErrors()) {
            // 先展示第一条字段错误；var 的实际类型由右侧表达式推断。
            var field = validation.getBindingResult().getFieldErrors().get(0);
            message = field.getField() + "：" + field.getDefaultMessage();
        } else if (status.value() == 405) {
            message = "请求方法不支持";
        }
        // ResponseEntity 同时携带响应体、响应头和 HTTP 状态，ApiResult 管理 JSON 字段。
        return new ResponseEntity<>(ApiResult.error(status.value(), message), headers, status);
    }

    /**
     * 兜底处理其他未处理异常：日志保留异常及堆栈，便于排查。
     * 客户端只收到通用提示，避免把数据库等内部细节放进响应。
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<Void>> handleUnexpected(Exception exception) {
        log.error("接口处理失败", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResult.error(500, "服务器处理失败，请稍后重试"));
    }
}
