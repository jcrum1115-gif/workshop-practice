package wrksp_practice.api.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import wrksp_practice.api.Model.Project;
import wrksp_practice.api.Model.User;
import wrksp_practice.api.Service.ProjectService;
import wrksp_practice.api.Service.UserService;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
public class ManagerController {

    private final ProjectService projectService;
    private final UserService userService;

    // Get all members of a project
    @GetMapping("/projects/{projectId}/members")
    public ResponseEntity<List<User>> getProjectMembers(@PathVariable String projectId) {
        Project project = projectService.getProjectById(projectId);
        List<User> members = project.getMemberIds().stream()
                .map(userService::getEmployeeById)
                .collect(Collectors.toList());
        return ResponseEntity.ok(members);
    }

    // Get all projects an employee belongs to
    @GetMapping("/users/{userId}/projects")
    public ResponseEntity<List<Project>> getEmployeeProjects(@PathVariable String userId) {
        List<Project> projects = projectService.getAllProjects().stream()
                .filter(p -> p.getMemberIds().contains(userId) ||
                             userId.equals(p.getProjectLeadId()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(projects);
    }

    // Get the project lead of a project
    @GetMapping("/projects/{projectId}/lead")
    public ResponseEntity<User> getProjectLead(@PathVariable String projectId) {
        Project project = projectService.getProjectById(projectId);
        if (project.getProjectLeadId() == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(userService.getEmployeeById(project.getProjectLeadId()));
    }
}
