package com.example.daily.common;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/** 一页的数据及分页信息；page 从 1 开始。 */
public record PageResult<T>(List<T> items, long total, int page, int size) {
    /** 验证请求并计算 SQL OFFSET，使用 long 避免大页码整数溢出。 */
    public static long offset(int page, int size) {
        if (page < 1 || size < 1 || size > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "页码必须大于等于1，每页数量必须为1到100");
        }
        return ((long) page - 1) * size;
    }
}
