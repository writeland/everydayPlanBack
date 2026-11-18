package com.example.daily.service;

import com.example.daily.domain.Entry;
import com.example.daily.mapper.EntryRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 处理日程业务；通过 EntryRepository 操作 entries 表。 */
@Service
public class EntryService {
    private final EntryRepository repository;

    /** Spring 注入 MyBatis 创建的 Mapper 对象，无需手动 new。 */
    public EntryService(EntryRepository repository) {
        this.repository = repository;
    }

    /** 查询所有日程，排序规则定义在 Mapper 的 SQL 中。 */
    public List<Entry> listEntries() {
        return repository.findAll();
    }

    /**
     * 新建日程：忽略客户端传入的 ID 和完成状态，再插入并读取新记录。
     * MyBatis 会将数据库生成的主键写入 input.id，供后面的查询使用。
     * 事务在此方法经 Spring 代理调用时生效，符合回滚规则的异常会撤销插入。
     */
    @Transactional
    public Entry createEntry(Entry input) {
        input.id = null;
        input.done = false;
        repository.insert(input);
        return repository.findById(input.id);
    }

    /** 根据路径中的 ID 更新日程；找不到时返回空 Optional，交给 Controller 处理。 */
    @Transactional
    public Optional<Entry> updateEntry(Long id, Entry input) {
        if (repository.findById(id) == null) {
            return Optional.empty();
        }

        input.id = id;
        repository.update(input);
        return Optional.ofNullable(repository.findById(id));
    }

    /** 标记日程完成并返回最新数据；已完成时不重复更新状态。 */
    @Transactional
    public Optional<Entry> markDone(Long id) {
        Entry existing = repository.findById(id);
        if (existing == null) {
            return Optional.empty();
        }

        if (!existing.done) {
            repository.updateDone(id, true);
        }
        return Optional.ofNullable(repository.findById(id));
    }

    /** 恢复未完成状态；该方法不删除历史打卡记录。 */
    @Transactional
    public Optional<Entry> markUndone(Long id) {
        Entry existing = repository.findById(id);
        if (existing == null) {
            return Optional.empty();
        }

        if (existing.done) {
            repository.updateDone(id, false);
        }
        return Optional.ofNullable(repository.findById(id));
    }

    /** DELETE 返回受影响的行数；大于 0 表示找到并删除了日程。 */
    public boolean deleteEntry(Long id) {
        return repository.deleteById(id) > 0;
    }
}
