package wrksp_practice.api.Config;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import wrksp_practice.api.Model.Priority;
import wrksp_practice.api.Model.Project;
import wrksp_practice.api.Model.ProjectStatus;
import wrksp_practice.api.Model.Role;
import wrksp_practice.api.Model.User;
import wrksp_practice.api.Repository.ProjectRepository;
import wrksp_practice.api.Repository.UserRepository;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return; // already seeded
        }

        // ── Admin ──────────────────────────────────────────────────────────────
        User admin = user("admin", "admin@company.com", "Admin1234!", "System", "Admin", "IT", Role.ADMIN);

        // ── Managers ──────────────────────────────────────────────────────────
        User m1 = user("michael.wilson",  "m.wilson@company.com",   "Manager1!", "Michael", "Wilson",   "Engineering",  Role.PROJECT_MANAGER);
        User m2 = user("sarah.martinez",  "s.martinez@company.com", "Manager2!", "Sarah",   "Martinez",  "Product",      Role.PROJECT_MANAGER);
        User m3 = user("james.anderson",  "j.anderson@company.com", "Manager3!", "James",   "Anderson",  "Design",       Role.PROJECT_MANAGER);
        User m4 = user("linda.taylor",    "l.taylor@company.com",   "Manager4!", "Linda",   "Taylor",    "QA",           Role.PROJECT_MANAGER);
        User m5 = user("robert.thomas",   "r.thomas@company.com",   "Manager5!", "Robert",  "Thomas",    "DevOps",       Role.PROJECT_MANAGER);

        // ── Employees ─────────────────────────────────────────────────────────
        User e1 = user("alice.johnson",   "a.johnson@company.com",  "Employee1!", "Alice",  "Johnson",  "Engineering",  Role.EMPLOYEE);
        User e2 = user("bob.smith",       "b.smith@company.com",    "Employee2!", "Bob",    "Smith",    "Engineering",  Role.EMPLOYEE);
        User e3 = user("carol.white",     "c.white@company.com",    "Employee3!", "Carol",  "White",    "Design",       Role.EMPLOYEE);
        User e4 = user("david.brown",     "d.brown@company.com",    "Employee4!", "David",  "Brown",    "QA",           Role.EMPLOYEE);
        User e5 = user("emma.davis",      "e.davis@company.com",    "Employee5!", "Emma",   "Davis",    "Product",      Role.EMPLOYEE);

        userRepository.saveAll(List.of(admin, m1, m2, m3, m4, m5, e1, e2, e3, e4, e5));

        // Reload to get MongoDB-generated IDs
        m1 = userRepository.findByUsername("michael.wilson").orElseThrow();
        m2 = userRepository.findByUsername("sarah.martinez").orElseThrow();
        m3 = userRepository.findByUsername("james.anderson").orElseThrow();
        m4 = userRepository.findByUsername("linda.taylor").orElseThrow();
        m5 = userRepository.findByUsername("robert.thomas").orElseThrow();

        e1 = userRepository.findByUsername("alice.johnson").orElseThrow();
        e2 = userRepository.findByUsername("bob.smith").orElseThrow();
        e3 = userRepository.findByUsername("carol.white").orElseThrow();
        e4 = userRepository.findByUsername("david.brown").orElseThrow();
        e5 = userRepository.findByUsername("emma.davis").orElseThrow();

        // ── Projects ──────────────────────────────────────────────────────────
        Project p1 = project(
            "Customer Portal Redesign",
            "Revamp the customer-facing portal with a modern UI and improved UX.",
            ProjectStatus.ACTIVE, Priority.HIGH,
            m1.getId(), List.of(e1.getId(), e2.getId(), e3.getId()),
            LocalDate.of(2026, 1, 15), LocalDate.of(2026, 8, 30)
        );

        Project p2 = project(
            "Mobile App v2.0",
            "Build the second major version of the mobile application with offline support.",
            ProjectStatus.PLANNING, Priority.CRITICAL,
            m2.getId(), List.of(e2.getId(), e5.getId()),
            LocalDate.of(2026, 3, 1), LocalDate.of(2026, 12, 1)
        );

        Project p3 = project(
            "Internal HR System",
            "Develop an in-house HR management system to replace the legacy tool.",
            ProjectStatus.ACTIVE, Priority.MEDIUM,
            m3.getId(), List.of(e3.getId(), e4.getId()),
            LocalDate.of(2026, 2, 10), LocalDate.of(2026, 9, 15)
        );

        Project p4 = project(
            "CI/CD Pipeline Overhaul",
            "Migrate all pipelines to GitHub Actions and containerize services with Docker.",
            ProjectStatus.ON_HOLD, Priority.HIGH,
            m5.getId(), List.of(e1.getId(), e4.getId(), e5.getId()),
            LocalDate.of(2025, 11, 1), LocalDate.of(2026, 7, 31)
        );

        Project p5 = project(
            "Data Analytics Dashboard",
            "Build a real-time analytics dashboard for business intelligence reporting.",
            ProjectStatus.PLANNING, Priority.LOW,
            m4.getId(), List.of(e2.getId(), e3.getId()),
            LocalDate.of(2026, 6, 1), LocalDate.of(2027, 1, 31)
        );

        projectRepository.saveAll(List.of(p1, p2, p3, p4, p5));

        System.out.println(">>> Seed data loaded: 1 admin, 5 managers, 5 employees, 5 projects.");
    }

    private User user(String username, String email, String password,
                      String firstName, String lastName, String department, Role role) {
        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(password));
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setDepartment(department);
        u.setRole(role);
        return u;
    }

    private Project project(String name, String description,
                            ProjectStatus status, Priority priority,
                            String leadId, List<String> memberIds,
                            LocalDate startDate, LocalDate dueDate) {
        Project p = new Project();
        p.setName(name);
        p.setDescription(description);
        p.setStatus(status);
        p.setPriority(priority);
        p.setProjectLeadId(leadId);
        p.getMemberIds().addAll(memberIds);
        p.setStartDate(startDate);
        p.setDueDate(dueDate);
        return p;
    }
}
