package com.example.daily;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Entry {
    public Long id;
    @NotBlank @Size(max = 120)
    public String title;
    @Size(max = 2000)
    public String note = "";
    @NotNull
    public LocalDate date;
    public LocalTime reminderTime;
    public boolean done;
}
