package com.example.daily;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EntryService {
    private final EntryRepository repository;

    public EntryService(EntryRepository repository) {
        this.repository = repository;
    }

    public List<Entry> listEntries() {
        return repository.findAll();
    }

    @Transactional
    public Entry createEntry(Entry input) {
        input.id = null;
        repository.insert(input);
        return repository.findById(input.id);
    }

    @Transactional
    public Optional<Entry> updateEntry(Long id, Entry input) {
        if (repository.findById(id) == null) {
            return Optional.empty();
        }

        input.id = id;
        repository.update(input);
        return Optional.ofNullable(repository.findById(id));
    }

    public boolean deleteEntry(Long id) {
        return repository.deleteById(id) > 0;
    }
}
