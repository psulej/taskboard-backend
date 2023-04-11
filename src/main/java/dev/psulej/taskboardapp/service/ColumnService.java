package dev.psulej.taskboardapp.service;
import dev.psulej.taskboardapp.repository.ColumnRepository;
import org.springframework.stereotype.Service;

@Service
public class ColumnService {
    ColumnRepository columnRepository;
    public ColumnService(ColumnRepository columnRepository) {
        this.columnRepository = columnRepository;
    }
}
