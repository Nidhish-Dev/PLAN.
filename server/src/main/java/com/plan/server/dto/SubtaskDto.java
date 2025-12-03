package com.plan.server.dto;

import com.plan.server.enums.Priority;
import lombok.Data;

@Data
public class SubtaskDto {
    private String description;
    private Priority priority;
}