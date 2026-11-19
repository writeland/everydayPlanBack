package com.example.daily.controller;

import com.example.daily.domain.CheckinRecord;
import com.example.daily.common.ApiResult;
import com.example.daily.common.PageResult;
import com.example.daily.service.CheckinRecordService;
import com.example.daily.service.EntryWorkflowService;

import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/** 处理打卡相关 HTTP 请求；返回的 Java 对象由 Spring MVC 转为 JSON。 */
@RestController
@RequestMapping("/api")
public class CheckinController {
    private final EntryWorkflowService service;
    private final CheckinRecordService recordService;

    /** 注入流程协调 Service 和记录查询 Service。 */
    public CheckinController(EntryWorkflowService service, CheckinRecordService recordService) {
        this.service = service;
        this.recordService = recordService;
    }

    /** GET /api/checkins；可选日期格式为 yyyy-MM-dd，查询结果放进统一响应的 data。 */
    @GetMapping("/checkins")
    public ApiResult<List<CheckinRecord>> list(@RequestParam(required = false)
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResult.success(recordService.listRecords(date));
    }

    /** 分页查询历史，日期条件与旧列表接口一致。 */
    @GetMapping("/checkins/page")
    public ApiResult<PageResult<CheckinRecord>> page(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResult.success(recordService.listPage(page, size, date));
    }

    /** 完成指定日程；Optional 为空时抛出异常，由全局异常处理器生成 404 响应。 */
    @PostMapping("/entries/{id}/check-in")
    public ApiResult<CheckinRecord> checkIn(@PathVariable Long id) {
        return ApiResult.success(service.checkIn(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "日程不存在")));
    }

    /** 恢复未完成；成功返回 data 为 null 的统一响应，日程不存在则交给异常处理器。 */
    @PostMapping("/entries/{id}/reopen")
    public ApiResult<Void> reopen(@PathVariable Long id) {
        if (service.reopen(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "日程不存在");
        }
        return ApiResult.success(null);
    }
}
