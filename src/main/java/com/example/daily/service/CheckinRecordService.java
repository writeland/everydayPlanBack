package com.example.daily.service;

import com.example.daily.domain.CheckinRecord;
import com.example.daily.domain.Entry;
import com.example.daily.common.PageResult;
import com.example.daily.mapper.CheckinRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 管理打卡历史；与日程的当前完成状态分别存储。 */
@Service
public class CheckinRecordService {
    // 用上海时区确定“今天”，避免依赖服务器所在时区。
    private static final ZoneId APP_ZONE = ZoneId.of("Asia/Shanghai");

    private final CheckinRepository repository;

    /** 注入负责打卡记录 SQL 的 MyBatis Mapper。 */
    public CheckinRecordService(CheckinRepository repository) {
        this.repository = repository;
    }

    /** 未传日期查询全部记录；传入日期则只查询对应的一天。 */
    public List<CheckinRecord> listRecords(LocalDate date) {
        return date == null ? repository.findAll() : repository.findByDate(date);
    }

    /** 查询打卡历史的一页；date 为空表示不限制日期。 */
    public PageResult<CheckinRecord> listPage(int page, int size, LocalDate date) {
        long offset = PageResult.offset(page, size);
        return new PageResult<>(repository.findPage(date, offset, size), repository.countPage(date), page, size);
    }

    /**
     * 为日程记录当天的打卡：若已有记录就返回原记录，否则新增一条。
     * entryTitle 是打卡时的标题快照，日程后续改名或删除不会改变这份历史。
     * 默认 REQUIRED 传播方式会加入调用者已有的事务。
     * 数据库唯一键还会限制同一日程同一天的重复记录；并发请求可能因唯一键冲突失败。
     */
    @Transactional
    public CheckinRecord recordCompletion(Entry entry) {
        LocalDateTime now = LocalDateTime.now(APP_ZONE);
        LocalDate today = now.toLocalDate();
        CheckinRecord existing = repository.findByEntryAndDate(entry.id, today);
        if (existing != null) {
            return existing;
        }

        CheckinRecord record = new CheckinRecord();
        record.entryId = entry.id;
        record.entryTitle = entry.title;
        record.checkinDate = today;
        record.checkedAt = now;
        repository.insert(record);
        return record;
    }
}
