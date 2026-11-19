package com.example.daily.mapper;

import com.example.daily.domain.Entry;

import java.util.List;
import java.time.LocalDate;
import org.apache.ibatis.annotations.*;

/** MyBatis Mapper：接口方法与 SQL 对应，由 MyBatis 提供实现。 */
@Mapper
public interface EntryRepository {
    /** 查询全部日程；entry_date 与 Java 字段 date 名称不同，需显式映射。 */
    @Select("SELECT id, title, note, entry_date, reminder_time, done FROM entries ORDER BY entry_date ASC, id DESC")
    @Results(id = "entryMapping", value = {@Result(column = "entry_date", property = "date"), @Result(column = "reminder_time", property = "reminderTime")})
    List<Entry> findAll();
    /** 可按日程日期和完成状态过滤；分页和计数使用相同条件。 */
    @Select({"<script> SELECT id, title, note, entry_date, reminder_time, done FROM entries",
        "<where><if test='date != null'>AND entry_date = #{date}</if>",
        "<if test='done != null'>AND done = #{done}</if></where>",
        "ORDER BY entry_date ASC, id DESC LIMIT #{size} OFFSET #{offset}</script>"})
    @ResultMap("entryMapping")
    List<Entry> findPage(@Param("date") LocalDate date, @Param("done") Boolean done,
                         @Param("offset") long offset, @Param("size") int size);

    @Select({"<script> SELECT COUNT(*) FROM entries",
        "<where><if test='date != null'>AND entry_date = #{date}</if>",
        "<if test='done != null'>AND done = #{done}</if></where></script>"})
    long countPage(@Param("date") LocalDate date, @Param("done") Boolean done);
    /** 按主键查询；#{id} 是绑定参数，找不到时返回 null。 */
    @Select("SELECT id, title, note, entry_date, reminder_time, done FROM entries WHERE id = #{id}")
    @ResultMap("entryMapping")
    Entry findById(Long id);
    /** 锁定日程行，直到事务结束，供更新流程判断修改前的完成状态。 */
    @Select("SELECT id, title, note, entry_date, reminder_time, done FROM entries WHERE id = #{id} FOR UPDATE")
    @ResultMap("entryMapping")
    Entry findByIdForUpdate(Long id);
    /** 插入后将数据库生成的主键回填到 entry.id；返回受影响的行数。 */
    @Insert("INSERT INTO entries (title, note, entry_date, reminder_time, done) VALUES (#{title}, #{note}, #{date}, #{reminderTime}, #{done})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Entry entry);
    /** 按对象中的 ID 更新日程字段，返回受影响的行数。 */
    @Update("UPDATE entries SET title = #{title}, note = #{note}, entry_date = #{date}, reminder_time = #{reminderTime}, done = #{done} WHERE id = #{id}")
    int update(Entry entry);
    /** 只修改完成状态；@Param 为多个方法参数指定 SQL 中使用的名称。 */
    @Update("UPDATE entries SET done = #{done} WHERE id = #{id}")
    int updateDone(@Param("id") Long id, @Param("done") boolean done);
    /** 删除指定日程；外键配置使关联打卡记录保留，但 entry_id 会变为 null。 */
    @Delete("DELETE FROM entries WHERE id = #{id}")
    int deleteById(Long id);
}
