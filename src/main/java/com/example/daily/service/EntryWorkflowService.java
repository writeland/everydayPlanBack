package com.example.daily.service;

import com.example.daily.domain.CheckinRecord;
import com.example.daily.domain.Entry;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 协调涉及日程和打卡记录的业务流程；具体 SQL 仍交给各自的 Mapper。 */
@Service
public class EntryWorkflowService {
    private final EntryService entryService;
    private final CheckinRecordService checkinRecordService;

    /** 构造器注入：Spring 创建本对象时，提供两个已经由容器管理的 Service。 */
    public EntryWorkflowService(EntryService entryService, CheckinRecordService checkinRecordService) {
        this.entryService = entryService;
        this.checkinRecordService = checkinRecordService;
    }

    /**
     * 更新日程；如果更新后的状态是已完成，确保当天有一条打卡记录。
     * 外层事务把更新和记录放在一起：符合回滚规则的异常向外抛出时，两步一起回滚。
     * Optional.map 只在日程存在时执行括号内的函数；不存在则继续返回 empty。
     */
    @Transactional
    public Optional<Entry> updateEntry(Long entryId, Entry input) {
        return entryService.updateEntry(entryId, input).map(entry -> {
            if (entry.done) {
                checkinRecordService.recordCompletion(entry);
            }
            return entry;
        });
    }

    /** 完成日程并记录打卡；方法引用相当于 entry -> checkinRecordService.recordCompletion(entry)。 */
    @Transactional
    public Optional<CheckinRecord> checkIn(Long entryId) {
        return entryService.markDone(entryId).map(checkinRecordService::recordCompletion);
    }

    /** 将日程恢复为未完成，已经产生的打卡历史仍然保留。 */
    @Transactional
    public Optional<Entry> reopen(Long entryId) {
        return entryService.markUndone(entryId);
    }
}
