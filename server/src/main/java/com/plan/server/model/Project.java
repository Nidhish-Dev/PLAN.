package com.plan.server.model;

import com.plan.server.enums.Status;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "projects")
@Data
public class Project {
    @Id
    private String id;
    private String userId;
    private String name;
    private String description;
    private String timeline;
    private List<Subtask> subtasks;
    private Status status;
}