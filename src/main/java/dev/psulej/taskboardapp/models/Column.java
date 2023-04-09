package dev.psulej.taskboardapp.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document
public class Column {
    @Id
    private Long columnId;
    @Field
    private String columnName;
    @Field
    private List<Task> taskList;
}
