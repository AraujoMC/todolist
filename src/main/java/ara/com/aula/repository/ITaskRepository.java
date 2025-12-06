package ara.com.aula.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ara.com.aula.model.TaskModel;

public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {
    TaskModel findByTitle(String taskTitle);
    List <TaskModel> findByUserID(UUID taskUserID);
}
