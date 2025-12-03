package com.plan.server.model;

import com.plan.server.enums.Priority;
import lombok.Data;

@Data
public class Subtask {
    private String description;
    private Priority priority;
}