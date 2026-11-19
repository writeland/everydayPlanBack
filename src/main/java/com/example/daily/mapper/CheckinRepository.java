package com.example.daily.mapper;

import com.example.daily.domain.CheckinRecord;

import java.time.LocalDate;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/** 打卡记录的数据访问接口；下划线列名通过 MyBatis 配置映射到驼峰字段。 */
@Mapper
public interface CheckinRepository {
    /** 根据打卡日期筛选并按时间倒序分页；ID 作为稳定排序的最后条件。 */
    List<CheckinRecord> findPage(@Param("date") LocalDate date, @Param("offset") long offset, @Param("size") int size);

    /** 按相同日期条件统计总数，供分页信息使用。 */
    long countPage(@Param("date") LocalDate date);
    /** 查询全部历史，最近日期与时间排在前面。 */
    @Select("SELECT id, entry_id, entry_title, checkin_date, checked_at FROM checkin_records ORDER BY checkin_date DESC, checked_at DESC, id DESC")
    List<CheckinRecord> findAll();

    /** 只查询指定打卡日期的历史。 */
    List<CheckinRecord> findByDate(@Param("date") LocalDate date);

    /** 查询某条日程当天的记录；不存在时返回 null。 */
    @Select("SELECT id, entry_id, entry_title, checkin_date, checked_at FROM checkin_records WHERE entry_id = #{entryId} AND checkin_date = #{date}")
    CheckinRecord findByEntryAndDate(@Param("entryId") Long entryId, @Param("date") LocalDate date);

    /** 插入历史记录，并把数据库生成的主键回填到 record.id。 */
    @Insert("INSERT INTO checkin_records (entry_id, entry_title, checkin_date, checked_at) VALUES (#{entryId}, #{entryTitle}, #{checkinDate}, #{checkedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CheckinRecord record);
}
