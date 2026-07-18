package wrksp_practice.api.DTO;

import lombok.Data;
import wrksp_practice.api.Model.ProjectStatus;

@Data
public class StatusUpdateRequest {
    private ProjectStatus status;
}
