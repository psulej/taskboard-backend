package dev.psulej.taskboardapp.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class Task {
    @Id
    private Long taskId;
    @Field
    private String title;
    @Field
    private String description;
}
