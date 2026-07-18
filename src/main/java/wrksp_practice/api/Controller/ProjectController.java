package wrksp_practice.api.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import wrksp_practice.api.DTO.AddProjectRequest;
import wrksp_practice.api.DTO.ProjectFilterRequest;
import wrksp_practice.api.DTO.StatusUpdateRequest;
import wrksp_practice.api.Model.Project;
import wrksp_practice.api.Service.ProjectService;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable String id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public ResponseEntity<Project> createProject(@Valid @RequestBody AddProjectRequest request) {
        return ResponseEntity.ok(projectService.createProject(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public ResponseEntity<Project> updateProject(@PathVariable String id,
                                                 @RequestBody AddProjectRequest request) {
        return ResponseEntity.ok(projectService.updateProject(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProject(@PathVariable String id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/members/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public ResponseEntity<Project> assignEmployee(@PathVariable String id,
                                                  @PathVariable String userId) {
        return ResponseEntity.ok(projectService.assignEmployee(id, userId));
    }

    @DeleteMapping("/{id}/members/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public ResponseEntity<Project> removeEmployee(@PathVariable String id,
                                                   @PathVariable String userId) {
        return ResponseEntity.ok(projectService.removeEmployee(id, userId));
    }

    @PutMapping("/{id}/lead/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public ResponseEntity<Project> setProjectLead(@PathVariable String id,
                                                   @PathVariable String userId) {
        return ResponseEntity.ok(projectService.setProjectLead(id, userId));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public ResponseEntity<Project> updateStatus(@PathVariable String id,
                                                @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(projectService.updateStatus(id, request.getStatus()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Project>> searchProjects(@ModelAttribute ProjectFilterRequest filter) {
        return ResponseEntity.ok(projectService.filterProjects(filter));
    }
}
