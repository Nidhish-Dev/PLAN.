package com.plan.server.service;

import com.plan.server.dto.ProjectDto;
import com.plan.server.enums.Status;
import com.plan.server.model.Project;
import com.plan.server.model.Subtask;
import com.plan.server.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> listProjects(String userId) {
        return projectRepository.findByUserId(userId);
    }

    public Project addProject(String userId, ProjectDto dto) {
        Project project = new Project();
        project.setUserId(userId);
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setTimeline(dto.getTimeline());
        project.setSubtasks(dto.getSubtasks().stream().map(this::toSubtask).collect(Collectors.toList()));
        project.setStatus(dto.getStatus());
        return projectRepository.save(project);
    }

    public Project updateProject(String userId, String id, ProjectDto dto) {
        Project project = getOwnedProject(userId, id);
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setTimeline(dto.getTimeline());
        project.setSubtasks(dto.getSubtasks().stream().map(this::toSubtask).collect(Collectors.toList()));
        project.setStatus(dto.getStatus());
        return projectRepository.save(project);
    }

    public Project updateStatus(String userId, String id, Status status) {
        Project project = getOwnedProject(userId, id);
        project.setStatus(status);
        return projectRepository.save(project);
    }

    public void deleteProject(String userId, String id) {
        Project project = getOwnedProject(userId, id);
        projectRepository.delete(project);
    }

    private Project getOwnedProject(String userId, String id) {
        return projectRepository.findById(id)
                .filter(p -> p.getUserId().equals(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found or not owned"));
    }

    private Subtask toSubtask(com.plan.server.dto.SubtaskDto dto) {
        Subtask subtask = new Subtask();
        subtask.setDescription(dto.getDescription());
        subtask.setPriority(dto.getPriority());
        return subtask;
    }
}