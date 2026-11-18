package com.example.daily.common;

/**
 * 接口统一响应：code 是业务码，data 是数据，msg 是给调用方的说明。
 * record 会根据这里声明的字段生成构造器和访问方法；Spring 会将它转换成 JSON。
 * T 是泛型占位符：ApiResult<Entry> 的 data 是日程，
 * ApiResult<List<Entry>> 的 data 则是日程列表。
 */
public record ApiResult<T>(int code, T data, String msg) {
    /**
     * 静态工厂方法：调用 ApiResult.success(data) 即可创建成功响应。
     * static 后面的 <T> 为这个静态方法声明自己的类型参数；Java 通常能根据 data 推断类型。
     * 本项目约定业务成功码为 0；操作没有数据时可以传入 null。
     */
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(0, data, "成功");
    }

    /**
     * 创建失败响应。Void 表示此处没有业务数据，实际 data 填 null。
     * 这里约定业务码取对应的 HTTP 状态码；响应的 HTTP 状态仍要由 Controller 或异常处理器设置。
     */
    public static ApiResult<Void> error(int code, String msg) {
        return new ApiResult<>(code, null, msg);
    }
}
