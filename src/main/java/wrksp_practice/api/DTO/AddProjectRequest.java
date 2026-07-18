package wrksp_practice.api.DTO;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import wrksp_practice.api.Model.Priority;
import wrksp_practice.api.Model.ProjectStatus;

@Data
public class AddProjectRequest {

    @NotBlank
    private String name;

    private String description;
    private ProjectStatus status = ProjectStatus.PLANNING;
    private Priority priority = Priority.MEDIUM;
    private String projectLeadId;
    private LocalDate startDate;
    private LocalDate dueDate;
}
