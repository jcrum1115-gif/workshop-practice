package wrksp_practice.api.DTO;

import lombok.Data;
import wrksp_practice.api.Model.Priority;
import wrksp_practice.api.Model.ProjectStatus;

@Data
public class ProjectFilterRequest {
    private String keyword;
    private ProjectStatus status;
    private Priority priority;
    private String projectLeadId;
    private String memberId;
}
