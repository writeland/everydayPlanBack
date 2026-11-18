package com.example.daily.domain;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

/** 日程数据对象：同时用于接口数据绑定和 MyBatis 查询结果。 */
public class Entry {
    /** 数据库生成的主键；Long 可用 null 表示尚未生成。 */
    public Long id;
    /** 标题不能为空白，最多 120 个字符；由 Controller 的 @Valid 触发校验。 */
    @NotBlank @Size(max = 120)
    public String title;
    /** 备注可为空；如果有内容，最多 2000 个字符。 */
    @Size(max = 2000)
    public String note = "";
    /** 日程日期必填；LocalDate 表示年月日，不包含具体时间。 */
    @NotNull
    public LocalDate date;
    /** 可选提醒时间，只包含时分秒。 */
    public LocalTime reminderTime;
    /** 当前完成状态；它与单独保存的打卡历史不是同一份数据。 */
    public boolean done;
}
