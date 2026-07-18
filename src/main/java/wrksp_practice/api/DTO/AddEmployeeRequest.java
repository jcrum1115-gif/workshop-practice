package wrksp_practice.api.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import wrksp_practice.api.Model.Role;

@Data
public class AddEmployeeRequest {

    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String department;
    private Role role = Role.EMPLOYEE;
}
