package com.example.daily;

import java.util.List;
import org.apache.ibatis.annotations.*;

@Mapper
public interface EntryRepository {
    @Select("SELECT id, title, note, entry_date, reminder_time, done FROM entries ORDER BY entry_date ASC, id DESC")
    @Results(id = "entryMapping", value = {@Result(column = "entry_date", property = "date"), @Result(column = "reminder_time", property = "reminderTime")})
    List<Entry> findAll();
    @Select("SELECT id, title, note, entry_date, reminder_time, done FROM entries WHERE id = #{id}")
    @ResultMap("entryMapping")
    Entry findById(Long id);
    @Insert("INSERT INTO entries (title, note, entry_date, reminder_time, done) VALUES (#{title}, #{note}, #{date}, #{reminderTime}, #{done})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Entry entry);
    @Update("UPDATE entries SET title = #{title}, note = #{note}, entry_date = #{date}, reminder_time = #{reminderTime}, done = #{done} WHERE id = #{id}")
    int update(Entry entry);
    @Delete("DELETE FROM entries WHERE id = #{id}")
    int deleteById(Long id);
}
