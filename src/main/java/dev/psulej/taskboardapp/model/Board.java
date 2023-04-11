package dev.psulej.taskboardapp.model;

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
    private UUID id;

    @Field
    private String name;

    @DBRef
    private List<User> users;

    @DBRef
    private List<Column> columns;

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
