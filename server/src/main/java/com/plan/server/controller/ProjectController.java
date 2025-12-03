package com.plan.server.controller;

import com.plan.server.dto.ProjectDto;
import com.plan.server.dto.StatusUpdateDto;
import com.plan.server.model.Project;
import com.plan.server.model.User;
import com.plan.server.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public List<Project> listProjects(@AuthenticationPrincipal User user) {
        return projectService.listProjects(user.getId());
    }

    @PostMapping
    public ResponseEntity<Project> addProject(@AuthenticationPrincipal User user, @RequestBody ProjectDto dto) {
        Project project = projectService.addProject(user.getId(), dto);
        return ResponseEntity.ok(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@AuthenticationPrincipal User user, @PathVariable String id, @RequestBody ProjectDto dto) {
        Project project = projectService.updateProject(user.getId(), id, dto);
        return ResponseEntity.ok(project);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Project> updateStatus(@AuthenticationPrincipal User user, @PathVariable String id, @RequestBody StatusUpdateDto dto) {
        Project project = projectService.updateStatus(user.getId(), id, dto.getStatus());
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@AuthenticationPrincipal User user, @PathVariable String id) {
        projectService.deleteProject(user.getId(), id);
        return ResponseEntity.noContent().build();
    }
}