package wrksp_practice.api.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wrksp_practice.api.DTO.AddProjectRequest;
import wrksp_practice.api.DTO.ProjectFilterRequest;
import wrksp_practice.api.Model.Priority;
import wrksp_practice.api.Model.Project;
import wrksp_practice.api.Model.ProjectStatus;
import wrksp_practice.api.Repository.ProjectRepository;
import wrksp_practice.api.Repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(String id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + id));
    }

    public Project createProject(AddProjectRequest request) {
        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStatus(request.getStatus() != null ? request.getStatus() : ProjectStatus.PLANNING);
        project.setPriority(request.getPriority() != null ? request.getPriority() : Priority.MEDIUM);
        project.setProjectLeadId(request.getProjectLeadId());
        project.setStartDate(request.getStartDate());
        project.setDueDate(request.getDueDate());
        return projectRepository.save(project);
    }

    public Project updateProject(String id, AddProjectRequest request) {
        Project project = getProjectById(id);
        if (request.getName() != null) project.setName(request.getName());
        if (request.getDescription() != null) project.setDescription(request.getDescription());
        if (request.getStatus() != null) project.setStatus(request.getStatus());
        if (request.getPriority() != null) project.setPriority(request.getPriority());
        if (request.getProjectLeadId() != null) project.setProjectLeadId(request.getProjectLeadId());
        if (request.getStartDate() != null) project.setStartDate(request.getStartDate());
        if (request.getDueDate() != null) project.setDueDate(request.getDueDate());
        project.setUpdatedAt(LocalDateTime.now());
        return projectRepository.save(project);
    }

    public void deleteProject(String id) {
        if (!projectRepository.existsById(id)) {
            throw new IllegalArgumentException("Project not found: " + id);
        }
        projectRepository.deleteById(id);
    }

    public Project assignEmployee(String projectId, String userId) {
        Project project = getProjectById(projectId);
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("Employee not found: " + userId);
        }
        if (!project.getMemberIds().contains(userId)) {
            project.getMemberIds().add(userId);
            project.setUpdatedAt(LocalDateTime.now());
            projectRepository.save(project);
        }
        return project;
    }

    public Project removeEmployee(String projectId, String userId) {
        Project project = getProjectById(projectId);
        project.getMemberIds().remove(userId);
        project.setUpdatedAt(LocalDateTime.now());
        return projectRepository.save(project);
    }

    public Project setProjectLead(String projectId, String userId) {
        Project project = getProjectById(projectId);
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("Employee not found: " + userId);
        }
        project.setProjectLeadId(userId);
        project.setUpdatedAt(LocalDateTime.now());
        return projectRepository.save(project);
    }

    public Project updateStatus(String projectId, ProjectStatus status) {
        Project project = getProjectById(projectId);
        project.setStatus(status);
        project.setUpdatedAt(LocalDateTime.now());
        return projectRepository.save(project);
    }

    public List<Project> filterProjects(ProjectFilterRequest filter) {
        if (filter.getKeyword() != null && !filter.getKeyword().isBlank()) {
            return projectRepository.searchProjects(filter.getKeyword());
        }
        if (filter.getStatus() != null && filter.getPriority() != null) {
            return projectRepository.findByStatusAndPriority(filter.getStatus(), filter.getPriority());
        }
        if (filter.getStatus() != null) {
            return projectRepository.findByStatus(filter.getStatus());
        }
        if (filter.getPriority() != null) {
            return projectRepository.findByPriority(filter.getPriority());
        }
        if (filter.getProjectLeadId() != null) {
            return projectRepository.findByProjectLeadId(filter.getProjectLeadId());
        }
        if (filter.getMemberId() != null) {
            return projectRepository.findByMemberIdsContaining(filter.getMemberId());
        }
        return projectRepository.findAll();
    }
}
