package com.example.daily.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** 一条已发生的打卡历史；字段由 MyBatis 映射并由 Controller 返回为 JSON。 */
public class CheckinRecord {
    public Long id;
    /** 原日程 ID；删除原日程后数据库会将其置为 null。 */
    public Long entryId;
    /** 打卡时的标题快照，用于保留历史。 */
    public String entryTitle;
    /** 按上海时区确定的打卡日期。 */
    public LocalDate checkinDate;
    /** 打卡时的本地日期与时间。 */
    public LocalDateTime checkedAt;
}
