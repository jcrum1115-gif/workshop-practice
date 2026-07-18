package wrksp_practice.api.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import wrksp_practice.api.DTO.AddEmployeeRequest;
import wrksp_practice.api.Model.Role;
import wrksp_practice.api.Model.User;
import wrksp_practice.api.Service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllEmployees() {
        return ResponseEntity.ok(userService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getEmployeeById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getEmployeeById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createEmployee(@Valid @RequestBody AddEmployeeRequest request) {
        return ResponseEntity.ok(userService.createEmployee(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public ResponseEntity<User> updateEmployee(@PathVariable String id,
                                               @RequestBody AddEmployeeRequest request) {
        return ResponseEntity.ok(userService.updateEmployee(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String id) {
        userService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchEmployees(@RequestParam String keyword) {
        return ResponseEntity.ok(userService.searchEmployees(keyword));
    }

    @GetMapping("/by-role")
    public ResponseEntity<List<User>> getByRole(@RequestParam Role role) {
        return ResponseEntity.ok(userService.getEmployeesByRole(role));
    }
}
