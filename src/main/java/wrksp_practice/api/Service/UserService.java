package wrksp_practice.api.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wrksp_practice.api.DTO.AddEmployeeRequest;
import wrksp_practice.api.Model.Role;
import wrksp_practice.api.Model.User;
import wrksp_practice.api.Repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllEmployees() {
        return userRepository.findAll();
    }

    public User getEmployeeById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + id));
    }

    public User createEmployee(AddEmployeeRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDepartment(request.getDepartment());
        user.setRole(request.getRole() != null ? request.getRole() : Role.EMPLOYEE);
        return userRepository.save(user);
    }

    public User updateEmployee(String id, AddEmployeeRequest request) {
        User user = getEmployeeById(id);
        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getDepartment() != null) user.setDepartment(request.getDepartment());
        if (request.getRole() != null) user.setRole(request.getRole());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        return userRepository.save(user);
    }

    public void deleteEmployee(String id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Employee not found: " + id);
        }
        userRepository.deleteById(id);
    }

    public List<User> searchEmployees(String keyword) {
        return userRepository.searchUsers(keyword);
    }

    public List<User> getEmployeesByRole(Role role) {
        return userRepository.findByRole(role);
    }
}
