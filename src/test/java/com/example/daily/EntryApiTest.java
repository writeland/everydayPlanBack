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

    @Test void createsUpdatesAndDeletesEntries() throws Exception {
        jdbc.execute("CREATE TABLE IF NOT EXISTS entries (id BIGINT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(120) NOT NULL, note VARCHAR(2000), entry_date DATE NOT NULL, reminder_time TIME, done BOOLEAN NOT NULL DEFAULT FALSE)");
        jdbc.execute("CREATE TABLE IF NOT EXISTS checkin_records (id BIGINT AUTO_INCREMENT PRIMARY KEY, entry_id BIGINT, entry_title VARCHAR(120) NOT NULL, checkin_date DATE NOT NULL, checked_at TIMESTAMP NOT NULL, CONSTRAINT uk_checkin_entry_date UNIQUE (entry_id, checkin_date), CONSTRAINT fk_checkin_entry FOREIGN KEY (entry_id) REFERENCES entries(id) ON DELETE SET NULL)");
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
