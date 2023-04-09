package dev.psulej.taskboardapp.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document
public class Board {
    @Id
    private Long boardId;
    @Field
    private String boardName;
    @Field
    private List<User> userList;
    @Field
    private List<Column> columnsList;
}
