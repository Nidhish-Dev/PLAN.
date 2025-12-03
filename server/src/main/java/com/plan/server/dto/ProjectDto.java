package com.plan.server.dto;

import com.plan.server.enums.Status;
import lombok.Data;

import java.util.List;

@Data
public class ProjectDto {
    private String name;
    private String description;
    private String timeline;
    private List<SubtaskDto> subtasks;
    private Status status;
}