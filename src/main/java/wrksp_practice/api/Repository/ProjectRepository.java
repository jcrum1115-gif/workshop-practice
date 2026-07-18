package wrksp_practice.api.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import wrksp_practice.api.Model.Priority;
import wrksp_practice.api.Model.Project;
import wrksp_practice.api.Model.ProjectStatus;

public interface ProjectRepository extends MongoRepository<Project, String> {

    List<Project> findByStatus(ProjectStatus status);

    List<Project> findByPriority(Priority priority);

    List<Project> findByProjectLeadId(String userId);

    List<Project> findByMemberIdsContaining(String userId);

    List<Project> findByStatusAndPriority(ProjectStatus status, Priority priority);

    @Query("{ $or: [ { 'name': { $regex: ?0, $options: 'i' } }, { 'description': { $regex: ?0, $options: 'i' } } ] }")
    List<Project> searchProjects(String keyword);
}
