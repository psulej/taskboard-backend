package dev.psulej.taskboardapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;



import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Document("columns")
public class Column {
    @Id
    private final UUID id;

    @Field
    private final String name;

    @DBRef
    private final List<Task> tasks;

    public Column(UUID id, String name, List<Task> tasks) {
        this.id = id;
        this.name = name;
        this.tasks = tasks;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public String toString() {
        return "Column{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tasks=" + tasks +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Column column = (Column) o;
        return id.equals(column.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
