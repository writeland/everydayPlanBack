package com.example.daily;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/entries")
public class EntryController {
    private final EntryService service;

    public EntryController(EntryService service) {
        this.service = service;
    }

    @GetMapping public List<Entry> list() { return service.listEntries(); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Entry create(@Valid @RequestBody Entry input) {
        return service.createEntry(input);
    }
    @PutMapping("/{id}") public Entry update(@PathVariable Long id, @Valid @RequestBody Entry input) {
        return service.updateEntry(id, input)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!service.deleteEntry(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
