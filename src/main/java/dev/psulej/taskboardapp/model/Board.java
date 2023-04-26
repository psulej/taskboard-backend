package dev.psulej.taskboardapp.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonValue;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.UUID;

@Document("boards")
public class Board {
    @Id
    private final UUID id;

    @Field
    private final String name;

    @DBRef
    private final List<User> users;

    @DBRef
    private final List<Column> columns;

    public Board(UUID id, String name, List<User> users, List<Column> columns) {
        this.id = id;
        this.name = name;
        this.users = users;
        this.columns = columns;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Column> getColumns() {
        return columns;
    }
}
