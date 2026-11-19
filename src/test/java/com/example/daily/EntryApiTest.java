package com.example.daily;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:dailytest;MODE=MySQL;DB_CLOSE_DELAY=-1",
    "spring.datasource.username=sa", "spring.datasource.password=", "spring.sql.init.mode=never"
})
@AutoConfigureMockMvc
class EntryApiTest {
    @Autowired MockMvc mvc;
    @Autowired JdbcTemplate jdbc;

    @Test void editingCompletedEntryDoesNotCreateAnotherCheckin() throws Exception {
        jdbc.execute("CREATE TABLE IF NOT EXISTS entries (id BIGINT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(120) NOT NULL, note VARCHAR(2000), entry_date DATE NOT NULL, reminder_time TIME, done BOOLEAN NOT NULL DEFAULT FALSE)");
        jdbc.execute("CREATE TABLE IF NOT EXISTS checkin_records (id BIGINT AUTO_INCREMENT PRIMARY KEY, entry_id BIGINT, entry_title VARCHAR(120) NOT NULL, checkin_date DATE NOT NULL, checked_at TIMESTAMP NOT NULL, CONSTRAINT uk_checkin_entry_date UNIQUE (entry_id, checkin_date), CONSTRAINT fk_checkin_entry FOREIGN KEY (entry_id) REFERENCES entries(id) ON DELETE SET NULL)");
        jdbc.update("DELETE FROM checkin_records");
        jdbc.update("DELETE FROM entries");
        jdbc.update("INSERT INTO entries(title, entry_date, done) VALUES ('原日程', '2000-01-01', true)");
        Long id = jdbc.queryForObject("SELECT id FROM entries", Long.class);
        jdbc.update("INSERT INTO checkin_records(entry_id, entry_title, checkin_date, checked_at) VALUES (?, '原日程', '2000-01-01', '2000-01-01 10:00:00')", id);
        String completed = "{\"title\":\"修改备注\",\"date\":\"2000-01-01\",\"done\":true}";
        mvc.perform(put("/api/entries/{id}", id).contentType(MediaType.APPLICATION_JSON).content(completed))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.done").value(true));
        org.junit.jupiter.api.Assertions.assertEquals(1, jdbc.queryForObject("SELECT COUNT(*) FROM checkin_records", Integer.class));
        String pending = "{\"title\":\"修改备注\",\"date\":\"2000-01-01\",\"done\":false}";
        mvc.perform(put("/api/entries/{id}", id).contentType(MediaType.APPLICATION_JSON).content(pending)).andExpect(status().isOk());
        mvc.perform(put("/api/entries/{id}", id).contentType(MediaType.APPLICATION_JSON).content(completed)).andExpect(status().isOk());
        org.junit.jupiter.api.Assertions.assertEquals(2, jdbc.queryForObject("SELECT COUNT(*) FROM checkin_records", Integer.class));
    }

    @Test void paginatesAndFiltersLists() throws Exception {
        jdbc.execute("CREATE TABLE IF NOT EXISTS entries (id BIGINT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(120) NOT NULL, note VARCHAR(2000), entry_date DATE NOT NULL, reminder_time TIME, done BOOLEAN NOT NULL DEFAULT FALSE)");
        jdbc.execute("CREATE TABLE IF NOT EXISTS checkin_records (id BIGINT AUTO_INCREMENT PRIMARY KEY, entry_id BIGINT, entry_title VARCHAR(120) NOT NULL, checkin_date DATE NOT NULL, checked_at TIMESTAMP NOT NULL, CONSTRAINT uk_checkin_entry_date UNIQUE (entry_id, checkin_date), CONSTRAINT fk_checkin_entry FOREIGN KEY (entry_id) REFERENCES entries(id) ON DELETE SET NULL)");
        jdbc.update("DELETE FROM checkin_records");
        jdbc.update("DELETE FROM entries");
        jdbc.update("INSERT INTO entries(title,entry_date,done) VALUES ('A','2026-12-01',false),('B','2026-12-01',false),('C','2026-12-01',true),('D','2026-12-02',false)");
        jdbc.update("INSERT INTO checkin_records(entry_title,checkin_date,checked_at) VALUES ('A','2026-12-01','2026-12-01 10:00:00'),('B','2026-12-01','2026-12-01 11:00:00'),('C','2026-12-02','2026-12-02 10:00:00')");
        mvc.perform(get("/api/checkins").param("date", "2026-12-01"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.length()").value(2))
            .andExpect(jsonPath("$.data[0].entryTitle").value("B"))
            .andExpect(jsonPath("$.data[1].entryTitle").value("A"));
        mvc.perform(get("/api/checkins").param("date", "2026-12-03"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.length()").value(0));
        mvc.perform(get("/api/entries/page").param("page", "2").param("size", "1").param("date", "2026-12-01").param("done", "false"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.total").value(2)).andExpect(jsonPath("$.data.items[0].title").value("A"));
        mvc.perform(get("/api/checkins/page").param("page", "2").param("size", "1").param("date", "2026-12-01"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.total").value(2)).andExpect(jsonPath("$.data.items[0].entryTitle").value("A"));
        mvc.perform(get("/api/checkins/page").param("page", "1").param("size", "2"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.total").value(3))
            .andExpect(jsonPath("$.data.items.length()").value(2)).andExpect(jsonPath("$.data.items[0].entryTitle").value("C"));
        mvc.perform(get("/api/checkins/page").param("date", "2026-12-03"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.total").value(0)).andExpect(jsonPath("$.data.items.length()").value(0));
        mvc.perform(get("/api/entries/page").param("page", "0"))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
        mvc.perform(get("/api/checkins/page").param("size", "101"))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
        mvc.perform(get("/api/entries/page").param("page", "999").param("size", "2"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.items.length()").value(0));
    }

    @Test void createsUpdatesAndDeletesEntries() throws Exception {
        jdbc.execute("CREATE TABLE IF NOT EXISTS entries (id BIGINT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(120) NOT NULL, note VARCHAR(2000), entry_date DATE NOT NULL, reminder_time TIME, done BOOLEAN NOT NULL DEFAULT FALSE)");
        jdbc.execute("CREATE TABLE IF NOT EXISTS checkin_records (id BIGINT AUTO_INCREMENT PRIMARY KEY, entry_id BIGINT, entry_title VARCHAR(120) NOT NULL, checkin_date DATE NOT NULL, checked_at TIMESTAMP NOT NULL, CONSTRAINT uk_checkin_entry_date UNIQUE (entry_id, checkin_date), CONSTRAINT fk_checkin_entry FOREIGN KEY (entry_id) REFERENCES entries(id) ON DELETE SET NULL)");
        jdbc.update("DELETE FROM checkin_records");
        jdbc.update("DELETE FROM entries");
        String body = "{\"title\":\"学习 Java\",\"date\":\"2026-12-01\",\"note\":\"备注\",\"reminderTime\":\"09:30\",\"done\":false}";
        mvc.perform(post("/api/entries").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.code").value(0)).andExpect(jsonPath("$.data.title").value("学习 Java"));
        Long id = jdbc.queryForObject("SELECT id FROM entries", Long.class);
        mvc.perform(get("/api/entries")).andExpect(status().isOk()).andExpect(jsonPath("$.data[0].date").value("2026-12-01"));
        String updated = "{\"title\":\"已完成\",\"date\":\"2026-12-01\",\"done\":true}";
        mvc.perform(put("/api/entries/{id}", id).contentType(MediaType.APPLICATION_JSON).content(updated))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.done").value(true));
        mvc.perform(delete("/api/entries/{id}", id)).andExpect(status().isOk()).andExpect(jsonPath("$.code").value(0));
        mvc.perform(delete("/api/entries/{id}", id)).andExpect(status().isNotFound()).andExpect(jsonPath("$.code").value(404)).andExpect(jsonPath("$.msg").value("日程不存在"));
        mvc.perform(post("/api/entries").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\" \",\"date\":\"2026-12-01\"}"))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400)).andExpect(jsonPath("$.msg").isNotEmpty());
    }
}
