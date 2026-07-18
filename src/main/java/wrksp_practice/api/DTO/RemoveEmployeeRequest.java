package wrksp_practice.api.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RemoveEmployeeRequest {

    @NotBlank
    private String employeeId;
}
