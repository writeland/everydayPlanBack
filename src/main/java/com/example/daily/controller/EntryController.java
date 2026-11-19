package com.example.daily.controller;

import com.example.daily.domain.Entry;
import com.example.daily.common.ApiResult;
import com.example.daily.common.PageResult;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import com.example.daily.service.EntryService;
import com.example.daily.service.EntryWorkflowService;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/** 日程 HTTP 入口；类上的路径前缀与各方法的映射路径组合。 */
@RestController
@RequestMapping("/api/entries")
public class EntryController {
    private final EntryService service;
    private final EntryWorkflowService workflowService;

    /** Spring 通过构造器注入日程 Service 和业务流程 Service。 */
    public EntryController(EntryService service, EntryWorkflowService workflowService) {
        this.service = service;
        this.workflowService = workflowService;
    }

    /** GET /api/entries：先查出列表，再用 ApiResult 包装；Spring MVC 将结果转换为 JSON。 */
    @GetMapping public ApiResult<List<Entry>> list() { return ApiResult.success(service.listEntries()); }
    /** page 从 1 开始；date 和 done 可选，先筛选再分页。 */
    @GetMapping("/page")
    public ApiResult<PageResult<Entry>> page(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Boolean done) {
        return ApiResult.success(service.listPage(page, size, date, done));
    }
    /** POST /api/entries：请求体 JSON 转为 Entry，@Valid 触发字段校验；成功返回 201。 */
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public ApiResult<Entry> create(@Valid @RequestBody Entry input) {
        return ApiResult.success(service.createEntry(input));
    }
    /**
     * PUT /api/entries/{id}：路径 ID 指定目标。
     * Service 返回 Optional；orElseThrow 在结果为空时抛出异常，由全局异常处理器返回 404。
     */
    @PutMapping("/{id}") public ApiResult<Entry> update(@PathVariable Long id, @Valid @RequestBody Entry input) {
        return ApiResult.success(workflowService.updateEntry(id, input)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "日程不存在")));
    }
    /** DELETE /api/entries/{id}：成功时 data 为 null；不存在时抛出异常交给全局处理。 */
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        if (!service.deleteEntry(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "日程不存在");
        return ApiResult.success(null);
    }
}
