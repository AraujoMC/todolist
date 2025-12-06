package ara.com.todolist.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ara.com.todolist.model.UserModel;

public interface IUserRepository extends JpaRepository <UserModel, UUID> {
    UserModel findByUsername(String username); 
}